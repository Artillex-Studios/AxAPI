package com.artillexstudios.axapi.nms.v1_18_R1;

import com.google.common.base.Preconditions;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtIo;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_18_R1.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

public class ItemStackSerializer {

    public byte[] serializeAsBytes(ItemStack itemStack) {
        Preconditions.checkArgument(itemStack != null, "Can't serialise a null itemstack!");
        Preconditions.checkArgument(itemStack.getType() != Material.AIR, "Can't serialise air!");
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            NbtIo.writeCompressed(CraftItemStack.asNMSCopy(itemStack).save(new CompoundTag()), outputStream);
            return outputStream.toByteArray();
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    public ItemStack deserializeFromBytes(byte[] bytes) {
        try (ByteArrayInputStream stream = new ByteArrayInputStream(bytes)) {
            return CraftItemStack.asBukkitCopy(net.minecraft.world.item.ItemStack.of(NbtIo.readCompressed(stream)));
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }
}
