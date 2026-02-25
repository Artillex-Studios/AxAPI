package com.artillexstudios.axapi.packet;

import com.artillexstudios.axapi.packet.exception.PacketReadingException;
import com.artillexstudios.axapi.packet.wrapper.PacketWrapper;
import com.artillexstudios.axapi.utils.featureflags.FeatureFlags;
import com.artillexstudios.axapi.utils.featureflags.exception.IllegalFeatureFlagStateException;
import com.artillexstudios.axapi.utils.logging.LogUtils;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;

public enum PacketEvents {
    INSTANCE;

    private final ObjectArrayList<PacketListener> listeners = new ObjectArrayList<>();
    private PacketListener[] baked = new PacketListener[0];
    private boolean listening = false;

    public void addListener(PacketListener listener) {
        if (!FeatureFlags.ENABLE_PACKET_LISTENERS.get()) {
            throw new IllegalFeatureFlagStateException("Packet listeners!");
        }

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
            try {
                if (event.side() == PacketSide.CLIENT_BOUND) {
                    listener.onPacketSending(event);
                } else {
                    listener.onPacketReceive(event);
                }
            } catch (PacketReadingException exception) {
                if (FeatureFlags.DEBUG.get()) {
                    LogUtils.info("An issue occurred while reading packet! Packet side: {}", event.side(), exception);
                }
                // The packet couldn't be read for some reason, we can just stop execution here, as no other listener will be able to
                // read the packet either way.
                return;
            } catch (Throwable throwable) {
                LogUtils.error("Failed to read {} packet!", event.side(), throwable);
                continue;
            }

            PacketWrapper wrapper = event.wrapper();
            if (wrapper != null) {
                FriendlyByteBuf out = event.out();
                wrapper.write(out);
            }
        }
    }


    public boolean listening() {
        return this.listening;
    }
}
