package com.artillexstudios.axapi.nms.v1_20_R4;

import com.artillexstudios.axapi.nms.v1_20_R4.utils.IBlockDataListCopier;
import com.artillexstudios.axapi.selection.Cuboid;
import com.artillexstudios.axapi.selection.ParallelBlockSetter;
import com.artillexstudios.axapi.utils.ClassUtils;
import com.artillexstudios.axapi.utils.FastFieldAccessor;
import com.artillexstudios.axapi.utils.FastMethodInvoker;
import com.destroystokyo.paper.util.maplist.IBlockDataList;
import com.google.common.collect.Sets;
import net.minecraft.network.protocol.game.ClientboundLevelChunkWithLightPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ChunkHolder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.ZeroBitStorage;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraft.world.level.chunk.PalettedContainer;
import net.minecraft.world.level.levelgen.Heightmap;
import org.apache.commons.math3.distribution.EnumeratedDistribution;
import org.bukkit.World;
import org.bukkit.block.data.BlockData;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.block.data.CraftBlockData;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.lang.reflect.RecordComponent;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.IntConsumer;

public class ParallelBlockSetterImpl implements ParallelBlockSetter {
    private static final ExecutorService executor = Executors.newSingleThreadExecutor();
    private static final ExecutorService parallelExecutor = Executors.newVirtualThreadPerTaskExecutor();
    private static final Logger log = LoggerFactory.getLogger(ParallelBlockSetterImpl.class);
    private static final ArrayList<FastFieldAccessor> accessors = new ArrayList<>();
    private static final FastFieldAccessor dataAccessor = FastFieldAccessor.forClassField(PalettedContainer.class, "d");
    private static final RecordComponent component = ClassUtils.INSTANCE.getClass("net.minecraft.world.level.chunk.DataPaletteBlock$c").getRecordComponents()[1];


    static {
        for (Field field : LevelChunkSection.class.getDeclaredFields()) {
            if (Modifier.isStatic(field.getModifiers())) continue;
            field.setAccessible(true);

            FastFieldAccessor fieldAccessor = FastFieldAccessor.forField(field);
            accessors.add(fieldAccessor);
        }
    }

    private final ServerLevel level;

    public ParallelBlockSetterImpl(World world) {
        this.level = ((CraftWorld) world).getHandle();
    }

    public void copyFields(LevelChunkSection fromSection, LevelChunkSection toSection) {
        log.error("CopyFields!");
        for (FastFieldAccessor accessor : accessors) {
            if (accessor.getField().getType() == Short.TYPE) {
                log.error("Short!");
                accessor.setShort(toSection, accessor.getShort(fromSection));
            } else if (accessor.getField().getType() == Integer.TYPE) {
                log.error("Int!");
                accessor.setInt(toSection, accessor.getInt(fromSection));
            } else if (accessor.getField().getType() == PalettedContainer.class) {
                log.error("Palettedcontainer!");
                PalettedContainer<?> container = accessor.get(fromSection);
                PalettedContainer<?> copy = container.copy();


                Object data = dataAccessor.get(container);
                Object o;
                try {
                    o = FastMethodInvoker.create(component.getAccessor()).invoke(data);
                } catch (Exception e) {
                    log.error("Doesn't work...", e);
                    throw new RuntimeException(e);
                }

                log.error("Class: {}!", o.getClass());
                if (o.getClass() == ZeroBitStorage.class) {
                    log.error("ZERO BIT STORAGE");
//                    storageAccessor.set(data, new SimpleBitStorage());
                } else {
                    log.error("NOT ZERO BIT STORAGE");
                }

                accessor.set(toSection, copy);
            } else if (accessor.getField().getType() == IBlockDataList.class) {
                log.error("Iblockdatalist!");
                accessor.set(toSection, IBlockDataListCopier.copy(accessor.get(fromSection)));
            } else {
                log.error("No copier for class {}!", accessor.getField().getType().getName());
            }
        }
    }

    public LevelChunkSection copy(LevelChunkSection section) {
        log.error("Copy!");
        try {
            LevelChunkSection newSection = ClassUtils.INSTANCE.newInstance(LevelChunkSection.class);
            copyFields(section, newSection);

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
                        log.info("New section! x: {} z: {} y: {}", chunkX, chunkZ, y);
                        lastSectionIndex = sectionIndex;

                        LevelChunkSection section = levelChunk.getSection(sectionIndex);
                        log.info("Sectionsize: {}, Original section: {}", levelChunk.getSections().length, section);
                        LevelChunkSection newSection = copy(section);

                        CompletableFuture<?> future = CompletableFuture.runAsync(() -> {
                            for (int i = selection.getMinY(); i <= selection.getMaxY(); i++) {
                                int m = levelChunk.getSectionIndex(i);
                                if (m < sectionIndex) continue;
                                if (m > sectionIndex) break;

                                for (int x = minX; x <= maxX; x++) {
                                    for (int z = minZ; z <= maxZ; z++) {
                                        CraftBlockData type = (CraftBlockData) distribution.sample();

                                        int j = x & 15;
                                        int k = i & 15;
                                        int l = z & 15;

                                        var state = type.getState();
                                        blockCount.incrementAndGet();
                                        newSection.setBlockState(j, k, l, state, true);
//                                        updateHeightMap(levelChunk, j, i, l, state);
                                    }
                                }
                            }

                            log.info("Sectionsize: {}, new section: {}", levelChunk.getSections().length, newSection);
                            levelChunk.getSections()[sectionIndex] = newSection;

//                            try {
//                                MinecraftServer.getServer().submit(() -> {
//                                    levelChunk.getSections()[sectionIndex] = newSection;
//                                }).get();
//                            } catch (InterruptedException | ExecutionException e) {
//                                throw new RuntimeException(e);
//                            }
                        }, parallelExecutor);

                        chunkFutures.add(future);
                    } else {
                        log.info("Old section! Y: {}", y);
                    }
                }

                CompletableFuture<?> thisChunk = CompletableFuture.allOf(chunkFutures.toArray(new CompletableFuture[0]));

                thisChunk.thenAccept((ignored) -> MinecraftServer.getServer().submit(() -> {
                    var lightEngine = level.chunkSource.getLightEngine();
                    lightEngine.relight(Sets.newHashSet(levelChunk.getPos()), c -> {
                    }, c -> {
                    });

                    sendUpdatePacket(levelChunk);
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

//        executor.execute(() -> {
        ClientboundLevelChunkWithLightPacket lightPacket = new ClientboundLevelChunkWithLightPacket(chunk, level.getLightEngine(), null, null, false);
        int size = playersInRange.size();
        for (int i = 0; i < size; i++) {
            ServerPlayer player = playersInRange.get(i);
            player.connection.send(lightPacket);
        }
//        });
    }

    private void updateHeightMap(ChunkAccess chunk, int x, int y, int z, BlockState blockState) {
        chunk.heightmaps.get(Heightmap.Types.MOTION_BLOCKING).update(x, y, z, blockState);
        chunk.heightmaps.get(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES).update(x, y, z, blockState);
        chunk.heightmaps.get(Heightmap.Types.OCEAN_FLOOR).update(x, y, z, blockState);
        chunk.heightmaps.get(Heightmap.Types.WORLD_SURFACE).update(x, y, z, blockState);
    }
}
