package com.artillexstudios.axapi.utils;

import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class BlacklistUtils {
    private final Map<Object, Object> map;

    public BlacklistUtils(Map<Object, Object> map) {
        this.map = new HashMap<>(map);
    }

    @NotNull
    private static List<ItemFlag> getItemFlags(@NotNull List<String> flags) {
        final List<ItemFlag> flagList = new ArrayList<>(flags.size());
        for (String flag : flags) {
            ItemFlag itemFlag;
            try {
                itemFlag = ItemFlag.valueOf(flag.toUpperCase(Locale.ENGLISH));
            } catch (Exception exception) {
                continue;
            }

            flagList.add(itemFlag);
        }

        return flagList;
    }

    public boolean allows(ItemStack itemStack, boolean strict) {
        ItemMeta meta = itemStack.getItemMeta();
        int counter = 0;

        String material = (String) map.getOrDefault("type", map.get("material"));
        if (material != null) {
            if (itemStack.getType().name().equalsIgnoreCase(material)) {
                if (strict) {
                    counter++;
                } else {
                    return false;
                }
            }
        }

        Optional<Object> flags = Optional.ofNullable(map.get("item-flags"));
        if (flags.isPresent()) {
            List<ItemFlag> itemFlags = getItemFlags((List<String>) flags.get());
            if (strict) {
                itemFlags.removeAll(meta.getItemFlags());
                if (itemFlags.isEmpty()) {
                    counter++;
                }
            } else {
                Set<ItemFlag> metaFlags = meta.getItemFlags();
                for (ItemFlag itemFlag : itemFlags) {
                    if (metaFlags.contains(itemFlag)) {
                        return false;
                    }
                }
            }
        }

        Optional<Object> name = Optional.ofNullable(map.get("name"));
        if (name.isPresent()) {
            String displayName = meta.getDisplayName();
            if (displayName.equalsIgnoreCase((String) name.get())) {
                if (strict) {
                    counter++;
                } else {
                    return false;
                }
            }
        }


        Optional<Object> lore = Optional.ofNullable(map.get("lore"));
        if (lore.isPresent()) {
            List<String> loreLines = (List<String>) flags.get();
            if (strict) {
                loreLines.removeAll(meta.getLore());
                if (loreLines.isEmpty()) {
                    counter++;
                }
            } else {
                List<String> metaLore = meta.getLore();
                if (metaLore == null) return false;
                for (String line : metaLore) {
                    if (loreLines.contains(line)) {
                        return false;
                    }
                }
            }
        }

        Optional<Object> customModelData = Optional.ofNullable(map.get("custom-model-data"));
        if (customModelData.isPresent()) {
            int modelData = meta.getCustomModelData();
            if (customModelData.get().equals(modelData)) {
                if (strict) {
                    counter++;
                } else {
                    return false;
                }
            }
        }

        if (strict) {
            return counter != map.size();
        }

        return true;
    }
}
