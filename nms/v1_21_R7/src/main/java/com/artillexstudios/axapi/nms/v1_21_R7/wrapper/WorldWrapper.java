package com.artillexstudios.axapi.nms.v1_21_R7.wrapper;

import com.artillexstudios.axapi.nms.v1_21_R7.BlockSetterImpl;
import com.artillexstudios.axapi.nms.v1_21_R7.ParallelBlockSetterImpl;
import com.artillexstudios.axapi.reflection.FieldAccessor;
import com.artillexstudios.axapi.selection.BlockSetter;
import com.artillexstudios.axapi.selection.ParallelBlockSetter;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.component.ResolvableProfile;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.BlockState;
import org.bukkit.block.Skull;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.block.CraftSkull;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public final class WorldWrapper implements com.artillexstudios.axapi.nms.wrapper.WorldWrapper {
    private World wrapped;
    private ServerLevel level;

    public WorldWrapper(World world) {
        this.wrapped = world;
    }

    public WorldWrapper(ServerLevel level) {
        this.level = level;
    }

    @Override
    public BlockSetter setter() {
        this.update();
        return new BlockSetterImpl(this.level);
    }

    @Override
    public ParallelBlockSetter parallelSetter() {
        this.update();
        return new ParallelBlockSetterImpl(this.level);
    }

    @Override
    public List<com.artillexstudios.axapi.nms.wrapper.ServerPlayerWrapper> players() {
        this.update();

        List<ServerPlayer> players = this.level.players();
        List<com.artillexstudios.axapi.nms.wrapper.ServerPlayerWrapper> playerList = new ObjectArrayList<>(players.size());
        if (players instanceof ArrayList<ServerPlayer> arrayList) {
            for (int i = 0; i < players.size(); i++) {
                ServerPlayer serverPlayer = arrayList.get(i);
                if (serverPlayer == null) {
                    continue;
                }

                playerList.add(new ServerPlayerWrapper(serverPlayer));
            }
        } else {
            for (ServerPlayer serverPlayer : players.toArray(new ServerPlayer[0])) {
                playerList.add(new ServerPlayerWrapper(serverPlayer));
            }
        }

        return playerList;
    }

    @Override
    public void setBlockOwner(Location location, String texture) {
        BlockState state = location.getBlock().getState();
        if (!(state instanceof Skull skull)) {
            return;
        }

        CraftSkull craftSkull = (CraftSkull) skull;
        FieldAccessor accessor = FieldAccessor.builder()
                .withClass(CraftSkull.class)
                .withField("profile")
                .build();

        GameProfile gameProfile = new GameProfile(UUID.randomUUID(), "axapi");
        gameProfile.properties().put("textures", new Property("texutres", texture));
        ResolvableProfile resolvableProfile = ResolvableProfile.createResolved(gameProfile);
        accessor.set(craftSkull, resolvableProfile);
        state.update();
    }

    @Override
    public void update(boolean force) {
        if (this.level == null || force) {
            this.level = ((CraftWorld) this.wrapped).getHandle();
        }
    }

    @Override
    public World wrapped() {
        World wrapped = this.wrapped;
        if (wrapped == null) {
            wrapped = this.level.getWorld();
            this.wrapped = wrapped;
        }

        return wrapped;
    }

    @Override
    public ServerLevel asMinecraft() {
        this.update();
        return this.level;
    }
}
