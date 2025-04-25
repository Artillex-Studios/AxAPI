package com.artillexstudios.axapi.nms.v1_21_R3.wrapper;

import com.artillexstudios.axapi.nms.v1_21_R3.packet.PacketEncoder;
import com.artillexstudios.axapi.nms.v1_21_R3.packet.PacketTransformer;
import com.artillexstudios.axapi.packet.wrapper.PacketWrapper;
import com.artillexstudios.axapi.reflection.FieldAccessor;
import com.artillexstudios.axapi.utils.ComponentSerializer;
import com.artillexstudios.axapi.utils.PlayerTextures;
import com.artillexstudios.axapi.utils.logging.LogUtils;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import io.netty.channel.Channel;
import net.kyori.adventure.text.Component;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundSystemChatPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import org.bukkit.attribute.Attribute;
import org.bukkit.craftbukkit.attribute.CraftAttribute;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.Objects;
import java.util.Optional;

public final class ServerPlayerWrapper implements com.artillexstudios.axapi.nms.wrapper.ServerPlayerWrapper {
    private static final FieldAccessor connectionAccessor = FieldAccessor.builder()
            .withClass("net.minecraft.server.network.ServerCommonPacketListenerImpl")
            .withField("e")
            .build();
    private static final FieldAccessor channelAccessor = FieldAccessor.builder()
            .withClass("net.minecraft.network.NetworkManager")
            .withField("n")
            .build();
    private static final FieldAccessor attributeAccessor = FieldAccessor.builder()
            .withClass(AttributeMap.class)
            .withField("e")
            .build();
    private Player wrapped;
    private ServerPlayer serverPlayer;

    public ServerPlayerWrapper(Player player) {
        this.wrapped = player;
    }

    public ServerPlayerWrapper(ServerPlayer player) {
        this.serverPlayer = player;
    }

    @Override
    public void inject() {
        this.update();

        Connection connection = connectionAccessor.get(this.serverPlayer.connection, Connection.class);
        Channel channel = channelAccessor.get(connection, Channel.class);

        if (!channel.pipeline().names().contains(ServerPlayerWrapper.PACKET_HANDLER)) {
            return;
        }

        if (channel.pipeline().names().contains(ServerPlayerWrapper.AXAPI_HANDLER)) {
            return;
        }

        channel.eventLoop().submit(() -> {
            channel.pipeline().addBefore("encoder", ServerPlayerWrapper.AXAPI_HANDLER, new PacketEncoder(this));
        });
    }

    @Override
    public void uninject() {
        this.update();

        Connection connection = connectionAccessor.get(this.serverPlayer.connection, Connection.class);
        Channel channel = channelAccessor.get(connection, Channel.class);

        channel.eventLoop().submit(() -> {
            if (channel.pipeline().get(ServerPlayerWrapper.AXAPI_HANDLER) != null) {
                channel.pipeline().remove(ServerPlayerWrapper.AXAPI_HANDLER);
            }
        });
    }

    @Override
    public void sendPacket(Object packet) {
        this.update();

        if (packet instanceof PacketWrapper wrapper) {
            this.serverPlayer.connection.send(PacketTransformer.transformClientbound(wrapper));
            return;
        }

        if (!(packet instanceof Packet<?> p)) {
            LogUtils.warn("Failed to send unknown packet to player {}! Packet: {}", this.wrapped().getName(), packet);
            return;
        }

        this.serverPlayer.connection.send(p);
    }

    @Override
    public void message(Component message) {
        this.update();
        this.serverPlayer.connection.send(new ClientboundSystemChatPacket((net.minecraft.network.chat.Component) ComponentSerializer.instance().toVanilla(message), false));
    }

    @Override
    public double getBase(Attribute attribute) {
        this.update();

        AttributeMap map = this.serverPlayer.getAttributes();
        AttributeSupplier supplier = attributeAccessor.get(map, AttributeSupplier.class);
        return supplier.getBaseValue(CraftAttribute.bukkitToMinecraftHolder(attribute));
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
    public ServerPlayer asMinecraft() {
        this.update();
        return this.serverPlayer;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ServerPlayerWrapper that)) {
            return false;
        }

        this.update();
        that.update();
        if (Objects.equals(this.serverPlayer, that.serverPlayer)) {
            return true;
        }

        return this.wrapped().getUniqueId().equals(that.wrapped().getUniqueId());
    }

    @Override
    public int hashCode() {
        this.update();
        return Objects.hashCode(this.serverPlayer);
    }
}
