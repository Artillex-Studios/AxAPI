package com.artillexstudios.axapi.gui.inventory;

import com.artillexstudios.axapi.context.HashMapContext;
import com.artillexstudios.axapi.nms.NMSHandlers;
import com.artillexstudios.axapi.scheduler.Scheduler;
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
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.Blocking;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Semaphore;

public class InventoryRenderer implements InventoryHolder {
    private final Semaphore renderLock = new Semaphore(1, true);
    private final Int2ObjectMap<BakedGuiItem> items = Int2ObjectMaps.synchronize(new Int2ObjectArrayMap<>());
    private final Player player;
    private boolean closed = true;
    private Inventory inventory;
    private Gui currentGui;

    public InventoryRenderer(Player player) {
        this.player = player;
    }

    public void queue(Gui gui) {
        LogUtils.debug("Enqueued!");
        Thread.ofVirtual().start(() -> {
            LogUtils.debug("Running from thread!");
            this.render(gui);
        });
    }

    @Blocking
    public void render(Gui gui) {
        LogUtils.debug("Rendering");
        // If we are currently rendering the same gui, we can safely return
        if (this.currentGui == gui) {
            LogUtils.debug("Same gui");
            if (!this.renderLock.tryAcquire()) {
                LogUtils.debug("Couldn't acquire lock");
                return;
            }
        } else {
            LogUtils.debug("Acquiring lock");
            this.renderLock.acquireUninterruptibly();
            LogUtils.debug("Lock acquired");
        }

        HashMapContext context = new HashMapContext()
                .with(GuiKeys.PLAYER, this.player)
                .with(GuiKeys.GUI, gui);
        LogUtils.debug("Setting current gui");

        boolean newInventory = this.buildInventory(gui, context);
        this.currentGui = gui;

        LogUtils.debug("Built new inventory? {}, inv: {}", newInventory, this.inventory);
        CompletableFuture<BakedGuiItem>[] futures = new CompletableFuture[gui.providers().size()];
        if (FeatureFlags.GUI_WAIT_FOR_ALL.get()) {
            LogUtils.debug("Waiting for all");
            gui.providers().forEach((slot, provider) -> {
                futures[slot] = provider.provide(context);
            });

            CompletableFuture.allOf(futures).whenComplete((v, throwable) -> {
                for (int i = 0; i < futures.length; i++) {
                    BakedGuiItem item = futures[i].join();
                    BakedGuiItem previous = this.items.put(i, item);
                    // Don't update it we already have it there
                    if (previous != null && previous.stack().equals(item.stack())) {
                        continue;
                    }

                    this.inventory.setItem(i, item.stack());
                }

                if (newInventory || this.closed) {
                    this.closed = false;
                    Scheduler.get().runAt(this.player.getLocation(), () -> {
                        this.player.openInventory(this.inventory);
                    });
                }

                this.renderLock.release();
            });
        } else {
            LogUtils.debug("Not waiting, closed: {}", this.closed);
            if (newInventory || this.closed) {
                this.closed = false;
                LogUtils.debug("New inventory");
                Scheduler.get().runAt(this.player.getLocation(), () -> {
                    LogUtils.debug("Opening");
                    this.player.openInventory(this.inventory);
                });
            }

            gui.providers().forEach((slot, provider) -> {
                CompletableFuture<BakedGuiItem> provide = provider.provide(context);
                LogUtils.debug("provide! slot: {}, provider: {}", slot, provider);
                futures[slot] = provide;
                provide.thenAccept(item -> {
                    LogUtils.debug("Adding to items");
                    BakedGuiItem previous = this.items.put(slot.intValue(), item);
                    // Don't update it we already have it there
                    if (previous != null && previous.stack().equals(item.stack())) {
                        LogUtils.debug("Didn't set");
                        return;
                    }

                    LogUtils.debug("Inventory setItem");
                    this.inventory.setItem(slot, item.stack());
                });
            });
            CompletableFuture.allOf(futures).whenComplete((v, throwable) -> {
                if (throwable != null) {
                    LogUtils.error("An exception occurred");
                }

                LogUtils.debug("Lock release");
                this.renderLock.release();
            });
        }
    }

    public void onTitleUpdate(HashMapContext context) {
        NMSHandlers.getNmsHandler().setTitle(this.inventory, this.currentGui.provideTitle(context));
    }

    public boolean buildInventory(Gui gui, HashMapContext context) {
        if (this.currentGui == null) {
            this.recreateGui(gui, context);
            return true;
        }

        if (this.currentGui.type() != gui.type()) {
            this.recreateGui(gui, context);
            return true;
        }

        if (this.currentGui.rows() != gui.rows()) {
            this.recreateGui(gui, context);
            return true;
        }

        return false;
    }

    private void recreateGui(Gui gui, HashMapContext context) {
        if (gui.type() == InventoryType.CHEST) {
            this.inventory = Bukkit.createInventory(this, gui.rows() * 9, StringUtils.formatToString(MiniMessage.miniMessage().serialize(gui.provideTitle(context))));
        } else {
            this.inventory = Bukkit.createInventory(this, gui.type(), StringUtils.formatToString(MiniMessage.miniMessage().serialize(gui.provideTitle(context))));
        }
    }

    public void handleClick(InventoryClickEvent event) {
        if (this.currentGui.isDisableAllInteractions()) {
            event.setCancelled(true);
        }

        this.items.get(event.getRawSlot()).eventConsumer().accept(event);
    }

    public void handleDrag(InventoryDragEvent event) {
        // TODO: Handle drag
        if (this.currentGui.isDisableAllInteractions()) {
            event.setCancelled(true);
        }
    }

    public void handleClose(InventoryCloseEvent event) {
        // TODO: Handle close
        this.closed = true;
    }

    @NotNull
    @Override
    public Inventory getInventory() {
        return this.inventory;
    }
}
