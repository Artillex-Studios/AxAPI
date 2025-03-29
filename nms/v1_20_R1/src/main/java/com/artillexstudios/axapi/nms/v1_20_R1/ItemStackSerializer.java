package com.artillexstudios.axapi.nms.v1_20_R1;

import com.artillexstudios.axapi.utils.logging.LogUtils;
import com.google.common.base.Preconditions;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Dynamic;
import net.minecraft.SharedConstants;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtIo;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.SnbtPrinterTagVisitor;
import net.minecraft.nbt.TagParser;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.datafix.fixes.References;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_20_R1.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

public enum ItemStackSerializer {
    INSTANCE;

    public byte[] serializeAsBytes(ItemStack itemStack) {
        Preconditions.checkArgument(itemStack != null, "Can't serialise a null itemstack!");
        Preconditions.checkArgument(itemStack.getType() != Material.AIR, "Can't serialise air!");
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            CompoundTag compoundTag = CraftItemStack.asNMSCopy(itemStack).save(new CompoundTag());
            compoundTag.putInt("DataVersion", SharedConstants.getCurrentVersion().getDataVersion().getVersion());
            NbtIo.writeCompressed(compoundTag, outputStream);
            return outputStream.toByteArray();
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    public ItemStack deserializeFromBytes(byte[] bytes) {
        try (ByteArrayInputStream stream = new ByteArrayInputStream(bytes)) {
            CompoundTag compound = NbtIo.readCompressed(stream);
            int dataVersion = compound.getInt("DataVersion");
            CompoundTag converted = (CompoundTag) MinecraftServer.getServer().fixerUpper.update(References.ITEM_STACK, new Dynamic<>(NbtOps.INSTANCE, compound), dataVersion, SharedConstants.getCurrentVersion().getDataVersion().getVersion()).getValue();
            return CraftItemStack.asCraftMirror(net.minecraft.world.item.ItemStack.of(converted));
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    public String serializeAsSnbt(ItemStack itemStack) {
        Preconditions.checkArgument(itemStack != null, "Can't serialise a null itemstack!");
        Preconditions.checkArgument(itemStack.getType() != Material.AIR, "Can't serialise air!");
        CompoundTag tag = CraftItemStack.asNMSCopy(itemStack).save(new CompoundTag());
        tag.putInt("DataVersion", SharedConstants.getCurrentVersion().getDataVersion().getVersion());
        return new SnbtPrinterTagVisitor().visit(tag);
    }

    public ItemStack deserializeFromSnbt(String snbt) {
        try {
            CompoundTag parsed = TagParser.parseTag(snbt);
            int dataVersion = parsed.getInt("DataVersion");
            CompoundTag converted = (CompoundTag) MinecraftServer.getServer().fixerUpper.update(References.ITEM_STACK, new Dynamic<>(NbtOps.INSTANCE, parsed), dataVersion, SharedConstants.getCurrentVersion().getDataVersion().getVersion()).getValue();
            return CraftItemStack.asCraftMirror(net.minecraft.world.item.ItemStack.of(converted));
        } catch (CommandSyntaxException exception) {
            LogUtils.error("Failed to parse SNBT into an ItemStack! Please, check out the exception for more details!", exception);
            throw new RuntimeException(exception);
        }
    }
}
