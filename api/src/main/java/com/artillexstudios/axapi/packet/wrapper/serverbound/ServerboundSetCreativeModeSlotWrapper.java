package com.artillexstudios.axapi.packet.wrapper.serverbound;

import com.artillexstudios.shared.axapi.packet.FriendlyByteBuf;
import com.artillexstudios.shared.axapi.packet.PacketEvent;
import com.artillexstudios.shared.axapi.packet.PacketType;
import com.artillexstudios.shared.axapi.packet.ServerboundPacketTypes;
import com.artillexstudios.shared.axapi.packet.wrapper.PacketWrapper;
import com.artillexstudios.shared.axapi.utils.Version;
import org.bukkit.inventory.ItemStack;

public class ServerboundSetCreativeModeSlotWrapper extends PacketWrapper {
    private int slot;
    private ItemStack stack;

    public ServerboundSetCreativeModeSlotWrapper(PacketEvent event) {
        super(event);
    }

    public int slot() {
        return this.slot;
    }

    public void slot(int slot) {
        this.slot = slot;
    }

    public ItemStack stack() {
        return this.stack;
    }

    public void stack(ItemStack stack) {
        this.stack = stack;
    }

    @Override
    public void write(FriendlyByteBuf out) {
        out.writeShort(this.slot);
        out.writeItemStack(this.stack);
    }

    @Override
    public void read(FriendlyByteBuf buf) {
        this.slot = Version.getServerVersion().isNewerThanOrEqualTo(Version.v1_20_4) ? buf.readUnsignedShort() : buf.readShort();
        this.stack = buf.readItemStack();
    }

    @Override
    public PacketType packetType() {
        return ServerboundPacketTypes.SET_CREATIVE_MODE_SLOT;
    }
}
