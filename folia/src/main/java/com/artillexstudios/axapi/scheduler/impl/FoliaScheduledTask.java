package com.artillexstudios.axapi.scheduler.impl;

import com.artillexstudios.axapi.scheduler.ScheduledTask;
import org.bukkit.plugin.Plugin;

public class FoliaScheduledTask implements ScheduledTask {
    private io.papermc.paper.threadedregions.scheduler.ScheduledTask task;

    public FoliaScheduledTask(io.papermc.paper.threadedregions.scheduler.ScheduledTask task) {
        this.task = task;
    }

    public static FoliaScheduledTask nullable(io.papermc.paper.threadedregions.scheduler.ScheduledTask task) {
        return task == null ? null : new FoliaScheduledTask(task);
    }

    @Override
    public Plugin getOwningPlugin() {
        return task.getOwningPlugin();
    }

    @Override
    public void cancel() {
        task.cancel();
    }

    @Override
    public boolean isCancelled() {
        io.papermc.paper.threadedregions.scheduler.ScheduledTask.ExecutionState state = task.getExecutionState();
        return state == io.papermc.paper.threadedregions.scheduler.ScheduledTask.ExecutionState.CANCELLED || state == io.papermc.paper.threadedregions.scheduler.ScheduledTask.ExecutionState.CANCELLED_RUNNING;
    }
}
