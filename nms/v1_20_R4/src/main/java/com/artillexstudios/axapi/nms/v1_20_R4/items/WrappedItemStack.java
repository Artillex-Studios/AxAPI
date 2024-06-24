package com.artillexstudios.axapi.nms.v1_20_R4.items;

import com.artillexstudios.axapi.items.component.DataComponent;
import com.artillexstudios.axapi.nms.v1_20_R4.ItemStackSerializer;
import net.minecraft.nbt.SnbtPrinterTagVisitor;
import net.minecraft.server.MinecraftServer;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.craftbukkit.util.CraftMagicNumbers;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class WrappedItemStack implements com.artillexstudios.axapi.items.WrappedItemStack {
    public net.minecraft.world.item.ItemStack itemStack;
    private ItemStack bukkitStack;

    public WrappedItemStack(ItemStack itemStack) {
        this(itemStack instanceof CraftItemStack cr ? cr.handle : CraftItemStack.asNMSCopy(itemStack));
        this.bukkitStack = itemStack;
    }

    public WrappedItemStack(net.minecraft.world.item.ItemStack itemStack) {
        this.itemStack = itemStack;
        this.bukkitStack = null;
    }

    @Override
    public <T> void set(DataComponent<T> component, T value) {
        component.apply(itemStack, value);
    }

    @Override
    public <T> T get(DataComponent<T> component) {
        return component.get(itemStack);
    }

    @Override
    public int getAmount() {
        return itemStack.getCount();
    }

    @Override
    public void setAmount(int amount) {
        itemStack.setCount(amount);
    }

    @Override
    public ItemStack toBukkit() {
        return CraftItemStack.asBukkitCopy(itemStack);
    }

    @Override
    public String toSNBT() {
        var compoundTag = (net.minecraft.nbt.CompoundTag) itemStack.save(MinecraftServer.getServer().registryAccess());
        compoundTag.putInt("DataVersion", CraftMagicNumbers.INSTANCE.getDataVersion());
        return new SnbtPrinterTagVisitor().visit(compoundTag);
    }

    @Override
    public byte[] serialize() {
        return ItemStackSerializer.INSTANCE.serializeAsBytes(toBukkit());
    }

    @Override
    public void finishEdit() {
        ItemMeta meta = CraftItemStack.getItemMeta(itemStack);
        if (bukkitStack != null) {
            bukkitStack.setItemMeta(meta);
        } else {
            CraftItemStack.setItemMeta(itemStack, meta);
        }
    }
}
