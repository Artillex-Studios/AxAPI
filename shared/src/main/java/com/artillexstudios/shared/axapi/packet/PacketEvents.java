package com.artillexstudios.shared.axapi.packet;

import com.artillexstudios.shared.axapi.packet.wrapper.PacketWrapper;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PacketEvents {
    private static final StackWalker stackWalker = StackWalker.getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE);
    private static final Logger log = LoggerFactory.getLogger(PacketEvents.class);
    private static final ObjectArrayList<PacketListener> listeners = new ObjectArrayList<>();
    private static PacketListener[] baked = new PacketListener[0];
    private static boolean listening = false;

    public static void addListener(PacketListener listener) {
        listeners.add(listener);
        listening = true;
        baked = listeners.toArray(new PacketListener[0]);
        log.error("REGISTERING LISTENER FROM {}! Listeners: {}", stackWalker.getCallerClass(), listeners);
    }

    public static void callEvent(PacketEvent event) {
        if (!listening) {
            return;
        }

        PacketListener[] baked = PacketEvents.baked;
        PacketWrapper lastWrapper = null;
        int bakedLength = baked.length;
        if (event.side() == PacketSide.SERVER_BOUND) {
            for (int i = 0; i < bakedLength; i++) {
                log.warn("Received! {}", i);
                baked[i].onPacketReceive(event);

                FriendlyByteBuf directIn = event.directIn();
                if (directIn != null) {
                    directIn.readerIndex(1);
                }
                PacketWrapper wrapper = event.wrapper();
                if (wrapper != null && lastWrapper != wrapper) {
                    FriendlyByteBuf out = event.out();
                    out.writerIndex(1);
                    wrapper.write(out);
                    lastWrapper = wrapper;
                }
            }
        } else {
            for (int i = 0; i < bakedLength; i++) {
                baked[i].onPacketSending(event);

                FriendlyByteBuf directIn = event.directIn();
                if (directIn != null) {
                    directIn.readerIndex(1);
                }
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

    public static boolean listening() {
        return listening;
    }
}
