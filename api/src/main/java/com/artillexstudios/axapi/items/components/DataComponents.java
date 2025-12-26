package com.artillexstudios.axapi.items.components;

import com.artillexstudios.axapi.items.components.data.ItemLore;
import com.artillexstudios.axapi.items.nbt.CompoundTag;
import com.artillexstudios.axapi.nms.NMSHandlers;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;

public final class DataComponents {
    public static final DataComponent<CompoundTag> CUSTOM_DATA = fetch("custom_data");
    public static final DataComponent<Integer> MAX_STACK_SIZE = fetch("max_stack_size");
    public static final DataComponent<Integer> MAX_DAMAGE = fetch("max_damage");
    public static final DataComponent<Integer> DAMAGE = fetch("damage");
    public static final DataComponent<Component> CUSTOM_NAME = fetch("custom_name");
    public static final DataComponent<Component> ITEM_NAME = fetch("item_name");
    public static final DataComponent<Key> ITEM_MODEL = fetch("item_model");
    public static final DataComponent<Key> TOOLTIP_STYLE = fetch("tooltip_style");
    public static final DataComponent<ItemLore> LORE = fetch("lore");

    private static <T extends DataComponent<?>> T fetch(String id) {
        return NMSHandlers.getNmsHandler().getDataComponent(id);
    }
}
