package com.artillexstudios.axapi.nms.v1_20_R3;

import com.google.common.base.Preconditions;
import com.mojang.serialization.Dynamic;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtAccounter;
import net.minecraft.nbt.NbtIo;
import net.minecraft.nbt.NbtOps;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.datafix.fixes.References;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_20_R3.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_20_R3.util.CraftMagicNumbers;
import org.bukkit.inventory.ItemStack;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

public class ItemStackSerializer {

    public byte[] serializeAsBytes(ItemStack itemStack) {
        Preconditions.checkArgument(itemStack != null, "Can't serialise a null itemstack!");
        Preconditions.checkArgument(itemStack.getType() != Material.AIR, "Can't serialise air!");
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            CompoundTag compoundTag = CraftItemStack.asNMSCopy(itemStack).save(new CompoundTag());
            compoundTag.putInt("DataVersion", CraftMagicNumbers.INSTANCE.getDataVersion());
            NbtIo.writeCompressed(compoundTag, outputStream);
            return outputStream.toByteArray();
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    public ItemStack deserializeFromBytes(byte[] bytes) {
        try (ByteArrayInputStream stream = new ByteArrayInputStream(bytes)) {
            CompoundTag compound = NbtIo.readCompressed(stream, NbtAccounter.unlimitedHeap());
            int dataVersion = compound.getInt("DataVersion");
            CompoundTag converted = (CompoundTag) MinecraftServer.getServer().fixerUpper.update(References.ITEM_STACK, new Dynamic<>(NbtOps.INSTANCE, compound), dataVersion, CraftMagicNumbers.INSTANCE.getDataVersion()).getValue();
            return CraftItemStack.asCraftMirror(net.minecraft.world.item.ItemStack.of(converted));
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }
}
