package com.artillexstudios.axapi.items;

import com.artillexstudios.axapi.packet.PacketEvent;
import com.artillexstudios.axapi.packet.PacketEvents;
import com.artillexstudios.axapi.packet.PacketListener;
import com.artillexstudios.axapi.packet.PacketTypes;
import com.artillexstudios.axapi.packet.wrapper.ClientboundContainerSetSlotWrapper;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.bukkit.entity.Player;

public class PacketItemModifier {
    private static boolean listening = false;
    private static final ObjectArrayList<PacketItemModifierListener> listeners = new ObjectArrayList<>();

    public static void registerModifierListener(PacketItemModifierListener listener) {
        if (!listening) {
            PacketEvents.INSTANCE.addListener(new PacketListener() {
                @Override
                public void onPacketSending(PacketEvent event) {
                    if (!PacketItemModifier.isListening()) {
                        return;
                    }

                    if (event.type() == PacketTypes.CONTAINER_SET_SLOT) {
                        try (ClientboundContainerSetSlotWrapper wrapper = new ClientboundContainerSetSlotWrapper(event)) {
                            PacketItemModifier.callModify(wrapper.stack(), event.player(), PacketItemModifier.Context.SET_SLOT);
                        }
                    }
                }
            });
        }

        if (!listeners.contains(listener)) {
            listeners.add(listener);
            listening = true;
        }
    }

    public static void unregister(PacketItemModifierListener listener) {
        listeners.remove(listener);
        if (listeners.isEmpty()) {
            listening = false;
        }
    }

    public static boolean isListening() {
        return listening;
    }

    public static void callModify(WrappedItemStack itemStack, Player player, Context context) {
        for (PacketItemModifierListener listener : listeners) {
            listener.modifyItemStack(player, itemStack, context);
        }
    }

    public static void restore(WrappedItemStack itemStack) {
        for (PacketItemModifierListener listener : listeners) {
            listener.restore(itemStack);
        }
    }

    public enum Context {
        SET_SLOT,
        SET_CONTENTS,
        EQUIPMENT,
        DROPPED_ITEM,
        MERCHANT_OFFER
    }
}
