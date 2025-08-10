package com.artillexstudios.axapi.hologram;

import com.artillexstudios.axapi.hologram.page.DroppedItemHologramPage;
import com.artillexstudios.axapi.hologram.page.TextDisplayHologramPage;
import com.artillexstudios.axapi.items.WrappedItemStack;

public final class HologramTypes {
    public static final HologramType<String> TEXT = new HologramType<>(TextDisplayHologramPage::new);
    public static final HologramType<WrappedItemStack> ITEM_STACK = new HologramType<>(DroppedItemHologramPage::new);
//    public static final HologramType<EntityType> ENTITY = new HologramType<>();
//    public static final HologramType<WrappedItemStack> HEAD = new HologramType<>();
}
