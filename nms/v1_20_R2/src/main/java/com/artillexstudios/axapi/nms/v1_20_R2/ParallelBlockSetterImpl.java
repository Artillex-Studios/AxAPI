package com.artillexstudios.axapi.nms.v1_20_R2;

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
import org.bukkit.World;
import org.bukkit.block.data.BlockData;
import org.bukkit.craftbukkit.v1_20_R2.CraftWorld;
import org.bukkit.craftbukkit.v1_20_R2.block.data.CraftBlockData;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import org.apache.commons.math3.distribution.EnumeratedDistribution;
import com.artillexstudios.axapi.selection.Cuboid;
import com.artillexstudios.axapi.selection.ParallelBlockSetter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.misc.Unsafe;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import net.minecraft.world.level.chunk.PalettedContainer;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.core.Holder;
import net.minecraft.world.level.chunk.Palette;
import net.minecraft.util.BitStorage;
import net.minecraft.world.level.material.FluidState;
import com.destroystokyo.paper.util.maplist.IBlockDataList;
import java.util.concurrent.CompletableFuture;
import com.google.common.collect.Sets;
import java.util.function.IntConsumer;
import com.artillexstudios.axapi.utils.FastFieldAccessor;

public class ParallelBlockSetterImpl implements ParallelBlockSetter {
    private static final ExecutorService executor = Executors.newSingleThreadExecutor();
    private static final ExecutorService parallelExecutor = Executors.newFixedThreadPool(8);
    private static final Logger log = LoggerFactory.getLogger(ParallelBlockSetterImpl.class);
    private final ServerLevel level;
    private final ArrayList<ChunkPos> chunks = new ArrayList<>();
    private static Unsafe unsafe;
    private static FastFieldAccessor nonEmptyBlockCount;
    private static FastFieldAccessor tickingBlockCount;
    private static FastFieldAccessor tickingFluidCount;
    private static FastFieldAccessor states;
    private static FastFieldAccessor biomes;
    private static FastFieldAccessor tickingList;
    private static FastFieldAccessor specialCollidingBlocks;
    private static FastFieldAccessor fluidStateCount;
    private static FastFieldAccessor paletteStrategy;
    private static FastFieldAccessor paletteData;
    private static Field palette;
    private static Field storage;

