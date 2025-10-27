package com.artillexstudios.axapi.nms.v1_21_R4.items;

import com.artillexstudios.axapi.items.HashGenerator;
import com.artillexstudios.axapi.items.WrappedItemStack;
import net.minecraft.core.Holder;
import net.minecraft.network.HashedPatchMap;
import net.minecraft.world.item.Item;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

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
    public int hash() {
        if (this.stack instanceof net.minecraft.network.HashedStack.ActualItem(Holder<Item> item, int count, HashedPatchMap components)) {
            Map<Object, Object> ordered1 = components.addedComponents().entrySet().stream()
                    .sorted(Map.Entry.comparingByKey((cmp, other) -> other.toString().compareTo(cmp.toString())))
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));

            Set<Object> ordered2 = components.removedComponents().stream()
                    .sorted(((cmp, other) -> other.toString().compareTo(cmp.toString())))
                    .collect(Collectors.toCollection(LinkedHashSet::new));
            return Objects.hash(item, count, ordered1, ordered2);
        }

        return 0;
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
