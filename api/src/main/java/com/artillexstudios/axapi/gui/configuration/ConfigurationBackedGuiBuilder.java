package com.artillexstudios.axapi.gui.configuration;

import com.artillexstudios.axapi.config.YamlConfiguration;
import com.artillexstudios.axapi.config.adapters.MapConfigurationGetter;
import com.artillexstudios.axapi.context.HashMapContext;
import com.artillexstudios.axapi.gui.inventory.Gui;
import com.artillexstudios.axapi.gui.inventory.GuiBuilder;
import com.artillexstudios.axapi.gui.inventory.GuiKeys;
import com.artillexstudios.axapi.gui.inventory.builder.PaginatedGuiBuilder;
import com.artillexstudios.axapi.gui.inventory.provider.GuiItemProvider;
import com.artillexstudios.axapi.placeholders.PlaceholderHandler;
import com.artillexstudios.axapi.placeholders.PlaceholderParameters;
import com.artillexstudios.axapi.utils.StringUtils;
import com.artillexstudios.axapi.utils.YamlUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public final class ConfigurationBackedGuiBuilder {
    private final List<String> ignoredKeys = new ArrayList<>(List.of("title", "rows", "type"));
    private final Map<String, Consumer<InventoryClickEvent>> overrideItems = new HashMap<>();
    private Function<HashMapContext, Supplier<List<?>>> objectProvider;
    private Consumer<InventoryCloseEvent> inventoryCloseListener;
    private Consumer<InventoryOpenEvent> inventoryOpenListener;
    private Integer refreshInterval;
    private final MapConfigurationGetter configuration;
    private final GuiBuilder<?, ?> builder;
    private boolean legacySupport = false;

    public static ConfigurationBackedGuiBuilder builder(MapConfigurationGetter configuration) {
        return builder(configuration, false);
    }

    public static ConfigurationBackedGuiBuilder builder(MapConfigurationGetter configuration, boolean paginated) {
        return new ConfigurationBackedGuiBuilder(configuration, paginated);
    }

    ConfigurationBackedGuiBuilder(MapConfigurationGetter configuration, boolean paginated) {
        this.configuration = configuration;
        this.builder = paginated ? GuiBuilder.createPaginated() : GuiBuilder.createDynamic();
    }

    public ConfigurationBackedGuiBuilder withValues(Function<HashMapContext, Supplier<List<?>>> supplier) {
        if (!(this.builder instanceof PaginatedGuiBuilder)) {
            return this;
        }

        this.objectProvider = supplier;
        return this;
    }

    public ConfigurationBackedGuiBuilder onClose(Consumer<InventoryCloseEvent> consumer) {
        this.inventoryCloseListener = consumer;
        return this;
    }

    public ConfigurationBackedGuiBuilder onOpen(Consumer<InventoryOpenEvent> consumer) {
        this.inventoryOpenListener = consumer;
        return this;
    }

    public ConfigurationBackedGuiBuilder legacySupport() {
        this.legacySupport = true;
        return this;
    }

    public ConfigurationBackedGuiBuilder ignoredKeys(String... keys) {
        this.ignoredKeys.addAll(Arrays.asList(keys));
        return this;
    }

    public <Z> ConfigurationBackedGuiBuilder withProvider(Class<Z> clazz, Function<Z, GuiItemProvider> provider) {
        if (!(this.builder instanceof PaginatedGuiBuilder paginatedGuiBuilder)) {
            return this;
        }

        paginatedGuiBuilder.withProvider(clazz, provider);
        return this;
    }

    public ConfigurationBackedGuiBuilder addItemOverride(String section, Consumer<InventoryClickEvent> event) {
        this.overrideItems.put(section, event);
        return this;
    }

    public List<String> ignoredKeys() {
        return this.ignoredKeys;
    }

    public Function<HashMapContext, Supplier<List<?>>> objectProvider() {
        return this.objectProvider;
    }

    public Consumer<InventoryCloseEvent> inventoryCloseListener() {
        return this.inventoryCloseListener;
    }

    public Consumer<InventoryOpenEvent> inventoryOpenListener() {
        return this.inventoryOpenListener;
    }

    public MapConfigurationGetter configuration() {
        return this.configuration;
    }

    public boolean supportsLegacy() {
        return this.legacySupport;
    }

    public GuiBuilder<?, ?> guiBuilder() {
        return this.builder;
    }

    public Map<String, Consumer<InventoryClickEvent>> overrideItems() {
        return this.overrideItems;
    }

    public Integer refreshInterval() {
        return this.refreshInterval;
    }

    public void refresh() {
        if (this.configuration instanceof YamlConfiguration<?> yamlConfiguration) {
            if (!YamlUtils.suggest(yamlConfiguration.path().toFile())) {
                return;
            }

            yamlConfiguration.load();
        }

        String title = this.configuration.getString("title");
        Integer rows = this.configuration.getInteger("rows");
        this.refreshInterval = this.configuration.getInteger("update-interval");
        String type = this.configuration.getString("type");
        InventoryType inventoryType = type == null ? InventoryType.CHEST : InventoryType.valueOf(type.toUpperCase(Locale.ENGLISH));

        if (title != null) {
            this.builder.title(ctx -> {
                Player player = ctx.get(GuiKeys.PLAYER);
                Gui gui = ctx.get(GuiKeys.GUI);
                return StringUtils.format(PlaceholderHandler.parseWithPlaceholderAPI(title, new PlaceholderParameters()
                        .withParameters(Player.class, player)
                        .withParameters(Gui.class, gui)
                ));
            });
        }

        this.builder.inventoryType(inventoryType);
        if (rows != null) {
            this.builder.rows(rows);
        }
    }

    public <T extends Gui> ConfigurationBackedGui<T> build() {
        this.refresh();
        return new ConfigurationBackedGui<>(this);
    }
}
