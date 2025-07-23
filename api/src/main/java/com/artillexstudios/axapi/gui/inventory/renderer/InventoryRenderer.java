package com.artillexstudios.axapi.gui.inventory.renderer;

import com.artillexstudios.axapi.context.HashMapContext;
import com.artillexstudios.axapi.gui.inventory.BakedGuiItem;
import com.artillexstudios.axapi.gui.inventory.Gui;
import com.artillexstudios.axapi.gui.inventory.GuiKeys;
import com.artillexstudios.axapi.gui.inventory.renderer.strategy.RenderStrategy;
import com.artillexstudios.axapi.gui.inventory.renderer.strategy.StreamingRenderStrategy;
import com.artillexstudios.axapi.gui.inventory.renderer.strategy.WaitingRenderStrategy;
import com.artillexstudios.axapi.nms.NMSHandlers;
import com.artillexstudios.axapi.utils.StringUtils;
import com.artillexstudios.axapi.utils.featureflags.FeatureFlags;
import com.artillexstudios.axapi.utils.logging.LogUtils;
import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMaps;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.Blocking;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.Semaphore;

public class InventoryRenderer implements InventoryHolder {
    private final Semaphore renderLock = new Semaphore(1, true);
    private final Int2ObjectMap<BakedGuiItem> items = Int2ObjectMaps.synchronize(new Int2ObjectArrayMap<>());
    private final Player player;
    private long lastClick;
    private boolean closed = true;
    private Inventory inventory;
    private Gui currentGui;

    public InventoryRenderer(Player player) {
        this.player = player;
    }

    public void queue(Gui gui) {
        if (FeatureFlags.DEBUG.get()) {
            LogUtils.debug("Enqueued!");
        }

        Thread.ofVirtual().start(() -> {
            if (FeatureFlags.DEBUG.get()) {
                LogUtils.debug("Running from thread!");
            }
            this.render(gui);
        });
    }

    @Blocking
    public void render(Gui gui) {
        if (this.currentGui == gui && !this.renderLock.tryAcquire()) {
            return;
        }

        if (this.currentGui != gui) {
            this.renderLock.acquireUninterruptibly();
        }

        HashMapContext context = this.createContext(gui);
        boolean newInventory = this.buildInventory(gui, context);
        this.currentGui = gui;
        RenderStrategy strategy = this.getStrategy();
        strategy.render(gui, context, newInventory, this.items, this.inventory, this.renderLock);
    }

    private RenderStrategy getStrategy() {
        return FeatureFlags.GUI_WAIT_FOR_ALL.get() ? new WaitingRenderStrategy() : new StreamingRenderStrategy();
    }

    private HashMapContext createContext(Gui gui) {
        return new HashMapContext()
                .with(GuiKeys.PLAYER, this.player)
                .with(GuiKeys.GUI, gui)
                .merge(gui.getContext());
    }

    public void onTitleUpdate(HashMapContext context) {
        NMSHandlers.getNmsHandler().setTitle(this.inventory, this.currentGui.provideTitle(context));
    }

    public boolean buildInventory(Gui gui, HashMapContext context) {
        if (this.currentGui == null || this.currentGui.getType() != gui.getType() || this.currentGui.getRows() != gui.getRows()) {
            this.recreateGui(gui, context);
            return true;
        }

        return false;
    }

    private void recreateGui(Gui gui, HashMapContext context) {
        if (gui.getType() == InventoryType.CHEST) {
            this.inventory = Bukkit.createInventory(this, gui.getRows() * 9, StringUtils.formatToString(MiniMessage.miniMessage().serialize(gui.provideTitle(context))));
        } else {
            this.inventory = Bukkit.createInventory(this, gui.getType(), StringUtils.formatToString(MiniMessage.miniMessage().serialize(gui.provideTitle(context))));
        }
    }

    public void handleClick(InventoryClickEvent event) {
        if (this.currentGui.isDisableAllInteractions()) {
            event.setCancelled(true);
        }

        long now = System.currentTimeMillis();
        if (now - this.lastClick < FeatureFlags.INVENTORY_CLICK_COOLDOWN.get()) {
            return;
        }

        this.lastClick = now;
        if (event.getClickedInventory() != null && event.getClickedInventory().getType() != InventoryType.PLAYER) {
            this.items.get(event.getRawSlot()).eventConsumer()
                    .accept(event);
        } else {
            this.currentGui.getPlayerInventoryClickListener().accept(event);
        }
    }

    public Gui getCurrentGui() {
        return this.currentGui;
    }

    public boolean isClosed() {
        return this.closed;
    }

    public void setClosed(boolean closed) {
        this.closed = closed;
    }

    public void handleDrag(InventoryDragEvent event) {
        // TODO: Handle drag
        if (this.currentGui.isDisableAllInteractions()) {
            event.setCancelled(true);
        }
    }

    public void handleClose(InventoryCloseEvent event) {
        this.closed = true;
        this.currentGui.getInventoryCloseListener().accept(event);
    }

    public void handleOpen(InventoryOpenEvent event) {
        this.currentGui.getInventoryOpenListener().accept(event);
    }

    public Player getPlayer() {
        return this.player;
    }

    @NotNull
    @Override
    public Inventory getInventory() {
        return this.inventory;
    }
}
