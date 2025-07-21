package com.artillexstudios.axapi.gui.inventory;

import com.artillexstudios.axapi.AxPlugin;
import com.artillexstudios.axapi.executor.ExceptionReportingScheduledThreadPool;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public enum InventoryUpdater {
    INSTANCE;
    private ScheduledExecutorService service;

    public void start(AxPlugin plugin) {
        this.service = new ExceptionReportingScheduledThreadPool(1, Thread.ofPlatform().name(plugin.getName() + "-AxAPI-inventory-updater")
                .factory()
        );

        this.service.scheduleWithFixedDelay(() -> {
            for (InventoryRenderer renderer : InventoryRenderers.getCurrentRenderers()) {
                Gui gui = renderer.currentGui();
                if (gui == null || renderer.closed()) {
                    continue;
                }

                gui.tick();
            }
        }, 0, 50L, TimeUnit.MILLISECONDS);
    }

    public void shutdown() {
        if (this.service == null || this.service.isShutdown()) {
            return;
        }

        this.service.shutdown();
    }
}
