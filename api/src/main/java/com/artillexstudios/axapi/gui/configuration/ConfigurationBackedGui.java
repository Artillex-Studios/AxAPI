package com.artillexstudios.axapi.gui.configuration;

import com.artillexstudios.axapi.config.adapters.MapConfigurationGetter;
import com.artillexstudios.axapi.context.HashMapContext;
import com.artillexstudios.axapi.gui.configuration.actions.Action;
import com.artillexstudios.axapi.gui.configuration.actions.Actions;
import com.artillexstudios.axapi.gui.inventory.Gui;
import com.artillexstudios.axapi.gui.inventory.GuiItem;
import com.artillexstudios.axapi.gui.inventory.GuiKeys;
import com.artillexstudios.axapi.gui.inventory.implementation.PaginatedGui;
import com.artillexstudios.axapi.gui.inventory.provider.GuiItemProvider;
import com.artillexstudios.axapi.gui.inventory.provider.implementation.CachingGuiItemProvider;
import com.artillexstudios.axapi.placeholders.PlaceholderParameters;
import com.artillexstudios.axapi.utils.ItemBuilder;
import com.artillexstudios.axapi.utils.UncheckedUtils;
import com.artillexstudios.axapi.utils.featureflags.FeatureFlags;
import com.artillexstudios.axapi.utils.logging.LogUtils;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.regex.Pattern;

public class ConfigurationBackedGui<T extends Gui> {
    private static final Pattern SPLIT_PATTERN = Pattern.compile("-");
    private final ConfigurationBackedGuiBuilder builder;

    public ConfigurationBackedGui(ConfigurationBackedGuiBuilder builder) {
        this.builder = builder;
    }

    private void setItems(T gui, HashMapContext globalContext) {
        List<MapConfigurationGetter> items = this.builder.configuration().<MapConfigurationGetter, Map<?, ?>>getList("items", MapConfigurationGetter::new);
        if (items == null) {
            items = new ArrayList<>();
            if (this.builder.supportsLegacy()) {
                for (Object key : this.builder.configuration().keys()) {
                    String keyString = key.toString();
                    if (this.builder.overrideItems().containsKey(keyString) || this.builder.ignoredKeys().contains(keyString)) {
                        continue;
                    }

                    items.add(this.builder.configuration().getConfiguration(keyString));
                }
            }
        }

        for (MapConfigurationGetter item : items) {
            Object slotConfig = item.getObject("slots");
            if (slotConfig == null) {
                LogUtils.warn("Failed to add item: {} to the gui, because it didn't have any slots set up!", item);
                continue;
            }


            IntList slots = this.slots(slotConfig);
            List<Action<?>> actions = Actions.INSTANCE.parseAll(item.getList("actions"));
            GuiItemProvider provider = new CachingGuiItemProvider(new GuiItem(ctx -> ItemBuilder.create(item, new PlaceholderParameters(ctx)).wrapped(), (context, event) -> {
                context.merge(globalContext);
                for (Action<?> action : actions) {
                    action.run((Player) event.getWhoClicked(), context);
                }
            }));
            slots.intIterator().forEachRemaining(integer -> {
                gui.setItem(integer, provider);
            });
        }
        // Create override items, which are added via code
        for (Map.Entry<String, Consumer<InventoryClickEvent>> entry : this.builder.overrideItems().entrySet()) {
            MapConfigurationGetter item = this.builder.configuration().getConfiguration(entry.getKey());
            if (item == null) {
                if (FeatureFlags.STRICT_ITEM_OVERRIDE_HANDLING.get()) {
                    LogUtils.error("Could not find item within section {} in file! Did you remove it? This is not allowed! Please regenerate the file!", entry.getKey());
                    throw new IllegalStateException();
                }
                return;
            }

            Object slotConfig = item.getObject("slots");
            if (slotConfig == null) {
                LogUtils.warn("Failed to add item: {} to the gui, because it didn't have any slots set up!", item);
                continue;
            }

            IntList slots = this.slots(slotConfig);
            List<Action<?>> actions = Actions.INSTANCE.parseAll(item.getList("actions"));
            GuiItemProvider provider = new CachingGuiItemProvider(new GuiItem(ctx -> ItemBuilder.create(item, new PlaceholderParameters(ctx)).wrapped(), (context, event) -> {
                context.merge(globalContext);
                for (Action<?> action : actions) {
                    action.run((Player) event.getWhoClicked(), context);
                }

                entry.getValue().accept(event);
            }));
            slots.intIterator().forEachRemaining(integer -> {
                gui.setItem(integer, provider);
            });
        }
    }

    public IntList slots(Object slots) {
        return switch (slots) {
            case Number number -> IntList.of(number.intValue());
            case String str -> this.parseSlot(str);
            case List<?> list -> {
                IntList newList = new IntArrayList(list.size());
                for (Object o : list) {
                    newList.addAll(this.slots(o));
                }

                yield newList;
            }
            default -> IntList.of();
        };
    }

    public ConfigurationBackedGuiBuilder builder() {
        return this.builder;
    }

    private IntList parseSlot(String str) {
        if (str.contains("-")) {
            IntList list = new IntArrayList();
            String[] arr = SPLIT_PATTERN.split(str);
            int from = Integer.parseInt(arr[0]);
            int to = Integer.parseInt(arr[1]);
            int min = Math.min(from, to);
            int max = Math.max(from, to);
            for (int i = min; i <= max; i++) {
                list.add(i);
            }
            return list;
        }

        return IntList.of(Integer.parseInt(str));
    }

    /**
     * Creates an instance of this gui configuration for the player.
     * @param player The player to create the inventory for.
     * @param globalContext The data from this context is available in click events, this allows for having extra state which is provided when
     * creating the gui.
     * @return A gui instance.
     * @throws IllegalStateException If the construction of the gui failed due to a misconfiguration, and
     * {@link FeatureFlags#STRICT_ITEM_OVERRIDE_HANDLING} is set to true.
     */
    public synchronized T create(Player player, HashMapContext globalContext) throws IllegalStateException {
        this.builder.guiBuilder().context(globalContext);
        T gui = UncheckedUtils.unsafeCast(this.builder.guiBuilder().build(player));
        if (gui instanceof PaginatedGui paginatedGui) {
            if (this.builder.objectProvider() != null) {
                paginatedGui.setItemsProvider(this.builder.objectProvider().apply(globalContext));
            }
        }

        gui.disableAllInteractions();
        try {
            this.setItems(gui, globalContext);
        } catch (IllegalStateException exception) {
            return null;
        }

        if (this.builder.inventoryCloseListener() != null) {
            gui.onClose(this.builder.inventoryCloseListener());
        }

        if (this.builder.inventoryOpenListener() != null) {
            gui.onOpen(this.builder.inventoryOpenListener());
        }

        if (this.builder.playerInventoryClickListener() != null) {
            gui.onPlayerInventoryClick(this.builder.playerInventoryClickListener());
        }

        if (this.builder.refreshInterval() != null) {
            gui.refreshInterval(this.builder.refreshInterval());
        }

        return gui;
    }

    /**
     * Creates an instance of this gui configuration for the player.
     * @param player The player to create the inventory for.
     * @return A gui instance.
     * @throws IllegalStateException If the construction of the gui failed due to a misconfiguration, and
     * {@link FeatureFlags#STRICT_ITEM_OVERRIDE_HANDLING} is set to true.
     */
    public T create(Player player) throws IllegalStateException {
        return this.create(player, HashMapContext.create()
                .with(GuiKeys.PLAYER, player)
        );
    }
}
