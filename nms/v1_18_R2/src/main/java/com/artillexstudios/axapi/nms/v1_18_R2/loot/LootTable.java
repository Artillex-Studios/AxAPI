package com.artillexstudios.axapi.nms.v1_18_R2.loot;

import com.artillexstudios.axapi.loot.LootContextParam;
import com.artillexstudios.axapi.loot.LootContextParamSets;
import com.artillexstudios.axapi.loot.LootContextParams;
import com.artillexstudios.axapi.loot.LootParams;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.kyori.adventure.key.Key;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;
import net.minecraft.world.phys.Vec3;
import org.bukkit.Location;
import org.bukkit.block.data.BlockData;
import org.bukkit.craftbukkit.v1_18_R2.CraftWorld;
import org.bukkit.craftbukkit.v1_18_R2.block.data.CraftBlockData;
import org.bukkit.craftbukkit.v1_18_R2.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_18_R2.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.v1_18_R2.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_18_R2.inventory.CraftItemStack;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.EnumMap;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

public class LootTable implements com.artillexstudios.axapi.loot.LootTable {
    private static final IdentityHashMap<LootContextParam<?>, net.minecraft.world.level.storage.loot.parameters.LootContextParam<Object>> params = new IdentityHashMap<>() {{
        this.put(LootContextParams.ATTACKING_ENTITY, (net.minecraft.world.level.storage.loot.parameters.LootContextParam<Object>) (Object) net.minecraft.world.level.storage.loot.parameters.LootContextParams.KILLER_ENTITY);
        this.put(LootContextParams.BLOCK_STATE, (net.minecraft.world.level.storage.loot.parameters.LootContextParam<Object>) (Object) net.minecraft.world.level.storage.loot.parameters.LootContextParams.BLOCK_STATE);
        this.put(LootContextParams.DAMAGE_SOURCE, (net.minecraft.world.level.storage.loot.parameters.LootContextParam<Object>) (Object) net.minecraft.world.level.storage.loot.parameters.LootContextParams.DAMAGE_SOURCE);
        this.put(LootContextParams.ORIGIN, (net.minecraft.world.level.storage.loot.parameters.LootContextParam<Object>) (Object) net.minecraft.world.level.storage.loot.parameters.LootContextParams.ORIGIN);
        this.put(LootContextParams.TOOL, (net.minecraft.world.level.storage.loot.parameters.LootContextParam<Object>) (Object) net.minecraft.world.level.storage.loot.parameters.LootContextParams.TOOL);
        this.put(LootContextParams.DIRECT_ATTACKING_ENTITY, (net.minecraft.world.level.storage.loot.parameters.LootContextParam<Object>) (Object) net.minecraft.world.level.storage.loot.parameters.LootContextParams.DIRECT_KILLER_ENTITY);
        this.put(LootContextParams.LAST_DAMAGE_PLAYER, (net.minecraft.world.level.storage.loot.parameters.LootContextParam<Object>) (Object) net.minecraft.world.level.storage.loot.parameters.LootContextParams.LAST_DAMAGE_PLAYER);
        this.put(LootContextParams.THIS_ENTITY, (net.minecraft.world.level.storage.loot.parameters.LootContextParam<Object>) (Object) net.minecraft.world.level.storage.loot.parameters.LootContextParams.THIS_ENTITY);
    }};
    private static final EnumMap<LootContextParamSets, LootContextParamSet> sets = new EnumMap<>(LootContextParamSets.class) {{
        this.put(LootContextParamSets.EMPTY, net.minecraft.world.level.storage.loot.parameters.LootContextParamSets.EMPTY);
        this.put(LootContextParamSets.CHEST, net.minecraft.world.level.storage.loot.parameters.LootContextParamSets.CHEST);
        this.put(LootContextParamSets.COMMAND, net.minecraft.world.level.storage.loot.parameters.LootContextParamSets.COMMAND);
        this.put(LootContextParamSets.SELECTOR, net.minecraft.world.level.storage.loot.parameters.LootContextParamSets.SELECTOR);
        this.put(LootContextParamSets.FISHING, net.minecraft.world.level.storage.loot.parameters.LootContextParamSets.FISHING);
        this.put(LootContextParamSets.ENTITY, net.minecraft.world.level.storage.loot.parameters.LootContextParamSets.ENTITY);
        this.put(LootContextParamSets.GIFT, net.minecraft.world.level.storage.loot.parameters.LootContextParamSets.GIFT);
        this.put(LootContextParamSets.PIGLIN_BARTER, net.minecraft.world.level.storage.loot.parameters.LootContextParamSets.PIGLIN_BARTER);
        this.put(LootContextParamSets.ADVANCEMENT_REWARD, net.minecraft.world.level.storage.loot.parameters.LootContextParamSets.ADVANCEMENT_REWARD);
        this.put(LootContextParamSets.ADVANCEMENT_ENTITY, net.minecraft.world.level.storage.loot.parameters.LootContextParamSets.ADVANCEMENT_ENTITY);
        this.put(LootContextParamSets.GENERIC, net.minecraft.world.level.storage.loot.parameters.LootContextParamSets.ALL_PARAMS);
        this.put(LootContextParamSets.BLOCK, net.minecraft.world.level.storage.loot.parameters.LootContextParamSets.BLOCK);
    }};
    private final net.minecraft.world.level.storage.loot.LootTable lootTable;

    public LootTable(Key key) {
        this.lootTable = MinecraftServer.getServer().getLootTables().get(new ResourceLocation(key.namespace(), key.value()));
    }

    private static net.minecraft.world.level.storage.loot.LootContext toLootParams(LootParams lootParams) {
        net.minecraft.world.level.storage.loot.LootContext.Builder builder = new net.minecraft.world.level.storage.loot.LootContext.Builder(((CraftWorld) lootParams.world()).getHandle());

        for (Map.Entry<LootContextParam<?>, Object> param : lootParams.params().entrySet()) {
            Object value;
            if (param.getValue() instanceof Player player) {
                if (param.getKey() == LootContextParams.DAMAGE_SOURCE) {
                    value = DamageSource.playerAttack(((CraftPlayer) player).getHandle());
                } else {
                    value = ((CraftPlayer) player).getHandle();
                }
            } else if (param.getValue() instanceof Entity entity) {
                if (param.getKey() == LootContextParams.DAMAGE_SOURCE) {
                    value = DamageSource.mobAttack(((CraftLivingEntity) entity).getHandle());
                } else {
                    value = ((CraftEntity) entity).getHandle();
                }
            } else if (param.getValue() instanceof BlockData blockData) {
                value = ((CraftBlockData) blockData).getState();
            } else if (param.getValue() instanceof ItemStack itemStack) {
                value = CraftItemStack.asNMSCopy(itemStack);
            } else if (param.getValue() instanceof Location location) {
                value = new Vec3(location.getX(), location.getY(), location.getZ());
            } else {
                value = null;
            }

            if (value == null) {
                continue;
            }

            builder.withParameter(params.get(param.getKey()), value);
        }

        return builder.create(sets.get(lootParams.sets()));
    }

    @Override
    public List<ItemStack> randomItems(LootParams params) {
        List<net.minecraft.world.item.ItemStack> items = this.lootTable.getRandomItems(toLootParams(params));
        ObjectArrayList<ItemStack> returning = new ObjectArrayList<>(items.size());
        for (net.minecraft.world.item.ItemStack item : items) {
            returning.add(CraftItemStack.asCraftMirror(item));
        }

        return returning;
    }
}

