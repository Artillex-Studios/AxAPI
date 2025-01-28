package com.artillexstudios.axapi.packet;

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
            }
        } else {
            for (int i = 0; i < bakedLength; i++) {
                baked[i].onPacketSending(event);
            }
        }
    }

    public boolean listening() {
        return this.listening;
    }
}
