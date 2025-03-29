package com.artillexstudios.axapi.nms.v1_21_R1;

import com.artillexstudios.axapi.selection.BlockSetter;
import net.minecraft.network.protocol.game.ClientboundLevelChunkWithLightPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ChunkHolder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraft.world.level.levelgen.Heightmap;
import org.bukkit.block.data.BlockData;
import org.bukkit.craftbukkit.block.data.CraftBlockData;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BlockSetterImpl implements BlockSetter {
    private static final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final ServerLevel level;
    private final ArrayList<ChunkPos> chunks = new ArrayList<>();
    private LevelChunk chunk = null;

    public BlockSetterImpl(ServerLevel level) {
        this.level = level;
    }

    @Override
    public void setBlock(int x, int y, int z, BlockData data) {
        int chunkX = x >> 4;
        int chunkZ = z >> 4;
        LevelChunk levelChunk;
        if (this.chunk != null && chunkX == this.chunk.getPos().x && chunkZ == this.chunk.getPos().z) {
            levelChunk = this.chunk;
        } else {
            this.chunk = levelChunk = level.getChunk(chunkX, chunkZ);
            chunks.add(levelChunk.getPos());
        }

        var state = ((CraftBlockData) data).getState();

        int sectionIndex = levelChunk.getSectionIndex(y);
        LevelChunkSection section = levelChunk.getSection(sectionIndex);

        int i = y;
        if (section.hasOnlyAir() && state.isAir()) {
            return;
        }

        int j = x & 15;
        int k = i & 15;
        int l = z & 15;
        section.setBlockState(j, k, l, state, false);

        updateHeightMap(levelChunk, j, y, l, state);
    }

    @Override
    public void finalise() {
        for (ChunkPos chunk : chunks) {
            LevelChunk levelChunk = level.getChunk(chunk.x, chunk.z);
            levelChunk.setUnsaved(true);
            sendUpdatePacket(levelChunk);
        }

        relight();

        chunks.clear();
    }

    private void updateHeightMap(ChunkAccess chunk, int x, int y, int z, BlockState blockState) {
        chunk.heightmaps.get(Heightmap.Types.MOTION_BLOCKING).update(x, y, z, blockState);
        chunk.heightmaps.get(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES).update(x, y, z, blockState);
        chunk.heightmaps.get(Heightmap.Types.OCEAN_FLOOR).update(x, y, z, blockState);
        chunk.heightmaps.get(Heightmap.Types.WORLD_SURFACE).update(x, y, z, blockState);
    }

    private void relight() {
        var set = new HashSet<>(chunks);
        MinecraftServer.getServer().executeBlocking(() -> {
            var lightEngine = level.chunkSource.getLightEngine();
            lightEngine.starlight$serverRelightChunks(set, c -> {
            }, c -> {
            });
        });
    }

    private void sendUpdatePacket(@NotNull LevelChunk chunk) {
        ChunkHolder playerChunk = level.getChunkSource().chunkMap.getVisibleChunkIfPresent(chunk.getPos().longKey);
        if (playerChunk == null) return;
        List<ServerPlayer> playersInRange = playerChunk.playerProvider.getPlayers(playerChunk.getPos(), false);

        executor.execute(() -> {
            ClientboundLevelChunkWithLightPacket lightPacket = new ClientboundLevelChunkWithLightPacket(chunk, level.getLightEngine(), null, null, false);
            int size = playersInRange.size();
            for (int i = 0; i < size; i++) {
                ServerPlayer player = playersInRange.get(i);
                player.connection.send(lightPacket);
            }
        });
    }
}
