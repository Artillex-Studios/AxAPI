package com.artillexstudios.axapi.gui.inventory.renderer.strategy;

import com.artillexstudios.axapi.context.HashMapContext;
import com.artillexstudios.axapi.gui.inventory.BakedGuiItem;
import com.artillexstudios.axapi.gui.inventory.Gui;
import com.artillexstudios.axapi.scheduler.Scheduler;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Semaphore;

public class WaitingRenderStrategy implements RenderStrategy {

    @Override
    public void render(Gui gui, HashMapContext context, boolean newInventory, Int2ObjectMap<BakedGuiItem> items, Inventory inventory, Semaphore lock) {
        //noinspection unchecked
        CompletableFuture<BakedGuiItem>[] futures = new CompletableFuture[gui.getProviders().size()];
        Player player = gui.getRenderer().getPlayer();

        gui.getProviders().forEach((slot, provider) -> futures[slot] = provider.provide(context, gui.getModifiers()));
        CompletableFuture.allOf(futures).whenComplete((v, throwable) -> {
            for (int i = 0; i < futures.length; i++) {
                BakedGuiItem item = futures[i].join();
                BakedGuiItem previous = items.put(i, item);
                // Don't update it we already have it there
                if (previous != null && previous.stack().equals(item.stack())) {
                    continue;
                }

                inventory.setItem(i, item.stack());
            }

            if (newInventory || gui.getRenderer().isClosed()) {
                gui.getRenderer().setClosed(false);
                Scheduler.get().runAt(player.getLocation(), () -> {
                    player.openInventory(inventory);
                });
            }

            lock.release();
        });
    }
}
