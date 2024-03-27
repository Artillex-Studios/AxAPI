package com.artillexstudios.axapi.serializers.impl;

import com.artillexstudios.axapi.nms.NMSHandlers;
import com.artillexstudios.axapi.serializers.Serializer;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.io.ByteArrayOutputStream;

public class ItemArraySerializer implements Serializer<ItemStack[]> {
    private static final ItemStack AIR = new ItemStack(Material.AIR);

    @Override
    public String serialize(ItemStack[] value) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        ByteArrayDataOutput outputStream = ByteStreams.newDataOutput(stream);
        outputStream.writeInt(value.length);

        for (ItemStack item : value) {
            if (item == null || item.getType().isAir()) {
                outputStream.writeShort(0);
                continue;
            }

            byte[] serialized = NMSHandlers.getNmsHandler().serializeItemStack(item);
            outputStream.writeShort(serialized.length);
            outputStream.write(serialized);
        }

        return Base64Coder.encodeLines(outputStream.toByteArray());
    }

    @Override
    public ItemStack[] deserialize(String value) {
        ByteArrayDataInput input = ByteStreams.newDataInput(Base64Coder.decodeLines(value));
        int length = input.readInt();
        ItemStack[] items = new ItemStack[length];

        for (int i = 0; i < length; i++) {
            short size = input.readShort();
            if (size > 0) {
                byte[] read = new byte[size];
                input.readFully(read);

                items[i] = NMSHandlers.getNmsHandler().deserializeItemStack(read);
            } else {
                items[i] = AIR.clone();
            }
        }

        return items;
    }
}
