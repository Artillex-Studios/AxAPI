package com.artillexstudios.axapi.dialog;

import com.artillexstudios.axapi.items.nbt.CompoundTag;
import com.artillexstudios.axapi.nms.NMSHandlers;
import com.artillexstudios.axapi.nms.wrapper.ServerPlayerWrapper;
import com.artillexstudios.axapi.packet.wrapper.clientbound.ClientboundShowDialogWrapper;
import com.artillexstudios.axapi.utils.logging.LogUtils;
import org.bukkit.entity.Player;

public interface Dialog {

    String type();

    void encode(CompoundTag tag);

    default void open(Player player) {
        ClientboundShowDialogWrapper wrapper = new ClientboundShowDialogWrapper(this);
        CompoundTag tag = NMSHandlers.getNmsHandler().newTag();
        this.encode(tag);
        LogUtils.debug("Tag: {}", tag);
        ServerPlayerWrapper.wrap(player).sendPacket(wrapper);
    }
}
