package com.artillexstudios.axapi.loot;

import org.bukkit.inventory.ItemStack;

import java.util.List;

public interface LootTable {

    List<ItemStack> randomItems(LootParams params);
}
