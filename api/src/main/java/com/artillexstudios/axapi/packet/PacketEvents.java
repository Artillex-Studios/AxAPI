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
        PacketWrapper lastWrapper = null;
        int bakedLength = baked.length;
        if (event.side() == PacketSide.SERVER_BOUND) {
            FriendlyByteBuf in = event.in();
            for (int i = 0; i < bakedLength; i++) {
                LogUtils.info("SERVERBOUND: Input buffer: {}, readerIndex: {}, writerIndex: {}", in, in.readerIndex(), in.writerIndex());
                try {
                    baked[i].onPacketReceive(event);
                } catch (Throwable throwable) {
                    LogUtils.error("Exception while running packet event! Buffer: {}", in, throwable);
                }

                FriendlyByteBuf directIn = in = event.in();
                LogUtils.info("SERVERBOUND: Input after event: {}, readerIndex: {}, writerIndex: {}", in, in.readerIndex(), in.writerIndex());
//                if (directIn != null) {
//                    directIn.readerIndex(1);
//                }
                PacketWrapper wrapper = event.wrapper();
                if (wrapper != null && lastWrapper != wrapper) {
                    FriendlyByteBuf out = event.out();
                    out.writerIndex(1);
                    wrapper.write(out);
                    lastWrapper = wrapper;
                }
            }
        } else {
            FriendlyByteBuf in = event.in();
            for (int i = 0; i < bakedLength; i++) {
                LogUtils.info("CLIENTBOUND: Input buffer: {}, readerIndex: {}, writerIndex: {}", in, in.readerIndex(), in.writerIndex());
                try {
                    baked[i].onPacketSending(event);
                } catch (Throwable throwable) {
                    LogUtils.error("Exception while running packet event! Buffer: {}", in, throwable);
                }

                FriendlyByteBuf directIn = in = event.in();
                LogUtils.info("CLIENTBOUND: Input after event: {}, readerIndex: {}, writerIndex: {}", in, in.readerIndex(), in.writerIndex());
//                FriendlyByteBuf directIn = event.directIn();
//                if (directIn != null) {
//                    directIn.readerIndex(1);
//                }
                PacketWrapper wrapper = event.wrapper();
                if (wrapper != null && lastWrapper != wrapper) {
                    FriendlyByteBuf out = event.out();
                    out.writerIndex(1);
                    wrapper.write(out);
                    lastWrapper = wrapper;
                }
            }
        }
    }

    public boolean listening() {
        return this.listening;
    }
}
