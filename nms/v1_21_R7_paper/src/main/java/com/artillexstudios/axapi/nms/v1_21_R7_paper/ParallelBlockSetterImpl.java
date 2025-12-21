package com.artillexstudios.axapi.nms.v1_21_R7_paper;

import com.artillexstudios.axapi.selection.Cuboid;
import com.artillexstudios.axapi.selection.ParallelBlockSetter;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ChunkHolder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraft.world.level.levelgen.Heightmap;
import org.apache.commons.math3.distribution.EnumeratedDistribution;
import org.apache.commons.math3.util.Pair;
import org.bukkit.block.data.BlockData;
import org.bukkit.craftbukkit.block.data.CraftBlockData;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.IntConsumer;

public class ParallelBlockSetterImpl implements ParallelBlockSetter {
    private static final ExecutorService executor = Executors.newSingleThreadExecutor();
    private static final ExecutorService parallelExecutor = Executors.newVirtualThreadPerTaskExecutor();
    private static final Logger log = LoggerFactory.getLogger(ParallelBlockSetterImpl.class);
//    private static final ArrayList<FastFieldAccessor> accessors = new ArrayList<>();
//    private static final FastFieldAccessor dataAccessor = FastFieldAccessor.forClassField(PalettedContainer.class, "d");
//    private static final FastFieldAccessor statesAccessor = FastFieldAccessor.forClassField(LevelChunkSection.class, "h");
//    private static final Method storage = ClassUtils.INSTANCE.getClass("net.minecraft.world.level.chunk.DataPaletteBlock$c").getRecordComponents()[1].getAccessor();
//    private static final BlockState[] presetBlockStates = new BlockState[]{Blocks.STONE.defaultBlockState()};

//    static {
//        for (Field field : LevelChunkSection.class.getDeclaredFields()) {
//            if (Modifier.isStatic(field.getModifiers())) continue;
//            field.setAccessible(true);
//
//            FastFieldAccessor fieldAccessor = FastFieldAccessor.forField(field);
//            accessors.add(fieldAccessor);
//        }
//
//        storage.setAccessible(true);
//    }

    private final ServerLevel level;

    public ParallelBlockSetterImpl(ServerLevel level) {
        this.level = level;
    }

//    public void copyFields(LevelChunkSection fromSection, LevelChunkSection toSection) {
//        for (FastFieldAccessor accessor : accessors) {
//            if (accessor.getField().getType() == Short.TYPE) {
//                accessor.setShort(toSection, accessor.getShort(fromSection));
//            } else if (accessor.getField().getType() == Integer.TYPE) {
//                accessor.setInt(toSection, accessor.getInt(fromSection));
//            } else if (accessor.getField().getType() == PalettedContainer.class) {
//                PalettedContainer<?> container = accessor.get(fromSection);
//                accessor.set(toSection, container.copy());
//            } /*else if (PaperUtils.isPaper() && accessor.getField().getType() == IBlockDataList.class) { // TODO: Update when paper updates
//                accessor.set(toSection, IBlockDataListCopier.copy(accessor.get(fromSection)));
//            }*/ else if (accessor.getField().getType() == long[].class) {
//                long[] longs = accessor.get(fromSection);
//                if (longs != null) {
//                    accessor.set(toSection, Arrays.copyOf(longs, longs.length));
//                    continue;
//                }
//                accessor.set(toSection, null);
//            } else {
//                log.error("No copier for class {}; classname: {} field name {} typename: {}!", accessor.getField().getType(), accessor.getField().getType().getName(), accessor.getField().getName(), accessor.getField().getType().getTypeName());
//            }
//        }
//
//        PalettedContainer<?> container = statesAccessor.get(toSection);
//        Object data = dataAccessor.get(container);
//        Object storageClass;
//        try {
//            storageClass = storage.invoke(data).getClass();
//        } catch (Exception exception) {
//            log.error("An unexpected error occurred while accessing states!", exception);
//            return;
//        }
//
//        // It's a chunk with zerobitstorage, we need to replace it with a simplebitstorage
//        if (storageClass == ZeroBitStorage.class) {
//            statesAccessor.set(toSection, new PalettedContainer<>(Block.BLOCK_STATE_REGISTRY, Blocks.AIR.defaultBlockState(), PalettedContainer.Strategy.SECTION_STATES, presetBlockStates));
//        }
//    }

    public LevelChunkSection copy(LevelChunkSection section) {
        try {
//            LevelChunkSection newSection = ClassUtils.INSTANCE.newInstance(LevelChunkSection.class);
//            copyFields(section, newSection);
            Constructor<LevelChunkSection> constructor = LevelChunkSection.class.getDeclaredConstructor(LevelChunkSection.class);
            constructor.setAccessible(true);
            LevelChunkSection newSection = constructor.newInstance(section);
//            LevelChunkSection newSection = new LevelChunkSection();

            return newSection;
        } catch (Exception exception) {
            log.error("An unexpected issue occurred while initializing ParallelBlockSetter. Is your version supported?", exception);
            throw new RuntimeException(exception);
        }
    }

