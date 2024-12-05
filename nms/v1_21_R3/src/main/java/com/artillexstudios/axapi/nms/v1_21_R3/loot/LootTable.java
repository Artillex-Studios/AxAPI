package com.artillexstudios.axapi.nms.v1_21_R3.loot;

import com.artillexstudios.axapi.loot.LootContextParam;
import com.artillexstudios.axapi.loot.LootContextParamSets;
import com.artillexstudios.axapi.loot.LootContextParams;
import com.artillexstudios.axapi.loot.LootParams;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.kyori.adventure.key.Key;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.context.ContextKey;
import net.minecraft.util.context.ContextKeySet;
import net.minecraft.world.phys.Vec3;
import org.bukkit.Location;
import org.bukkit.block.data.BlockData;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.block.data.CraftBlockData;
import org.bukkit.craftbukkit.entity.CraftEntity;
import org.bukkit.craftbukkit.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.EnumMap;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

public class LootTable implements com.artillexstudios.axapi.loot.LootTable {
    private static final IdentityHashMap<LootContextParam<?>, ContextKey<Object>> params = new IdentityHashMap<>() {{
        this.put(LootContextParams.ATTACKING_ENTITY, (ContextKey<Object>) (Object) net.minecraft.world.level.storage.loot.parameters.LootContextParams.ATTACKING_ENTITY);
        this.put(LootContextParams.BLOCK_STATE, (ContextKey<Object>) (Object) net.minecraft.world.level.storage.loot.parameters.LootContextParams.BLOCK_STATE);
        this.put(LootContextParams.DAMAGE_SOURCE, (ContextKey<Object>) (Object) net.minecraft.world.level.storage.loot.parameters.LootContextParams.DAMAGE_SOURCE);
        this.put(LootContextParams.ORIGIN, (ContextKey<Object>) (Object) net.minecraft.world.level.storage.loot.parameters.LootContextParams.ORIGIN);
        this.put(LootContextParams.TOOL, (ContextKey<Object>) (Object) net.minecraft.world.level.storage.loot.parameters.LootContextParams.TOOL);
        this.put(LootContextParams.DIRECT_ATTACKING_ENTITY, (ContextKey<Object>) (Object) net.minecraft.world.level.storage.loot.parameters.LootContextParams.DIRECT_ATTACKING_ENTITY);
        this.put(LootContextParams.LAST_DAMAGE_PLAYER, (ContextKey<Object>) (Object) net.minecraft.world.level.storage.loot.parameters.LootContextParams.LAST_DAMAGE_PLAYER);
        this.put(LootContextParams.THIS_ENTITY, (ContextKey<Object>) (Object) net.minecraft.world.level.storage.loot.parameters.LootContextParams.THIS_ENTITY);
    }};
    private static final EnumMap<LootContextParamSets, ContextKeySet> sets = new EnumMap<>(LootContextParamSets.class) {{
        this.put(LootContextParamSets.EMPTY, net.minecraft.world.level.storage.loot.parameters.LootContextParamSets.EMPTY);
        this.put(LootContextParamSets.CHEST, net.minecraft.world.level.storage.loot.parameters.LootContextParamSets.CHEST);
        this.put(LootContextParamSets.COMMAND, net.minecraft.world.level.storage.loot.parameters.LootContextParamSets.COMMAND);
        this.put(LootContextParamSets.SELECTOR, net.minecraft.world.level.storage.loot.parameters.LootContextParamSets.SELECTOR);
        this.put(LootContextParamSets.FISHING, net.minecraft.world.level.storage.loot.parameters.LootContextParamSets.FISHING);
        this.put(LootContextParamSets.ENTITY, net.minecraft.world.level.storage.loot.parameters.LootContextParamSets.ENTITY);
        this.put(LootContextParamSets.EQUIPMENT, net.minecraft.world.level.storage.loot.parameters.LootContextParamSets.EQUIPMENT);
        this.put(LootContextParamSets.ARCHAEOLOGY, net.minecraft.world.level.storage.loot.parameters.LootContextParamSets.ARCHAEOLOGY);
        this.put(LootContextParamSets.GIFT, net.minecraft.world.level.storage.loot.parameters.LootContextParamSets.GIFT);
        this.put(LootContextParamSets.PIGLIN_BARTER, net.minecraft.world.level.storage.loot.parameters.LootContextParamSets.PIGLIN_BARTER);
        this.put(LootContextParamSets.VAULT, net.minecraft.world.level.storage.loot.parameters.LootContextParamSets.VAULT);
        this.put(LootContextParamSets.ADVANCEMENT_REWARD, net.minecraft.world.level.storage.loot.parameters.LootContextParamSets.ADVANCEMENT_REWARD);
        this.put(LootContextParamSets.ADVANCEMENT_ENTITY, net.minecraft.world.level.storage.loot.parameters.LootContextParamSets.ADVANCEMENT_ENTITY);
        this.put(LootContextParamSets.ADVANCEMENT_LOCATION, net.minecraft.world.level.storage.loot.parameters.LootContextParamSets.ADVANCEMENT_LOCATION);
        this.put(LootContextParamSets.BLOCK_USE, net.minecraft.world.level.storage.loot.parameters.LootContextParamSets.BLOCK_USE);
        this.put(LootContextParamSets.GENERIC, net.minecraft.world.level.storage.loot.parameters.LootContextParamSets.ALL_PARAMS);
        this.put(LootContextParamSets.BLOCK, net.minecraft.world.level.storage.loot.parameters.LootContextParamSets.BLOCK);
        this.put(LootContextParamSets.SHEARING, net.minecraft.world.level.storage.loot.parameters.LootContextParamSets.SHEARING);
        this.put(LootContextParamSets.ENCHANTED_DAMAGE, net.minecraft.world.level.storage.loot.parameters.LootContextParamSets.ENCHANTED_DAMAGE);
        this.put(LootContextParamSets.ENCHANTED_ITEM, net.minecraft.world.level.storage.loot.parameters.LootContextParamSets.ENCHANTED_ITEM);
        this.put(LootContextParamSets.ENCHANTED_LOCATION, net.minecraft.world.level.storage.loot.parameters.LootContextParamSets.ENCHANTED_LOCATION);
        this.put(LootContextParamSets.ENCHANTED_ENTITY, net.minecraft.world.level.storage.loot.parameters.LootContextParamSets.ENCHANTED_ENTITY);
        this.put(LootContextParamSets.HIT_BLOCK, net.minecraft.world.level.storage.loot.parameters.LootContextParamSets.HIT_BLOCK);
    }};
    private final net.minecraft.world.level.storage.loot.LootTable lootTable;

