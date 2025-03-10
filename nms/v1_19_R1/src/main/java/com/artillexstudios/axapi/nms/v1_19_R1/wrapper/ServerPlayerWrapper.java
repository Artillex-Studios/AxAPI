package com.artillexstudios.axapi.nms.v1_19_R1.wrapper;

import com.artillexstudios.axapi.utils.PlayerTextures;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import net.minecraft.server.level.ServerPlayer;
import org.bukkit.craftbukkit.v1_19_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.Objects;
import java.util.Optional;

public final class ServerPlayerWrapper implements com.artillexstudios.axapi.nms.wrapper.ServerPlayerWrapper {
    private Player wrapped;
    private ServerPlayer serverPlayer;

    public ServerPlayerWrapper(Player player) {
        this.wrapped = player;
    }

    public ServerPlayerWrapper(ServerPlayer player) {
        this.serverPlayer = player;
    }

    @Override
    public PlayerTextures textures() {
        this.update();
        GameProfile profile = this.serverPlayer.getGameProfile();
        Optional<Property> property = profile.getProperties()
                .get("textures")
                .stream()
                .findFirst();

        if (property.isEmpty()) {
            return new PlayerTextures(null, null);
        }

        Property value = property.get();
        return new PlayerTextures(value.getValue(), value.getSignature());
    }

    @Override
    public double getX() {
        this.update();
        return this.serverPlayer.getX();
    }

    @Override
    public double getZ() {
        this.update();
        return this.serverPlayer.getZ();
    }

    @Override
    public Player wrapped() {
        Player wrapped = this.wrapped;
        if (wrapped == null) {
            wrapped = this.serverPlayer.getBukkitEntity();
            this.wrapped = wrapped;
        }

        return wrapped;
    }

    @Override
    public void update(boolean force) {
        if (this.serverPlayer == null || force) {
            this.serverPlayer = ((CraftPlayer) this.wrapped).getHandle();
        }
    }

    @Override
    public Object asMinecraft() {
        this.update();
        return this.serverPlayer;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ServerPlayerWrapper that)) {
            return false;
        }

        if (that.serverPlayer != null && (that.serverPlayer == this.serverPlayer || Objects.equals(this.serverPlayer, that.serverPlayer))) {
            return true;
        }

        return Objects.equals(this.wrapped, that.wrapped);
    }

    @Override
    public int hashCode() {
        this.update();
        return Objects.hashCode(this.serverPlayer);
    }
}
