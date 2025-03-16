package com.artillexstudios.axapi.nms.v1_21_R2.wrapper;

import com.artillexstudios.axapi.nms.v1_21_R2.packet.FriendlyByteBufWrapper;
import com.artillexstudios.axapi.packet.ClientboundPacketTypes;
import com.artillexstudios.axapi.packet.wrapper.PacketWrapper;
import com.artillexstudios.axapi.utils.PlayerTextures;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.GameProtocols;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

public final class ServerPlayerWrapper implements com.artillexstudios.axapi.nms.wrapper.ServerPlayerWrapper {
    private static final Function<ByteBuf, RegistryFriendlyByteBuf> decorator = RegistryFriendlyByteBuf.decorator(MinecraftServer.getServer().registryAccess());
    private static final StreamCodec<ByteBuf, Packet<? super ClientGamePacketListener>> codec = GameProtocols.CLIENTBOUND_TEMPLATE.bind(decorator).codec();
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
        return new PlayerTextures(value.value(), value.signature());
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
    public void sendPacket(PacketWrapper wrapper) {
        RegistryFriendlyByteBuf buf = decorator.apply(Unpooled.buffer());
        buf.writeVarInt(ClientboundPacketTypes.forPacketType(wrapper.packetType()));
        wrapper.write(new FriendlyByteBufWrapper(buf));
        this.serverPlayer.connection.send(codec.decode(buf));
        buf.release();
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
