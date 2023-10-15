package com.artillexstudios.axapi.nms.v1_20_R1;

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
import net.minecraft.world.level.levelgen.Heightmap;
import org.bukkit.World;
import org.bukkit.block.data.BlockData;
import org.bukkit.craftbukkit.v1_20_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_20_R1.block.data.CraftBlockData;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;

public class BlockSetterImpl implements BlockSetter {
    private final ServerLevel level;
    private final World world;
    private final HashSet<ChunkPos> chunks = new LinkedHashSet<>();

    public BlockSetterImpl(World world) {
        this.world = world;
        this.level = ((CraftWorld) world).getHandle();
    }

    @Override
    public void setBlock(int x, int y, int z, BlockData data) {
        var levelChunk = level.getChunk(x >> 4, z >> 4);
        var state = ((CraftBlockData) data).getState();
        var section = levelChunk.getSection(levelChunk.getSectionIndex(y));

        if (section.hasOnlyAir() && state.isAir()) {
            return;
        }

        var j = x & 15;
        var l = z & 15;
        chunks.add(levelChunk.getPos());
        section.setBlockState(j, y & 15, l, state, true);

        updateHeightMap(levelChunk, j, y, l, state);
    }

    @Override
    public void finalise() {
        relight();

        for (ChunkPos chunk : chunks) {
            sendUpdatePacket(level.getChunk(chunk.x, chunk.z));
        }

        chunks.clear();
    }

    private void updateHeightMap(ChunkAccess chunk, int x, int y, int z, BlockState blockState) {
        chunk.heightmaps.get(Heightmap.Types.MOTION_BLOCKING).update(x, y, z, blockState);
        chunk.heightmaps.get(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES).update(x, y, z, blockState);
        chunk.heightmaps.get(Heightmap.Types.OCEAN_FLOOR).update(x, y, z, blockState);
        chunk.heightmaps.get(Heightmap.Types.WORLD_SURFACE).update(x, y, z, blockState);
    }

    private void relight() {
        MinecraftServer.getServer().executeBlocking(() -> {
            var lightEngine = level.chunkSource.getLightEngine();
            lightEngine.relight(chunks, c -> {
            }, c -> {
            });
        });
    }

    private void sendUpdatePacket(@NotNull LevelChunk chunk) {
        ChunkHolder playerChunk = level.getChunkSource().chunkMap.getVisibleChunkIfPresent(chunk.getPos().longKey);
        if (playerChunk == null) return;
        List<ServerPlayer> playersInRange = playerChunk.playerProvider.getPlayers(playerChunk.getPos(), false);

        ClientboundLevelChunkWithLightPacket lightPacket = new ClientboundLevelChunkWithLightPacket(chunk, level.getLightEngine(), null, null, false);
        int size = playersInRange.size();
        for (int i = 0; i < size; i++) {
            ServerPlayer player = playersInRange.get(i);
            player.connection.send(lightPacket);
        }
    }
}
