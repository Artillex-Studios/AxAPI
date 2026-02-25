package com.artillexstudios.axapi.gui.inventory.renderer.strategy;

import com.artillexstudios.axapi.context.HashMapContext;
import com.artillexstudios.axapi.gui.inventory.BakedGuiItem;
import com.artillexstudios.axapi.gui.inventory.Gui;
import com.artillexstudios.axapi.scheduler.Scheduler;
import com.artillexstudios.axapi.utils.logging.LogUtils;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Semaphore;

public class StreamingRenderStrategy implements RenderStrategy {

    @Override
    public void render(Gui gui, HashMapContext context, boolean newInventory, Int2ObjectMap<BakedGuiItem> items, Inventory inventory, Semaphore lock) {
        //noinspection unchecked
        CompletableFuture<BakedGuiItem>[] futures = new CompletableFuture[gui.getProviders().size()];
        Player player = gui.getRenderer().getPlayer();

        if (newInventory || gui.getRenderer().isClosed()) {
            gui.getRenderer().setClosed(false);
            Scheduler.get().runAt(player.getLocation(), () -> {
                player.openInventory(inventory);
            });
        }

        gui.getProviders().forEach((slot, provider) -> {
            CompletableFuture<BakedGuiItem> provide = provider.provide(context, gui.getModifiers());
            futures[slot] = provide;
            provide.thenAccept(item -> {
                BakedGuiItem previous = items.put(slot.intValue(), item);
                // Don't update it we already have it there
                if (previous != null && previous.stack().equals(item.stack())) {
                    return;
                }

                inventory.setItem(slot, item.stack());
            });
        });
        CompletableFuture.allOf(futures).whenComplete((v, throwable) -> {
            if (throwable != null) {
                LogUtils.error("An exception occurred");
            }

            lock.release();
        });
    }
}
