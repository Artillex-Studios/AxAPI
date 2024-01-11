package com.artillexstudios.axapi.nms.v1_20_R1;

import com.artillexstudios.axapi.entity.PacketEntityTracker;
import com.artillexstudios.axapi.nms.v1_20_R1.entity.EntityTracker;
import com.artillexstudios.axapi.nms.v1_20_R1.packet.PacketListener;
import com.artillexstudios.axapi.selection.BlockSetter;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import io.netty.channel.Channel;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ServerGamePacketListener;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_20_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.inventory.meta.ItemMeta;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Locale;
import java.util.UUID;

public class NMSHandler implements com.artillexstudios.axapi.nms.NMSHandler {
    private final ItemStackSerializer serializer = new ItemStackSerializer();
    private static final String PACKET_HANDLER = "packet_handler";
    private final String AXAPI_HANDLER;
    private Method skullMetaMethod;
    private Field channelField;
    private Field connectionField;

    public NMSHandler(JavaPlugin plugin) {
        AXAPI_HANDLER = "axapi_handler_" + plugin.getName().toLowerCase(Locale.ENGLISH);

        try {
            connectionField = Class.forName("net.minecraft.server.network.PlayerConnection").getDeclaredField("h");
            connectionField.setAccessible(true);
            channelField = Class.forName("net.minecraft.network.NetworkManager").getDeclaredField("m");
            channelField.setAccessible(true);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public byte[] serializeItemStack(ItemStack itemStack) {
        return serializer.serializeAsBytes(itemStack);
    }

    @Override
    public ItemStack deserializeItemStack(byte[] bytes) {
        return serializer.deserializeFromBytes(bytes);
    }

    @Override
    public void injectPlayer(Player player) {
        var serverPlayer = ((CraftPlayer) player).getHandle();

        var channel = getChannel(getConnection(serverPlayer.connection));

        if (!channel.pipeline().names().contains(PACKET_HANDLER)) {
            return;
        }

        if (channel.pipeline().names().contains(AXAPI_HANDLER)) {
            return;
        }

        channel.eventLoop().submit(() -> {
            channel.pipeline().addBefore(PACKET_HANDLER, AXAPI_HANDLER, new PacketListener(player));
        });
    }

    @Override
    public void uninjectPlayer(Player player) {
        var serverPlayer = ((CraftPlayer) player).getHandle();

        var channel = getChannel(getConnection(serverPlayer.connection));

        channel.eventLoop().submit(() -> {
            if (channel.pipeline().get(AXAPI_HANDLER) != null) {
                channel.pipeline().remove(AXAPI_HANDLER);
            }
        });
    }

    @Override
    public int getProtocolVersionId(Player player) {
        ServerPlayer serverPlayer = ((CraftPlayer) player).getHandle();
        return serverPlayer.connection.connection.protocolVersion;
    }

    @Override
    public void setItemStackTexture(ItemMeta meta, String texture) {
        if (meta instanceof SkullMeta skullMeta) {
            if (skullMetaMethod == null) {
                try {
                    skullMetaMethod = skullMeta.getClass().getDeclaredMethod("setProfile", GameProfile.class);
                    skullMetaMethod.setAccessible(true);
                } catch (NoSuchMethodException e) {
                    throw new RuntimeException(e);
                }
            }

            GameProfile profile = new GameProfile(UUID.randomUUID(), "skull");
            profile.getProperties().put("textures", new Property("textures", texture));

            try {
                skullMetaMethod.invoke(skullMeta, profile);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }

    @Override
    public PacketEntityTracker newTracker() {
        return new EntityTracker();
    }

    @Override
    public BlockSetter newSetter(World world) {
        return new BlockSetterImpl(world);
    }

    private Channel getChannel(Connection connection) {
        try {
            return (Channel) channelField.get(connection);
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    private Connection getConnection(ServerGamePacketListenerImpl serverGamePacketListener) {
        try {
            return (Connection) connectionField.get(serverGamePacketListener);
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }
}
