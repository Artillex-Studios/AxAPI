package com.artillexstudios.axapi.nms.v1_21_R4.items;

import com.artillexstudios.axapi.items.HashGenerator;
import com.artillexstudios.axapi.items.WrappedItemStack;

import java.util.Objects;

public record HashedStack(
        net.minecraft.network.HashedStack stack) implements com.artillexstudios.axapi.items.HashedStack {

    public static HashedStack create(com.artillexstudios.axapi.nms.v1_21_R4.items.WrappedItemStack stack, HashGenerator generator) {
        return new HashedStack(net.minecraft.network.HashedStack.create(stack.itemStack, generator::apply));
    }

    @Override
    public boolean matches(WrappedItemStack stack, HashGenerator generator) {
        return this.stack.matches(((com.artillexstudios.axapi.nms.v1_21_R4.items.WrappedItemStack) stack).itemStack, generator::apply);
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof HashedStack(net.minecraft.network.HashedStack stack1))) {
            return false;
        }

        return Objects.equals(this.stack, stack1);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.stack);
    }
}