    public LootTable(Key key) {
        this.lootTable = MinecraftServer.getServer().reloadableRegistries().getLootTable(ResourceKey.create(Registries.LOOT_TABLE, ResourceLocation.fromNamespaceAndPath(key.namespace(), key.value())));
    }

    private static net.minecraft.world.level.storage.loot.LootParams toLootParams(LootParams lootParams) {
        net.minecraft.world.level.storage.loot.LootParams.Builder builder = new net.minecraft.world.level.storage.loot.LootParams.Builder(((CraftWorld) lootParams.world()).getHandle());

        for (Map.Entry<LootContextParam<?>, Object> param : lootParams.params().entrySet()) {
            Object value = switch (param.getValue()) {
                case Player player -> {
                    if (param.getKey() == LootContextParams.DAMAGE_SOURCE) {
                        yield ((CraftPlayer) player).getHandle().damageSources().playerAttack(((CraftPlayer) player).getHandle());
                    }

                    yield ((CraftPlayer) player).getHandle();
                }
                case Entity entity -> {
                    if (param.getKey() == LootContextParams.DAMAGE_SOURCE) {
                        yield ((CraftEntity) entity).getHandle().damageSources().mobAttack(((CraftLivingEntity) entity).getHandle());
                    }

                    yield ((CraftEntity) entity).getHandle();
                }
                case BlockData blockData -> ((CraftBlockData) blockData).getState();
                case ItemStack itemStack -> CraftItemStack.asNMSCopy(itemStack);
                case Location location -> new Vec3(location.x(), location.y(), location.z());
                case null, default -> null;
            };

            if (value == null) {
                continue;
            }

            builder.withParameter(params.get(param.getKey()), value);
        }

        return builder.create(sets.get(lootParams.sets()));
    }

    @Override
    public List<ItemStack> randomItems(LootParams params) {
        ObjectArrayList<net.minecraft.world.item.ItemStack> items = this.lootTable.getRandomItems(toLootParams(params));
        ObjectArrayList<ItemStack> returning = new ObjectArrayList<>(items.size());
        for (net.minecraft.world.item.ItemStack item : items) {
            returning.add(CraftItemStack.asCraftMirror(item));
        }

        return returning;
    }
}
