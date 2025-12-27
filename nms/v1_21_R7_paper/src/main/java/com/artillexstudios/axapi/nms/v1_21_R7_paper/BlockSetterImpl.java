package com.artillexstudios.axapi.nms.v1_21_R7_paper;

import com.artillexstudios.axapi.executor.ExceptionReportingScheduledThreadPool;
import com.artillexstudios.axapi.nms.v1_21_R7_paper.wrapper.WorldWrapper;
import com.artillexstudios.axapi.selection.BlockSetter;
import com.artillexstudios.axapi.utils.logging.LogUtils;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraft.world.level.levelgen.Heightmap;
import org.bukkit.block.data.BlockData;
import org.bukkit.craftbukkit.block.data.CraftBlockData;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.concurrent.ExecutorService;

public class BlockSetterImpl implements BlockSetter {
    private static final ExecutorService executor = new ExceptionReportingScheduledThreadPool(1);
    private final WorldWrapper wrapper;
    private final ServerLevel level;
    private final ArrayList<ChunkPos> chunks = new ArrayList<>();
    private LevelChunk chunk = null;

    public BlockSetterImpl(WorldWrapper wrapper) {
        this.wrapper = wrapper;
        this.level = wrapper.asMinecraft();
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
            LogUtils.debug("Finalising chunk at {}, {}", chunk.x, chunk.z);
            LevelChunk levelChunk = level.getChunk(chunk.x, chunk.z);
            levelChunk.markUnsaved();
//            sendUpdatePacket(levelChunk);
            this.wrapper.wrapped().refreshChunk(chunk.x, chunk.z);
            LogUtils.debug("Refreshed chunk!");
        }

        relight();
        LogUtils.debug("Did relighting!");

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

//    private void sendUpdatePacket(@NotNull LevelChunk chunk) {
//        List<ChunkHolder> visibleChunkHolders = PlatformHooks.get().getVisibleChunkHolders(this.level);
//        ChunkHolder playerChunk = level.getChunkSource().getChunkAtIfCachedImmediately(chunk.getPos().x, chunk.getPos().z);
//        if (playerChunk == null) return;
//        List<ServerPlayer> playersInRange = playerChunk.playerProvider.getPlayers(playerChunk.getPos(), false);
//
//        executor.execute(() -> {
//            ClientboundLevelChunkWithLightPacket lightPacket = new ClientboundLevelChunkWithLightPacket(chunk, level.getLightEngine(), null, null, false);
//            int size = playersInRange.size();
//            for (int i = 0; i < size; i++) {
//                ServerPlayer player = playersInRange.get(i);
//                player.connection.send(lightPacket);
//            }
//        });
//    }
}
