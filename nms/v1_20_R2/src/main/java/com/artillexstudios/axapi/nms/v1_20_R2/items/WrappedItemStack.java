package com.artillexstudios.axapi.nms.v1_20_R2.items;

import com.artillexstudios.axapi.items.component.DataComponent;
import com.artillexstudios.axapi.nms.v1_20_R2.ItemStackSerializer;
import com.artillexstudios.axapi.reflection.FastFieldAccessor;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.SnbtPrinterTagVisitor;
import net.minecraft.world.item.ItemStack;
import org.bukkit.craftbukkit.v1_20_R2.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_20_R2.util.CraftMagicNumbers;

public class WrappedItemStack implements com.artillexstudios.axapi.items.WrappedItemStack {
    private static final FastFieldAccessor HANDLE_ACCESSOR = FastFieldAccessor.forClassField(CraftItemStack.class, "handle");
    public final ItemStack parent;
    private final org.bukkit.inventory.ItemStack bukkitStack;

    public WrappedItemStack(org.bukkit.inventory.ItemStack itemStack) {
        this.parent = itemStack.getType().isAir() ? ItemStack.EMPTY : itemStack instanceof CraftItemStack ? HANDLE_ACCESSOR.get(itemStack) : CraftItemStack.asNMSCopy(itemStack);
        bukkitStack = itemStack;
    }

    public WrappedItemStack(ItemStack itemStack) {
        this.parent = itemStack;
        this.bukkitStack = parent.getBukkitStack();
    }

    @Override
    public <T> void set(DataComponent<T> component, T value) {
        component.apply(parent, value);
    }

    @Override
    public <T> T get(DataComponent<T> component) {
        return component.get(parent);
    }

    @Override
    public int getAmount() {
        return parent.getCount();
    }

    @Override
    public void setAmount(int amount) {
        this.parent.setCount(amount);
    }

    @Override
    public org.bukkit.inventory.ItemStack toBukkit() {
        return CraftItemStack.asCraftMirror(parent);
    }

    @Override
    public String toSNBT() {
        var compoundTag = (net.minecraft.nbt.CompoundTag) parent.save(new CompoundTag());
        compoundTag.putInt("DataVersion", CraftMagicNumbers.INSTANCE.getDataVersion());
        return new SnbtPrinterTagVisitor().visit(compoundTag);
    }

    @Override
    public byte[] serialize() {
        return ItemStackSerializer.INSTANCE.serializeAsBytes(CraftItemStack.asBukkitCopy(parent));
    }

    @Override
    public void finishEdit() {
        CompoundTag tag = parent.getTag();
        if (tag == null || tag.isEmpty()) {
            if (CraftItemStack.class.isAssignableFrom(bukkitStack.getClass())) {
                CraftItemStack craftItemStack = (CraftItemStack) bukkitStack;
                ItemStack handle = HANDLE_ACCESSOR.get(craftItemStack);
                handle.setTag(null);
            } else {
                parent.setTag(null);
                org.bukkit.inventory.ItemStack bukkitItem = CraftItemStack.asCraftMirror(parent);
                bukkitStack.setItemMeta(bukkitItem.getItemMeta());
            }
            return;
        }

        if (CraftItemStack.class.isAssignableFrom(bukkitStack.getClass())) {
            CraftItemStack craftItemStack = (CraftItemStack) bukkitStack;
            ItemStack handle = HANDLE_ACCESSOR.get(craftItemStack);
            handle.setTag(tag);
        } else {
            parent.setTag(tag);
            org.bukkit.inventory.ItemStack bukkitItem = CraftItemStack.asCraftMirror(parent);
            bukkitStack.setItemMeta(bukkitItem.getItemMeta());
        }
    }

    @Override
    public com.artillexstudios.axapi.items.WrappedItemStack copy() {
        finishEdit();
        return new WrappedItemStack(this.parent.copy());
    }
}
