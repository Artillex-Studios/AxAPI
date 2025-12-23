package com.artillexstudios.axapi.nms.v1_21_R4.items;

import com.artillexstudios.axapi.items.HashGenerator;
import com.artillexstudios.axapi.items.HashedStack;
import com.artillexstudios.axapi.items.component.DataComponent;
import com.artillexstudios.axapi.nms.v1_21_R4.ItemStackSerializer;
import com.artillexstudios.axapi.reflection.FieldAccessor;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Objects;

public class WrappedItemStack implements com.artillexstudios.axapi.items.WrappedItemStack {
    private static final FieldAccessor handleAccessor = FieldAccessor.builder()
            .withClass(CraftItemStack.class)
            .withField("handle").build();
    public net.minecraft.world.item.ItemStack itemStack;
    private ItemStack bukkitStack;
    private boolean dirty = false;

    public WrappedItemStack(ItemStack itemStack) {
        this(itemStack.getType().isAir() ? net.minecraft.world.item.ItemStack.EMPTY : itemStack instanceof CraftItemStack cr ? handleAccessor.get(cr, net.minecraft.world.item.ItemStack.class) : CraftItemStack.asNMSCopy(itemStack));
        this.bukkitStack = itemStack;
    }

    public WrappedItemStack(net.minecraft.world.item.ItemStack itemStack) {
        this.itemStack = itemStack;
        this.bukkitStack = null;
    }

    @Override
    public <T> void set(DataComponent<T> component, T value) {
        this.dirty = true;
        component.apply(this.itemStack, value);
    }

    @Override
    public <T> T get(DataComponent<T> component) {
        return component.get(this.itemStack);
    }

    @Override
    public int getAmount() {
        return this.itemStack.getCount();
    }

    @Override
    public void setAmount(int amount) {
        this.itemStack.setCount(amount);
    }

    @Override
    public ItemStack toBukkit() {
        return CraftItemStack.asBukkitCopy(this.itemStack);
    }

    @Override
    public HashedStack toHashedStack(HashGenerator generator) {
        return com.artillexstudios.axapi.nms.v1_21_R4.items.HashedStack.create(this, generator);
    }

    @Override
    public String toSNBT() {
        return ItemStackSerializer.INSTANCE.serializeAsSnbt(this.toBukkit());
    }

    @Override
    public byte[] serialize() {
        return ItemStackSerializer.INSTANCE.serializeAsBytes(this.toBukkit());
    }

    @Override
    public void finishEdit() {
        if (!this.dirty) {
            return;
        }

        ItemMeta meta = CraftItemStack.getItemMeta(this.itemStack);
        if (this.bukkitStack != null) {
            this.bukkitStack.setItemMeta(meta);
        } else {
            CraftItemStack.setItemMeta(this.itemStack, meta);
        }
    }

    @Override
    public WrappedItemStack copy() {
        finishEdit();
        return new WrappedItemStack(this.itemStack.copy());
    }

    @Override
    public void editFrom(com.artillexstudios.axapi.items.WrappedItemStack stack) {
        this.itemStack = ((WrappedItemStack) stack).itemStack;
        this.bukkitStack = ((WrappedItemStack) stack).bukkitStack;
    }

    @Override
    public void update(boolean force) {

    }

    @Override
    public ItemStack wrapped() {
        return this.toBukkit();
    }

    @Override
    public net.minecraft.world.item.ItemStack asMinecraft() {
        return this.itemStack;
    }

    @Override
    public final boolean equals(Object object) {
        if (!(object instanceof WrappedItemStack that)) {
            return false;
        }

        return Objects.equals(this.itemStack, that.itemStack) && Objects.equals(this.bukkitStack, that.bukkitStack);
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(this.itemStack);
        result = 31 * result + Objects.hashCode(this.bukkitStack);
        return result;
    }
}
