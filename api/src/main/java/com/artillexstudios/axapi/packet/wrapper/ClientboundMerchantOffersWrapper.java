package com.artillexstudios.axapi.packet.wrapper;

import com.artillexstudios.axapi.packet.FriendlyByteBuf;
import com.artillexstudios.axapi.packet.PacketEvent;
import com.artillexstudios.axapi.packet.accessor.MerchantOffer;
import com.artillexstudios.axapi.utils.Version;

import java.util.ArrayList;
import java.util.List;

public final class ClientboundMerchantOffersWrapper extends PacketWrapper {
    private int containerId;
    private List<MerchantOffer> merchantOffers;
    private int villagerLevel;
    private int villagerXp;
    private boolean showProgress;
    private boolean canRestock;

    public ClientboundMerchantOffersWrapper(PacketEvent event) {
        super(event);
    }

    @Override
    public void write(FriendlyByteBuf out) {

    }

    @Override
    public void read(FriendlyByteBuf buf) {
        this.containerId = buf.readContainerId();
        if (Version.getServerVersion().isNewerThanOrEqualTo(Version.v1_19)) {
            this.merchantOffers = null;
        } else {
            int size = buf.readByte() & 0xFF;
            this.merchantOffers = new ArrayList<>(size);
            for (int i = 0; i < size; i++) {
                this.merchantOffers.add(null);
            }
        }

        this.villagerLevel = buf.readVarInt();
        this.villagerLevel = buf.readVarInt();
        this.showProgress = buf.readBoolean();
        this.canRestock = buf.readBoolean();
    }
}
