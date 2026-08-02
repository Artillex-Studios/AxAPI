package com.artillexstudios.axapi.items;

import com.artillexstudios.axapi.items.component.DataComponent;
import com.artillexstudios.axapi.nms.wrapper.Wrapper;
import com.artillexstudios.axapi.nms.wrapper.WrapperRegistry;
import org.bukkit.inventory.ItemStack;

import java.util.function.Function;

public interface WrappedItemStack extends Wrapper<ItemStack> {

    static WrappedItemStack wrap(Object data) {
        return WrapperRegistry.ITEM_STACK.map(data);
    }

    static <T> T edit(ItemStack itemStack, Function<WrappedItemStack, T> function) {
        WrappedItemStack wrapped = wrap(itemStack);
        T result = function.apply(wrapped);
        wrapped.finishEdit();
        return result;
    }

    <T> void set(DataComponent<T> component, T value);

    default <T> void setNew(com.artillexstudios.axapi.items.components.DataComponent<T> component, T value) {
        throw new IllegalStateException("Not yet implemented");
    }

    <T> T get(DataComponent<T> component);

    default <T> T getNew(com.artillexstudios.axapi.items.components.DataComponent<T> component) {
        throw new IllegalStateException("Not yet implemented");
    }

    void setAmount(int amount);

    int getAmount();

    ItemStack toBukkit();

    HashedStack toHashedStack(HashGenerator generator);

    String toSNBT();

    byte[] serialize();

    void finishEdit();

    WrappedItemStack copy();

    /**
     * Modifies the wrapped ItemStack instance of this
     * WrappedItemStack to be the same as the other item's.
     * @param stack The WrappedItemStack to copy from
     */
    void editFrom(WrappedItemStack stack);

    enum CodecData {
        OPTIONAL_STREAM_CODEC,
        OPTIONAL_UNTRUSTED_STREAM_CODEC,
        STREAM_CODEC;
    }
}
