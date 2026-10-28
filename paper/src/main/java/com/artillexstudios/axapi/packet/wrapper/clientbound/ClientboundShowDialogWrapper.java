package com.artillexstudios.axapi.packet.wrapper.clientbound;

import com.artillexstudios.axapi.dialog.Dialog;
import com.artillexstudios.axapi.items.nbt.CompoundTag;
import com.artillexstudios.axapi.nms.NMSHandlers;
import com.artillexstudios.axapi.packet.ClientboundPacketTypes;
import com.artillexstudios.axapi.packet.FriendlyByteBuf;
import com.artillexstudios.axapi.packet.PacketType;
import com.artillexstudios.axapi.packet.wrapper.PacketWrapper;

/**
 * This wrapper only allows writing dialog packets, not reading!
 */
public final class ClientboundShowDialogWrapper extends PacketWrapper {
    private Dialog dialog;

    public ClientboundShowDialogWrapper(Dialog dialog) {
        this.dialog = dialog;
    }

    @Override
    public void write(FriendlyByteBuf out) {
        out.writeVarInt(0); // direct holder
        CompoundTag tag = NMSHandlers.getNmsHandler().newTag();
        tag.putString("type", this.dialog.type());
        this.dialog.encode(tag);
        out.writeNBT(tag);
    }

    public Dialog getDialog() {
        return this.dialog;
    }

    @Override
    public void read(FriendlyByteBuf buf) {
        throw new UnsupportedOperationException();
    }

    @Override
    public PacketType packetType() {
        return ClientboundPacketTypes.SHOW_DIALOG;
    }
}
