package com.artillexstudios.axapi.gui.inventory;

import com.artillexstudios.axapi.context.ContextKey;
import com.artillexstudios.axapi.context.HashMapContext;
import com.artillexstudios.axapi.gui.inventory.provider.GuiItemProvider;
import com.artillexstudios.axapi.gui.inventory.provider.implementation.CachingGuiItemProvider;
import com.artillexstudios.axapi.gui.inventory.provider.implementation.EmptyGuiItemProvider;
import com.artillexstudios.axapi.scheduler.Scheduler;
import com.artillexstudios.axapi.utils.PaperUtils;
import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;

import java.util.function.Function;

public abstract class Gui {
    protected final Player player;
    protected final InventoryRenderer renderer;
    protected final Int2ObjectArrayMap<GuiItemProvider> providers = new Int2ObjectArrayMap<>();
    protected final InventoryType type;
    protected boolean disableAllInteractions;
    protected Function<HashMapContext, Component> titleProvider;
    protected int rows;
    protected int size;

    public Gui(Player player, Function<HashMapContext, Component> titleProvider, InventoryType type, int rows) {
        this.player = player;
        this.renderer = InventoryRenderers.getRenderer(this.player);
        this.titleProvider = titleProvider;
        this.type = type;
        this.rows = rows;

        this.size = rows * 9;
        if (this.type != InventoryType.CHEST) {
            this.size = this.type.getDefaultSize();
        }

        for (int i = 0; i < this.size; i++) {
            this.providers.put(i, EmptyGuiItemProvider.INSTANCE);
        }
    }

    public void disableAllInteractions() {
        this.disableAllInteractions = true;
    }

    public boolean isDisableAllInteractions() {
        return this.disableAllInteractions;
    }

    public void setItem(int slot, GuiItem item) {
        this.setItem(slot, item, CachingGuiItemProvider::new);
    }

    public void setItem(int slot, GuiItem item, Function<GuiItem, GuiItemProvider> provider) {
        this.providers.put(slot, provider.apply(item));
    }

    public void setItem(int slot, GuiItemProvider provider) {
        this.providers.put(slot, provider);
    }

    public int rows() {
        return this.rows;
    }

    public Component provideTitle(HashMapContext context) {
        return this.titleProvider.apply(context);
    }

    public InventoryType type() {
        return this.type;
    }

    public Int2ObjectArrayMap<GuiItemProvider> providers() {
        return this.providers;
    }

    public void updateTitle() {
        this.renderer.onTitleUpdate(new HashMapContext().with(ContextKey.of("player", Player.class), this.player));
    }

    public void open() {
        if (Scheduler.get().isGlobalTickThread() || (PaperUtils.isFolia() && Scheduler.get().isOwnedByCurrentRegion(this.player.getLocation()))) {
            this.renderer.queue(this);
        } else {
            this.renderer.render(this);
        }
    }
}
