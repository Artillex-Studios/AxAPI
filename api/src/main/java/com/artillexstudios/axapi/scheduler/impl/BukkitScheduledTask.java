package com.artillexstudios.axapi.scheduler.impl;

import com.artillexstudios.axapi.scheduler.ScheduledTask;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

public class BukkitScheduledTask implements ScheduledTask {
    private BukkitTask task;

    public BukkitScheduledTask(BukkitTask task) {
        this.task = task;
    }

    @Override
    public Plugin getOwningPlugin() {
        return task.getOwner();
    }

    @Override
    public void cancel() {
        task.cancel();
    }

    @Override
    public boolean isCancelled() {
        return task.isCancelled();
    }
}
