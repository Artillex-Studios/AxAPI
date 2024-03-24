package com.artillexstudios.axapi.scheduler.impl;

import com.artillexstudios.axapi.scheduler.ScheduledTask;
import com.artillexstudios.axapi.scheduler.Scheduler;
import io.papermc.paper.threadedregions.scheduler.AsyncScheduler;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class FoliaScheduler implements Scheduler {
    private final JavaPlugin plugin;

    public FoliaScheduler(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run(Consumer<ScheduledTask> task) {
        Bukkit.getGlobalRegionScheduler().run(this.plugin, (a) -> task.accept(new FoliaScheduledTask(a)));
    }

    @Override
    public void execute(Runnable runnable) {
        Bukkit.getGlobalRegionScheduler().execute(this.plugin, runnable);
    }

    @Override
    public void runAsync(Runnable runnable) {
        Bukkit.getAsyncScheduler().runNow(this.plugin, (task) -> runnable.run());
    }

    @Override
    public void runLater(Consumer<ScheduledTask> task, long ticksLater) {
        Bukkit.getGlobalRegionScheduler().runDelayed(this.plugin, (a) -> task.accept(new FoliaScheduledTask(a)), ticksLater);
    }

    @Override
    public void runLaterAsync(Consumer<ScheduledTask> task, long ticksLater) {
        Bukkit.getAsyncScheduler().runDelayed(this.plugin, (a) -> task.accept(new FoliaScheduledTask(a)), ticksLater * 50, TimeUnit.MILLISECONDS);
    }

    @Override
    public void runTimer(Consumer<ScheduledTask> task, long delay, long period) {
        Bukkit.getGlobalRegionScheduler().runAtFixedRate(this.plugin, (a) -> task.accept(new FoliaScheduledTask(a)), delay, period);
    }

    @Override
    public void runAsyncTimer(Consumer<ScheduledTask> task, long delay, long period) {
        Bukkit.getAsyncScheduler().runAtFixedRate(this.plugin, (a) -> task.accept(new FoliaScheduledTask(a)), delay * 50, period * 50, TimeUnit.MILLISECONDS);
    }

    @Override
    public void runAt(Location location, Consumer<ScheduledTask> task) {
        Bukkit.getRegionScheduler().run(this.plugin, location, (a) -> task.accept(new FoliaScheduledTask(a)));
    }

    @Override
    public void runTimerAt(Location location, Consumer<ScheduledTask> task, long delay, long period) {
        Bukkit.getRegionScheduler().runAtFixedRate(this.plugin, location, (a) -> task.accept(new FoliaScheduledTask(a)), delay, period);
    }

    @Override
    public void runLaterAt(Location location, Consumer<ScheduledTask> task, long ticksLater) {
        Bukkit.getRegionScheduler().runDelayed(this.plugin, location, (a) -> task.accept(new FoliaScheduledTask(a)), ticksLater);
    }

    @Override
    public void executeAt(Location location, Runnable runnable) {
        Bukkit.getRegionScheduler().execute(this.plugin, location, runnable);
    }

    @Override
    public void run(Entity entity, Consumer<ScheduledTask> task, Runnable retired) {
        entity.getScheduler().run(this.plugin, (a) -> task.accept(new FoliaScheduledTask(a)), retired);
    }

    @Override
    public void execute(Entity entity, Runnable run, Runnable retired, long delay) {
        entity.getScheduler().execute(this.plugin, run, retired, delay);
    }

    @Override
    public void runTaskTimer(Entity entity, Consumer<ScheduledTask> task, Runnable retired, long initialDelayTicks, long periodTicks) {
        entity.getScheduler().runAtFixedRate(this.plugin, (a) -> task.accept(new FoliaScheduledTask(a)), retired, initialDelayTicks, periodTicks);
    }

    @Override
    public void runLater(Entity entity, Consumer<ScheduledTask> task, Runnable retired, long delayTicks) {
        entity.getScheduler().runDelayed(this.plugin, (a) -> task.accept(new FoliaScheduledTask(a)), retired, delayTicks);
    }

    @Override
    public void cancelAll() {
        Bukkit.getAsyncScheduler().cancelTasks(this.plugin);
        Bukkit.getGlobalRegionScheduler().cancelTasks(this.plugin);
    }
}
