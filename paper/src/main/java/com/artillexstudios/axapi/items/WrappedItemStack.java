package com.artillexstudios.axapi.items;

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

    <T> void set(com.artillexstudios.axapi.items.components.DataComponent<T> component, T value);

    default <T> T get(com.artillexstudios.axapi.items.components.DataComponent<T> component) {
        if (component == null) {
            return null;
        }

        return component.getData(this);
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
