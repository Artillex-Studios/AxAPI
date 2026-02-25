package com.artillexstudios.axapi.loot;

import net.kyori.adventure.key.Key;
import org.bukkit.Location;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public final class LootContextParams {
    public static final LootContextParam<Entity> THIS_ENTITY = create("this_entity");
    public static final LootContextParam<Player> LAST_DAMAGE_PLAYER = create("last_damage_player");
    public static final LootContextParam<Entity> DAMAGE_SOURCE = create("damage_source");
    public static final LootContextParam<Entity> ATTACKING_ENTITY = create("attacking_entity");
    public static final LootContextParam<Entity> DIRECT_ATTACKING_ENTITY = create("direct_attacking_entity");
    public static final LootContextParam<Location> ORIGIN = create("origin");
    public static final LootContextParam<BlockData> BLOCK_STATE = create("block_state");
    public static final LootContextParam<ItemStack> TOOL = create("tool");
    public static final LootContextParam<Entity> INTERACTING_ENTITY = create("interacting_entity");
    public static final LootContextParam<Entity> TARGET_ENTITY = create("target_entity");

    private static <T> LootContextParam<T> create(String key) {
        return new LootContextParam<>(Key.key(Key.MINECRAFT_NAMESPACE, key));
    }
}
