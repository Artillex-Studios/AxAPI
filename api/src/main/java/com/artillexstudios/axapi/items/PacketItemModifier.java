package com.artillexstudios.axapi.items;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.bukkit.entity.Player;

public class PacketItemModifier {
    private static boolean listening = false;
    private static final ObjectArrayList<PacketItemModifierListener> listeners = new ObjectArrayList<>();

    public static void registerModifierListener(PacketItemModifierListener listener) {
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

    public static void callModify(WrappedItemStack itemStack, Player player) {
        for (PacketItemModifierListener listener : listeners) {
            listener.modifyItemStack(player, itemStack);
        }
    }
}
