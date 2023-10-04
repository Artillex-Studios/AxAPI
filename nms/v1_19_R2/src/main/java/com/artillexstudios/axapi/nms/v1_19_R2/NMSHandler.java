package com.artillexstudios.axapi.nms.v1_19_R2;

import com.artillexstudios.axapi.nms.v1_19_R2.packet.PacketListener;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import net.minecraft.server.level.ServerPlayer;
import org.bukkit.craftbukkit.v1_19_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Method;
import java.util.UUID;

public class NMSHandler implements com.artillexstudios.axapi.nms.NMSHandler {
    private final ItemStackSerializer serializer = new ItemStackSerializer();
    private static final String PACKET_HANDLER = "packet_handler";
    private static final String AXAPI_HANDLER = "axapi_handler";
    private Method skullMetaMethod;

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

        var channel = serverPlayer.connection.connection.channel;

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

        var channel = serverPlayer.connection.connection.channel;

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
    public void setItemStackTexture(ItemStack item, String texture) {
        if (item.getItemMeta() instanceof SkullMeta skullMeta) {
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

            item.setItemMeta(skullMeta);
        }
    }
}