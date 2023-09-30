package com.artillexstudios.axapi.nms.v1_19_R3;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtIo;
import org.bukkit.craftbukkit.v1_19_R3.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class ItemStackSerializer {

    public byte[] serializeAsBytes(ItemStack itemStack) {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream(); ObjectOutputStream stream = new ObjectOutputStream(outputStream)) {
            NbtIo.writeCompressed(CraftItemStack.asNMSCopy(itemStack).save(new CompoundTag()), stream);
            return outputStream.toByteArray();
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    public ItemStack deserializeFromBytes(byte[] bytes) {
        try (ByteArrayInputStream stream = new ByteArrayInputStream(bytes); ObjectInputStream inputStream = new ObjectInputStream(stream)) {
            return CraftItemStack.asBukkitCopy(net.minecraft.world.item.ItemStack.of(NbtIo.read(inputStream)));
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }
}
