package com.artillexstudios.axapi.packet;

import com.artillexstudios.axapi.packet.wrapper.PacketWrapper;
import com.artillexstudios.axapi.utils.logging.LogUtils;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;

public enum PacketEvents {
    INSTANCE;

    private final ObjectArrayList<PacketListener> listeners = new ObjectArrayList<>();
    private PacketListener[] baked = new PacketListener[0];
    private boolean listening = false;

    public void addListener(PacketListener listener) {
        this.listeners.add(listener);
        this.listening = true;
        this.baked = this.listeners.toArray(new PacketListener[0]);
    }

    public void callEvent(PacketEvent event) {
        if (!this.listening) {
            return;
        }

        PacketListener[] baked = this.baked;
        for (PacketListener listener : baked) {
            if (event.side() == PacketSide.CLIENT_BOUND) {
                try {
                    listener.onPacketSending(event);
                } catch (Throwable throwable) {
                    LogUtils.error("Failed to read clientbound packet!", throwable);
                    continue;
                }

                PacketWrapper wrapper = event.wrapper();
                if (wrapper != null) {
                    FriendlyByteBuf out = event.out();
                    wrapper.write(out);
                }
            } else {
                try {
                    listener.onPacketReceive(event);
                } catch (Throwable throwable) {
                    LogUtils.error("Failed to read serverbound packet!", throwable);
                    continue;
                }

                PacketWrapper wrapper = event.wrapper();
                if (wrapper != null) {
                    FriendlyByteBuf out = event.out();
                    wrapper.write(out);
                }
            }
        }
    }

    public boolean listening() {
        return this.listening;
    }
}
