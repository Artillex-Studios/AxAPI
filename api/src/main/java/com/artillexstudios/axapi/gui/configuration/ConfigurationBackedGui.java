package com.artillexstudios.axapi.gui.configuration;

import com.artillexstudios.axapi.config.YamlConfiguration;
import com.artillexstudios.axapi.config.adapters.MapConfigurationGetter;
import com.artillexstudios.axapi.context.HashMapContext;
import com.artillexstudios.axapi.gui.configuration.actions.Action;
import com.artillexstudios.axapi.gui.configuration.actions.Actions;
import com.artillexstudios.axapi.gui.inventory.Gui;
import com.artillexstudios.axapi.gui.inventory.GuiBuilder;
import com.artillexstudios.axapi.gui.inventory.GuiItem;
import com.artillexstudios.axapi.gui.inventory.GuiKeys;
import com.artillexstudios.axapi.gui.inventory.builder.PaginatedGuiBuilder;
import com.artillexstudios.axapi.gui.inventory.implementation.PaginatedGui;
import com.artillexstudios.axapi.gui.inventory.provider.GuiItemProvider;
import com.artillexstudios.axapi.gui.inventory.provider.implementation.CachingGuiItemProvider;
import com.artillexstudios.axapi.placeholders.PlaceholderHandler;
import com.artillexstudios.axapi.placeholders.PlaceholderParameters;
import com.artillexstudios.axapi.utils.ItemBuilder;
import com.artillexstudios.axapi.utils.StringUtils;
import com.artillexstudios.axapi.utils.UncheckedUtils;
import com.artillexstudios.axapi.utils.featureflags.FeatureFlags;
import com.artillexstudios.axapi.utils.logging.LogUtils;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.regex.Pattern;

public class ConfigurationBackedGui<T extends Gui> {
    private static final Pattern SPLIT_PATTERN = Pattern.compile("-");
    // TODO: Cache providers without any placeholders
    private final List<GuiItemProvider> cachingProviders = new ArrayList<>();
    private final Map<String, Consumer<InventoryClickEvent>> overrideItems = new HashMap<>();
    private final GuiBuilder<?, ?> builder;
    private final YamlConfiguration<?> configuration;
    private final boolean paginated;
    private Function<HashMapContext, Supplier<List<?>>> objectProvider;
    private String title;
    private Integer rows;
    private InventoryType type;

    public ConfigurationBackedGui(YamlConfiguration<?> configuration) {
        this(configuration, false);
    }

    public ConfigurationBackedGui(YamlConfiguration<?> configuration, boolean paginated) {
        this.configuration = configuration;
        this.paginated = paginated;
        this.builder = paginated ? GuiBuilder.createPaginated() : GuiBuilder.createDynamic();
        this.refresh();
    }

    public void refresh() {
        this.configuration.load();
        this.title = this.configuration.getString("title");
        this.rows = this.configuration.getInteger("rows");
        String type = this.configuration.getString("type");
        this.type = type == null ? InventoryType.CHEST : InventoryType.valueOf(type.toUpperCase(Locale.ENGLISH));

        if (this.title != null) {
            this.builder.title(ctx -> {
                Player player = ctx.get(GuiKeys.PLAYER);
                Gui gui = ctx.get(GuiKeys.GUI);
                return StringUtils.format(PlaceholderHandler.parseWithPlaceholderAPI(this.title, new PlaceholderParameters()
                        .withParameters(Player.class, player)
                        .withParameters(Gui.class, gui)
                ));
            });
        }

        this.builder.inventoryType(this.type);
        if (this.rows != null) {
            this.builder.rows(this.rows);
        }
    }

    public void withValues(Function<HashMapContext, Supplier<List<?>>> supplier) {
        if (!(this.builder instanceof PaginatedGuiBuilder)) {
            return;
        }

        this.objectProvider = supplier;
    }

    public <Z> void withProvider(Class<Z> clazz, Function<Z, GuiItemProvider> provider) {
        if (!(this.builder instanceof PaginatedGuiBuilder paginatedGuiBuilder)) {
            return;
        }

        paginatedGuiBuilder.withProvider(clazz, provider);
    }

    private void setItems(T gui, HashMapContext globalContext) {
        // TODO: handle cached items
        List<MapConfigurationGetter> items = this.configuration.getList("items", MapConfigurationGetter::new);
        if (items == null) {
            return;
        }

        for (MapConfigurationGetter item : items) {
            // TODO: create an itembuilder that supports PAPI placeholder parsing
            Object slotConfig = item.getObject("slots");
            if (slotConfig == null) {
                LogUtils.warn("Failed to add item: {} to the gui, because it didn't have any slots set up!", item);
                continue;
            }


            IntList slots = this.slots(slotConfig);
            List<Action<?>> actions = Actions.INSTANCE.parseAll(item.getList("actions"));
            // TODO: Check if has placeholders, maybe add time based caching provider
            GuiItemProvider provider = new CachingGuiItemProvider(new GuiItem(ctx -> new ItemBuilder(item.wrapped()).wrapped(), (context, event) -> {
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
        for (Map.Entry<String, Consumer<InventoryClickEvent>> entry : this.overrideItems.entrySet()) {
            MapConfigurationGetter item = this.configuration.getConfiguration(entry.getKey());
            if (item == null) {
                if (FeatureFlags.STRICT_ITEM_OVERRIDE_HANDLING.get()) {
                    LogUtils.error("Could not find item within section {} in {} file! Did you remove it? This is not allowed! Please regenerate the file!", entry.getKey(), this.configuration.path());
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
            GuiItemProvider provider = new CachingGuiItemProvider(new GuiItem(ctx -> new ItemBuilder(item.wrapped()).wrapped(), (context, event) -> {
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

    public void addItemOverride(String section, Consumer<InventoryClickEvent> event) {
        this.overrideItems.put(section, event);
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
    public T create(Player player, HashMapContext globalContext) throws IllegalStateException {
        T gui = UncheckedUtils.unsafeCast(this.builder.build(player));
        if (gui instanceof PaginatedGui paginatedGui) {
            if (this.objectProvider != null) {
                paginatedGui.setItemsProvider(this.objectProvider.apply(globalContext));
            }
        }

        gui.disableAllInteractions();
        try {
            this.setItems(gui, globalContext);
        } catch (IllegalStateException exception) {
            return null;
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
