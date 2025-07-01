package com.artillexstudios.axapi.gui.inventory;

import com.artillexstudios.axapi.context.ContextKey;
import com.artillexstudios.axapi.context.HashMapContext;
import com.artillexstudios.axapi.nms.NMSHandlers;
import com.artillexstudios.axapi.utils.StringUtils;
import com.artillexstudios.axapi.utils.featureflags.FeatureFlags;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.Blocking;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Semaphore;

public class InventoryRenderer implements InventoryHolder {
    private final Semaphore renderLock = new Semaphore(1, true);
    private final Player player;
    private Inventory inventory;
    private Gui currentGui;

    public InventoryRenderer(Player player) {
        this.player = player;
    }

    @Blocking
    public void render(Gui gui) {
        // If we are currently rendering the same gui, we can safely return
        if (this.currentGui == gui) {
            if (!this.renderLock.tryAcquire()) {
                return;
            }
        } else {
            this.renderLock.acquireUninterruptibly();
        }

        this.currentGui = gui;
        this.buildInventory(gui);

        HashMapContext context = new HashMapContext()
                .with(ContextKey.of("player", Player.class), this.player);

        CompletableFuture<BakedGuiItem>[] futures = new CompletableFuture[gui.providers().size()];
        if (FeatureFlags.GUI_WAIT_FOR_ALL.get()) {
            gui.providers().forEach((slot, provider) -> {
                futures[slot] = provider.provide(context);
            });

            CompletableFuture.allOf(futures).whenComplete((v, throwable) -> {
                for (int i = 0; i < futures.length; i++) {
                    BakedGuiItem item = futures[i].join();
                    this.inventory.setItem(i, item.stack());
                }
                this.renderLock.release();
            });
        } else {
            gui.providers().forEach((slot, provider) -> {
                CompletableFuture<BakedGuiItem> provide = provider.provide(context);
                futures[slot] = provide;
                provide.thenAccept(item -> {
                    this.inventory.setItem(slot, item.stack());
                });
            });
            CompletableFuture.allOf(futures).whenComplete((v, throwable) -> {
                this.renderLock.release();
            });
        }
    }

    public void onTitleUpdate() {
        NMSHandlers.getNmsHandler().setTitle(this.inventory, this.currentGui.title());
    }

    public void buildInventory(Gui gui) {
        if (this.currentGui == null) {
            this.recreateGui(gui);
            return;
        }

        if (this.currentGui.type() != gui.type()) {
            this.recreateGui(gui);
            return;
        }

        if (this.currentGui.rows() != gui.rows()) {
            this.recreateGui(gui);
        }
    }

    private void recreateGui(Gui gui) {
        if (gui.type() == InventoryType.CHEST) {
            this.inventory = Bukkit.createInventory(this, gui.rows() * 9, StringUtils.formatToString(MiniMessage.miniMessage().serialize(gui.title())));
        } else {
            this.inventory = Bukkit.createInventory(this, gui.type(), StringUtils.formatToString(MiniMessage.miniMessage().serialize(gui.title())));
        }
    }

    @NotNull
    @Override
    public Inventory getInventory() {
        return this.inventory;
    }
}
