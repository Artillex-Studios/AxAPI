package com.artillexstudios.axapi.packet;

import com.artillexstudios.axapi.packet.wrapper.PacketWrapper;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;

public enum PacketEvents {
    INSTANCE;
    private ObjectArrayList<PacketListener> listeners = new ObjectArrayList<>();
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

        PacketListener[] baked =  this.baked;
        int bakedLength = baked.length;
        if (event.side() == PacketSide.SERVER_BOUND) {
            for (int i = 0; i < bakedLength; i++) {
                baked[i].onPacketReceive(event);

                PacketWrapper wrapper = event.wrapper();
                if (wrapper != null) {
                    wrapper.write();
                }
            }
        } else {
            for (int i = 0; i < bakedLength; i++) {
                baked[i].onPacketSending(event);

                PacketWrapper wrapper = event.wrapper();
                if (wrapper != null) {
                    // TOOO: Reset writerindex
//                    event.directOut().clear();
                    wrapper.write();
                }
            }
        }
    }

    public boolean listening() {
        return this.listening;
    }
}