    public void fill(Cuboid selection, EnumeratedDistribution<BlockData> distribution, IntConsumer consumer) {
        AtomicInteger blockCount = new AtomicInteger();
        int chunkMinX = selection.getMinX() >> 4;
        int chunkMaxX = selection.getMaxX() >> 4;
        int chunkMinZ = selection.getMinZ() >> 4;
        int chunkMaxZ = selection.getMaxZ() >> 4;

        List<Pair<BlockData, Double>> pmf = distribution.getPmf();

        List<CompletableFuture<?>> chunkTasks = new ArrayList<>();
        for (int chunkX = chunkMinX; chunkX <= chunkMaxX; chunkX++) {
            int minX = Math.max(selection.getMinX(), chunkX << 4);
            int maxX = Math.min(selection.getMaxX(), (chunkX << 4) + 15);

            for (int chunkZ = chunkMinZ; chunkZ <= chunkMaxZ; chunkZ++) {
                int minZ = Math.max(selection.getMinZ(), chunkZ << 4);
                int maxZ = Math.min(selection.getMaxZ(), (chunkZ << 4) + 15);
                LevelChunk levelChunk = level.getChunk(chunkX, chunkZ);
                List<CompletableFuture<?>> chunkFutures = new ArrayList<>();

                int lastSectionIndex = -1;

                for (int y = selection.getMinY(); y <= selection.getMaxY(); y++) {
                    int sectionIndex = levelChunk.getSectionIndex(y);

                    if (lastSectionIndex != sectionIndex) {
                        lastSectionIndex = sectionIndex;

                        LevelChunkSection section = levelChunk.getSection(sectionIndex);
                        LevelChunkSection newSection = copy(section);

                        CompletableFuture<?> future = CompletableFuture.runAsync(() -> {
                            EnumeratedDistribution<BlockData> newDistribution = new EnumeratedDistribution<>(pmf);

                            for (int i = selection.getMinY(); i <= selection.getMaxY(); i++) {
                                int m = levelChunk.getSectionIndex(i);
                                if (m < sectionIndex) continue;
                                if (m > sectionIndex) break;

                                for (int x = minX; x <= maxX; x++) {
                                    for (int z = minZ; z <= maxZ; z++) {
                                        CraftBlockData type = (CraftBlockData) newDistribution.sample();

                                        int j = x & 15;
                                        int k = i & 15;
                                        int l = z & 15;

                                        var state = type.getState();
                                        blockCount.incrementAndGet();
                                        newSection.setBlockState(j, k, l, state, true);
                                        updateHeightMap(levelChunk, j, i, l, state);
                                    }
                                }
                            }

                            try {
                                MinecraftServer.getServer().submit(() -> {
                                    levelChunk.getSections()[sectionIndex] = newSection;
                                }).get();
                            } catch (InterruptedException | ExecutionException e) {
                                throw new RuntimeException(e);
                            }
                        }, parallelExecutor);

                        chunkFutures.add(future);
                    }
                }

                CompletableFuture<?> thisChunk = CompletableFuture.allOf(chunkFutures.toArray(new CompletableFuture[0]));

                thisChunk.thenAccept((ignored) -> MinecraftServer.getServer().submit(() -> {
                    var lightEngine = level.chunkSource.getLightEngine();
                    levelChunk.markUnsaved();
                    sendUpdatePacket(levelChunk);
//                    lightEngine.starlight$serverRelightChunks(Sets.newHashSet(levelChunk.getPos()), c -> {
//                    }, c -> {
//                    });


                }));

                chunkTasks.add(thisChunk);
            }
        }

        CompletableFuture<?> future = CompletableFuture.allOf(chunkTasks.toArray(new CompletableFuture[0]));
        future.thenAccept((ignored) -> consumer.accept(blockCount.get()));
    }

    private void sendUpdatePacket(@NotNull LevelChunk chunk) {
        ChunkHolder playerChunk = level.getChunkSource().chunkMap.getVisibleChunkIfPresent(chunk.getPos().longKey);
        if (playerChunk == null) return;
        List<ServerPlayer> playersInRange = playerChunk.playerProvider.getPlayers(playerChunk.getPos(), false);

//        ClientboundLevelChunkWithLightPacket lightPacket = new ClientboundLevelChunkWithLightPacket(chunk, level.getLightEngine(), null, null, false);
//        executor.execute(() -> {
//            int size = playersInRange.size();
//            for (int i = 0; i < size; i++) {
//                ServerPlayer player = playersInRange.get(i);
//                player.connection.send(lightPacket);
//            }
//        });
    }

    private void updateHeightMap(ChunkAccess chunk, int x, int y, int z, BlockState blockState) {
        chunk.heightmaps.get(Heightmap.Types.MOTION_BLOCKING).update(x, y, z, blockState);
        chunk.heightmaps.get(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES).update(x, y, z, blockState);
        chunk.heightmaps.get(Heightmap.Types.OCEAN_FLOOR).update(x, y, z, blockState);
        chunk.heightmaps.get(Heightmap.Types.WORLD_SURFACE).update(x, y, z, blockState);
    }
}
