package com.artillexstudios.axapi.loot;

import com.artillexstudios.axapi.nms.NMSHandlers;
import net.kyori.adventure.key.Key;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.EntityType;

public interface LootTables {

    static LootTable lootTable(Key key) {
        return NMSHandlers.getNmsHandler().lootTable(key);
    }

    static LootTable entityLootTable(EntityType entityType) {
        return NMSHandlers.getNmsHandler().lootTable(Key.key("minecraft", "entities/" + entityType.getName()));
    }

    static LootTable blockLoottable(BlockData block) {
        return NMSHandlers.getNmsHandler().lootTable(Key.key("minecraft", "blocks/" + block.getMaterial().getKey().value()));
    }
}
