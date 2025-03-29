package com.artillexstudios.axapi.packet.wrapper.clientbound;

import com.artillexstudios.axapi.items.WrappedItemStack;
import com.artillexstudios.axapi.packet.ClientboundPacketTypes;
import com.artillexstudios.axapi.packet.FriendlyByteBuf;
import com.artillexstudios.axapi.packet.PacketEvent;
import com.artillexstudios.axapi.packet.PacketType;
import com.artillexstudios.axapi.packet.wrapper.PacketWrapper;
import com.artillexstudios.axapi.utils.MerchantOffer;
import com.artillexstudios.axapi.utils.Version;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    public int containerId() {
        return this.containerId;
    }

    public void setContainerId(int containerId) {
        this.containerId = containerId;
    }

    public void addMerchantOffer(MerchantOffer offer) {
        this.merchantOffers.add(offer);
    }

    public List<MerchantOffer> merchantOffers() {
        return this.merchantOffers;
    }

    public int villagerLevel() {
        return this.villagerLevel;
    }

    public void setVillagerLevel(int villagerLevel) {
        this.villagerLevel = villagerLevel;
    }

    public int villagerXp() {
        return villagerXp;
    }

    public void setVillagerXp(int villagerXp) {
        this.villagerXp = villagerXp;
    }

    public boolean showProgress() {
        return this.showProgress;
    }

    public void setShowProgress(boolean showProgress) {
        this.showProgress = showProgress;
    }

    public boolean canRestock() {
        return this.canRestock;
    }

    public void setCanRestock(boolean canRestock) {
        this.canRestock = canRestock;
    }

    @Override
    public void write(FriendlyByteBuf out) {
        out.writeContainerId(this.containerId);
        if (Version.getServerVersion().isNewerThanOrEqualTo(Version.v1_19)) {
            out.writeVarInt(this.merchantOffers.size());
            for (MerchantOffer merchantOffer : this.merchantOffers) {
                writeOffer(out, merchantOffer);
            }
        } else {
            out.writeByte(this.merchantOffers.size() & 0xFF);
            for (MerchantOffer merchantOffer : this.merchantOffers) {
                writeOffer(out, merchantOffer);
            }
        }
        out.writeVarInt(this.villagerLevel);
        out.writeVarInt(this.villagerXp);
        out.writeBoolean(this.showProgress);
        out.writeBoolean(this.canRestock);
    }

    @Override
    public void read(FriendlyByteBuf buf) {
        this.containerId = buf.readContainerId();
        if (Version.getServerVersion().isNewerThanOrEqualTo(Version.v1_19)) {
            int size = buf.readVarInt();
            for (int i = 0; i < size; i++) {
                this.merchantOffers.add(this.readOffer(buf));
            }
        } else {
            int size = buf.readByte() & 0xFF;
            this.merchantOffers = new ArrayList<>(size);
            for (int i = 0; i < size; i++) {
                this.merchantOffers.add(this.readOffer(buf));
            }
        }

        this.villagerLevel = buf.readVarInt();
        this.villagerXp = buf.readVarInt();
        this.showProgress = buf.readBoolean();
        this.canRestock = buf.readBoolean();
    }

    @Override
    public PacketType packetType() {
        return ClientboundPacketTypes.MERCHANT_OFFERS;
    }

    private MerchantOffer readOffer(FriendlyByteBuf buf) {
        WrappedItemStack stack = Version.getServerVersion().isOlderThan(Version.v1_20_4) ? buf.readItemStack() : buf.readItemCost();
        WrappedItemStack soldItem = buf.readItemStack();
        Optional<WrappedItemStack> secondStack = Version.getServerVersion().isNewerThanOrEqualTo(Version.v1_20_4) || Version.getServerVersion().isOlderThan(Version.v1_19) ? buf.readOptionalItemCost() : Optional.ofNullable(buf.readItemStack());
        boolean tradeDisabled = buf.readBoolean();
        int uses = buf.readInt();
        int maxUses = buf.readInt();
        int xp = buf.readInt();
        int specialPrice = buf.readInt();
        float priceMultiplier = buf.readFloat();
        int demand = buf.readInt();

        return new MerchantOffer(stack, secondStack, soldItem, tradeDisabled ? maxUses : uses, maxUses, xp, specialPrice, priceMultiplier, demand);
    }

    private void writeOffer(FriendlyByteBuf buf, MerchantOffer offer) {
        buf.writeItemCost(offer.item1());
        buf.writeItemStack(offer.output());
        if (Version.getServerVersion().isNewerThanOrEqualTo(Version.v1_20_4) || Version.v1_20_4.isOlderThan(Version.v1_19)) {
            buf.writeOptionalItemCost(offer.item2());
        } else {
            buf.writeItemStack(offer.item2().orElse(null));
        }
        buf.writeBoolean(offer.uses() >= offer.maxUses());
        buf.writeInt(offer.uses());
        buf.writeInt(offer.maxUses());
        buf.writeInt(offer.xp());
        buf.writeInt(offer.price());
        buf.writeFloat(offer.priceMultiplier());
        buf.writeInt(offer.demand());
    }
}
