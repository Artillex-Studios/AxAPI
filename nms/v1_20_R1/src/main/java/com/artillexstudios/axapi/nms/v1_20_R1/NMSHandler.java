package com.artillexstudios.axapi.nms.v1_20_R1;

import com.artillexstudios.axapi.nms.v1_20_R1.packet.PacketListener;
import org.bukkit.craftbukkit.v1_20_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class NMSHandler implements com.artillexstudios.axapi.nms.NMSHandler {
    private final ItemStackSerializer serializer = new ItemStackSerializer();
    private static final String PACKET_HANDLER = "packet_handler";
    private static final String AXAPI_HANDLER = "axapi_handler";

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
}
