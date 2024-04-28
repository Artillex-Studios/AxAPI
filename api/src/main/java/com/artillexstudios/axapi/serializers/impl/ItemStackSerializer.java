package com.artillexstudios.axapi.serializers.impl;

import com.artillexstudios.axapi.nms.NMSHandlers;
import com.artillexstudios.axapi.serializers.Serializer;
import org.bukkit.inventory.ItemStack;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

public class ItemStackSerializer implements Serializer<ItemStack, String> {

    @Override
    public String serialize(ItemStack object) {
        return Base64Coder.encodeLines(NMSHandlers.getNmsHandler().wrapItem(object).serialize());
    }

    @Override
    public ItemStack deserialize(String value) {
        return NMSHandlers.getNmsHandler().wrapItem(Base64Coder.decodeLines(value)).toBukkit();
    }
}
