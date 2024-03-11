package com.artillexstudios.axapi.nms.v1_19_R1;

import com.artillexstudios.axapi.selection.Cuboid;
import com.artillexstudios.axapi.selection.ParallelBlockSetter;
import com.artillexstudios.axapi.utils.FastFieldAccessor;
import com.google.common.collect.Sets;
import net.minecraft.network.protocol.game.ClientboundLevelChunkWithLightPacket;
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
import org.bukkit.World;
import org.bukkit.block.data.BlockData;
import org.bukkit.craftbukkit.v1_19_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_19_R1.block.data.CraftBlockData;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
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
    private static final ExecutorService parallelExecutor = Executors.newFixedThreadPool(8);
    private static final Logger log = LoggerFactory.getLogger(ParallelBlockSetterImpl.class);
    private static final ArrayList<FastFieldAccessor> accessors = new ArrayList<>();
    private static Unsafe unsafe = null;

    static {
        try {
            Field f = Unsafe.class.getDeclaredField("theUnsafe");
            f.setAccessible(true);
            unsafe = (Unsafe) f.get(null);
        } catch (Exception exception) {
            log.error("An error occurred while initializing FastFieldAccessor!", exception);
        }

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
        for (FastFieldAccessor accessor : accessors) {
            if (accessor.getField().getType() == Boolean.TYPE) {
                accessor.setBoolean(toSection, accessor.getBoolean(fromSection));
            } else if (accessor.getField().getType() == Byte.TYPE) {
                accessor.setByte(toSection, accessor.getByte(fromSection));
            } else if (accessor.getField().getType() == Short.TYPE) {
                accessor.setShort(toSection, accessor.getShort(fromSection));
            } else if (accessor.getField().getType() == Character.TYPE) {
                accessor.setChar(toSection, accessor.getChar(fromSection));
            } else if (accessor.getField().getType() == Integer.TYPE) {
                accessor.setInt(toSection, accessor.getInt(fromSection));
            } else if (accessor.getField().getType() == Long.TYPE) {
                accessor.setLong(toSection, accessor.getLong(fromSection));
            } else if (accessor.getField().getType() == Float.TYPE) {
                accessor.setFloat(toSection, accessor.getFloat(fromSection));
            } else if (accessor.getField().getType() == Double.TYPE) {
                accessor.setDouble(toSection, accessor.getDouble(fromSection));
            } else {
                accessor.set(toSection, accessor.get(fromSection));
            }
        }
    }

    public LevelChunkSection copy(LevelChunkSection section) {
        try {
            LevelChunkSection newSection = (LevelChunkSection) unsafe.allocateInstance(LevelChunkSection.class);
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

                for (int y = selection.getMinY(); y <= selection.getMaxY(); y++) {
                    int sectionIndex = levelChunk.getSectionIndex(y);
                    LevelChunkSection section = levelChunk.getSection(sectionIndex);

                    int finalY = y;
                    CompletableFuture<?> future = CompletableFuture.runAsync(() -> {
                        LevelChunkSection newSection = copy(section);

                        for (int x = minX; x <= maxX; x++) {
                            for (int z = minZ; z <= maxZ; z++) {
                                CraftBlockData type = (CraftBlockData) distribution.sample();

                                int j = x & 15;
                                int k = finalY & 15;
                                int l = z & 15;

                                var state = type.getState();
                                blockCount.incrementAndGet();
                                newSection.setBlockState(j, k, l, state, true);
                                updateHeightMap(levelChunk, j, finalY, l, state);
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

                CompletableFuture<?> thisChunk = CompletableFuture.allOf(chunkFutures.toArray(new CompletableFuture[0]));

                thisChunk.thenAccept((ignored) -> {
                    MinecraftServer.getServer().executeBlocking(() -> {
                        var lightEngine = level.chunkSource.getLightEngine();
                        lightEngine.relight(Sets.newHashSet(levelChunk.getPos()), c -> {
                        }, c -> {
                        });

                        sendUpdatePacket(levelChunk);
                    });
                });

                chunkTasks.add(thisChunk);
            }
        }

        CompletableFuture<?> future = CompletableFuture.allOf(chunkTasks.toArray(new CompletableFuture[0]));
        future.thenAccept((ignored) -> {
            consumer.accept(blockCount.get());
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

    private void updateHeightMap(ChunkAccess chunk, int x, int y, int z, BlockState blockState) {
        chunk.heightmaps.get(Heightmap.Types.MOTION_BLOCKING).update(x, y, z, blockState);
        chunk.heightmaps.get(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES).update(x, y, z, blockState);
        chunk.heightmaps.get(Heightmap.Types.OCEAN_FLOOR).update(x, y, z, blockState);
        chunk.heightmaps.get(Heightmap.Types.WORLD_SURFACE).update(x, y, z, blockState);
    }
}