    static {
        try {
            Field f = Unsafe.class.getDeclaredField("theUnsafe");
            f.setAccessible(true);
            unsafe = (Unsafe) f.get(null);

            Field nonEmptyBlockCount = LevelChunkSection.class.getDeclaredField("e");
            Field tickingBlockCount = LevelChunkSection.class.getDeclaredField("f");
            Field tickingFluidCount = LevelChunkSection.class.getDeclaredField("g");
            Field states = LevelChunkSection.class.getDeclaredField("h");
            Field biomes = LevelChunkSection.class.getDeclaredField("biomes");
            Field tickingList = LevelChunkSection.class.getDeclaredField("tickingList");
            Field specialCollidingBlocks = LevelChunkSection.class.getDeclaredField("specialCollidingBlocks");
            Field fluidStateCount = LevelChunkSection.class.getDeclaredField("fluidStateCount");
            Field paletteData = PalettedContainer.class.getDeclaredField("d");
            Field paletteStrategy = PalettedContainer.class.getDeclaredField("e");
            Class<?> clazz = Class.forName("net.minecraft.world.level.chunk.DataPaletteBlock$c");
            palette = clazz.getDeclaredField("c");
            storage = clazz.getDeclaredField("b");

            nonEmptyBlockCount.setAccessible(true);
            tickingBlockCount.setAccessible(true);
            tickingFluidCount.setAccessible(true);
            states.setAccessible(true);
            biomes.setAccessible(true);
            tickingList.setAccessible(true);
            specialCollidingBlocks.setAccessible(true);
            fluidStateCount.setAccessible(true);
            paletteData.setAccessible(true);
            paletteStrategy.setAccessible(true);
            palette.setAccessible(true);
            storage.setAccessible(true);

            ParallelBlockSetterImpl.nonEmptyBlockCount = new FastFieldAccessor(nonEmptyBlockCount);
            ParallelBlockSetterImpl.tickingBlockCount = new FastFieldAccessor(tickingBlockCount);
            ParallelBlockSetterImpl.tickingFluidCount = new FastFieldAccessor(tickingFluidCount);
            ParallelBlockSetterImpl.states = new FastFieldAccessor(states);
            ParallelBlockSetterImpl.biomes = new FastFieldAccessor(biomes);
            ParallelBlockSetterImpl.tickingList = new FastFieldAccessor(tickingList);
            ParallelBlockSetterImpl.specialCollidingBlocks = new FastFieldAccessor(specialCollidingBlocks);
            ParallelBlockSetterImpl.fluidStateCount = new FastFieldAccessor(fluidStateCount);
            ParallelBlockSetterImpl.paletteData = new FastFieldAccessor(paletteData);
            ParallelBlockSetterImpl.paletteStrategy = new FastFieldAccessor(paletteStrategy);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public BlockState setBlockState(PalettedContainer.Strategy strategy, Palette<BlockState> palette, BitStorage bitStorage, LevelChunkSection section, int x, int y, int z, BlockState state, boolean lock) {  // Paper - state -> new state
        BlockState iblockdata1 = getAndSet(strategy, palette, bitStorage, x, y, z, state);

        FluidState fluid = iblockdata1.getFluidState();
        FluidState fluid1 = state.getFluidState();

        if (!iblockdata1.isAir()) {
            nonEmptyBlockCount.setShort(section, (short) (nonEmptyBlockCount.getShort(section) - 1));
            if (iblockdata1.isRandomlyTicking()) {
                tickingBlockCount.setShort(section, (short) (tickingBlockCount.getShort(section) - 1));

                ((IBlockDataList) ParallelBlockSetterImpl.tickingList.get(section)).remove(x, y, z);
            }
        }

        if (!fluid.isEmpty()) {
            tickingFluidCount.setShort(section, (short) (tickingFluidCount.getShort(section) - 1));
        }

        if (!state.isAir()) {
            nonEmptyBlockCount.setShort(section, (short) (nonEmptyBlockCount.getShort(section) + 1));
            if (state.isRandomlyTicking()) {
                tickingBlockCount.setShort(section, (short) (tickingBlockCount.getShort(section) + 1));
                // Paper start
                ((IBlockDataList) ParallelBlockSetterImpl.tickingList.get(section)).add(x, y, z, state);
                // Paper end
            }
        }

        if (!fluid1.isEmpty()) {
            tickingFluidCount.setShort(section, (short) (tickingFluidCount.getShort(section) + 1));
        }


        if (io.papermc.paper.util.CollisionUtil.isSpecialCollidingBlock(state)) {
            specialCollidingBlocks.setInt(section, specialCollidingBlocks.getInt(section) + 1);
        }
        if (io.papermc.paper.util.CollisionUtil.isSpecialCollidingBlock(iblockdata1)) {
            specialCollidingBlocks.setInt(section, specialCollidingBlocks.getInt(section) - 1);
        }
        return iblockdata1;
    }

    private <T> T getAndSet(PalettedContainer.Strategy strategy, Palette<T> palette, BitStorage bitStorage, int x, int y, int z, T value) {
        return getAndSet(palette, bitStorage, strategy.getIndex(x, y, z), value);
    }

    private <T> T getAndSet(Palette<T> palette, BitStorage storage, int index, T value) {
        int i = palette.idFor(value);
        int j = storage.getAndSet(index, i);
        return palette.valueFor(j);
    }

    public LevelChunkSection copy(LevelChunkSection section) {
        try {
            LevelChunkSection newSection = (LevelChunkSection) unsafe.allocateInstance(LevelChunkSection.class);
            short nonEmpty = nonEmptyBlockCount.getShort(section);
            short tickingBlock = tickingBlockCount.getShort(section);
            short tickingFluid = tickingFluidCount.getShort(section);
            short fluidStateCount = ParallelBlockSetterImpl.fluidStateCount.getShort(section);
            PalettedContainer<BlockState> statesContainer = states.get(section);
            PalettedContainer<Holder<Biome>> biomesContainer = biomes.get(section);
            com.destroystokyo.paper.util.maplist.IBlockDataList tickingList = ParallelBlockSetterImpl.tickingList.get(section);
            int specialColliding = specialCollidingBlocks.getInt(section);

            nonEmptyBlockCount.setShort(newSection, nonEmpty);
            tickingBlockCount.setShort(newSection, tickingBlock);
            tickingFluidCount.setShort(newSection, tickingFluid);
            ParallelBlockSetterImpl.fluidStateCount.setShort(newSection, fluidStateCount);
            states.set(newSection, statesContainer);
            biomes.set(newSection, biomesContainer);
            ParallelBlockSetterImpl.tickingList.set(newSection, tickingList);
            specialCollidingBlocks.setInt(newSection, specialColliding);

            return newSection;
        } catch (Exception exception) {
            log.error("An unexpected issue occurred while initializing ParallelBlockSetter. Is your version supported?", exception);
            throw new RuntimeException(exception);
        }
    }

    public ParallelBlockSetterImpl(World world) {
        this.level = ((CraftWorld) world).getHandle();
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

                        synchronized (levelChunk) {
                            levelChunk.getSections()[sectionIndex] = newSection;
                        }
                    }, parallelExecutor);

                    chunkFutures.add(future);
                }

                CompletableFuture<?> thisChunk = CompletableFuture.allOf(chunkFutures.toArray(new CompletableFuture[0]));
                
                thisChunk.thenAccept((ignored) -> {
                    MinecraftServer.getServer().executeBlocking(() -> {
                        var lightEngine = level.chunkSource.getLightEngine();
                        lightEngine.relight(Sets.newHashSet(levelChunk.getPos()),c -> {
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
