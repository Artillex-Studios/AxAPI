package com.artillexstudios.axapi.scheduler.impl;

import com.artillexstudios.axapi.scheduler.ScheduledTask;
import com.artillexstudios.axapi.scheduler.Scheduler;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.function.Consumer;

public class BukkitScheduler implements Scheduler {
    private final JavaPlugin plugin;

    public BukkitScheduler(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run(Consumer<ScheduledTask> task) {
        Bukkit.getScheduler().runTask(this.plugin, (a) -> task.accept(new BukkitScheduledTask(a)));
    }

    @Override
    public ScheduledTask run(Runnable task) {
        return new BukkitScheduledTask(Bukkit.getScheduler().runTask(this.plugin, task));
    }

    @Override
    public void execute(Runnable runnable) {
        runnable.run();
    }

    @Override
    public void runAsync(Runnable runnable) {
        Bukkit.getScheduler().runTaskAsynchronously(this.plugin, runnable);
    }

    @Override
    public void runLater(Consumer<ScheduledTask> task, long ticksLater) {
        Bukkit.getScheduler().runTaskLater(this.plugin, (a) -> task.accept(new BukkitScheduledTask(a)), ticksLater);
    }

    @Override
    public ScheduledTask runLater(Runnable task, long ticksLater) {
        return new BukkitScheduledTask(Bukkit.getScheduler().runTaskLater(this.plugin, task, ticksLater));
    }

    @Override
    public void runLaterAsync(Consumer<ScheduledTask> task, long ticksLater) {
        Bukkit.getScheduler().runTaskLaterAsynchronously(this.plugin, (a) -> task.accept(new BukkitScheduledTask(a)), ticksLater);
    }

    @Override
    public ScheduledTask runLaterAsync(Runnable task, long ticksLater) {
        return new BukkitScheduledTask(Bukkit.getScheduler().runTaskLaterAsynchronously(this.plugin, task, ticksLater));
    }

    @Override
    public void runTimer(Consumer<ScheduledTask> task, long delay, long period) {
        Bukkit.getScheduler().runTaskTimer(this.plugin, (a) -> task.accept(new BukkitScheduledTask(a)), delay, period);
    }

    @Override
    public ScheduledTask runTimer(Runnable task, long delay, long period) {
        return new BukkitScheduledTask(Bukkit.getScheduler().runTaskTimer(this.plugin, task, delay, period));
    }

    @Override
    public void runAsyncTimer(Consumer<ScheduledTask> task, long delay, long period) {
        Bukkit.getScheduler().runTaskTimerAsynchronously(this.plugin, (a) -> task.accept(new BukkitScheduledTask(a)), delay, period);
    }

    @Override
    public ScheduledTask runAsyncTimer(Runnable task, long delay, long period) {
        return new BukkitScheduledTask(Bukkit.getScheduler().runTaskTimerAsynchronously(this.plugin, task, delay, period));
    }

    @Override
    public void runAt(Location location, Consumer<ScheduledTask> task) {
        Bukkit.getScheduler().runTask(this.plugin, (a) -> task.accept(new BukkitScheduledTask(a)));
    }

    @Override
    public ScheduledTask runAt(Location location, Runnable task) {
        return new BukkitScheduledTask(Bukkit.getScheduler().runTask(this.plugin, task));
    }

    @Override
    public void runTimerAt(Location location, Consumer<ScheduledTask> task, long delay, long period) {
        Bukkit.getScheduler().runTaskTimer(this.plugin, (a) -> task.accept(new BukkitScheduledTask(a)), delay, period);
    }

    @Override
    public ScheduledTask runTimerAt(Location location, Runnable task, long delay, long period) {
        return new BukkitScheduledTask(Bukkit.getScheduler().runTaskTimer(this.plugin, task, delay, period));
    }

    @Override
    public void runLaterAt(Location location, Consumer<ScheduledTask> task, long ticksLater) {
        Bukkit.getScheduler().runTaskLater(this.plugin, (a) -> task.accept(new BukkitScheduledTask(a)), ticksLater);
    }

    @Override
    public ScheduledTask runLaterAt(Location location, Runnable task, long ticksLater) {
        return new BukkitScheduledTask(Bukkit.getScheduler().runTaskLater(this.plugin, task, ticksLater));
    }

    @Override
    public void executeAt(Location location, Runnable runnable) {
        runnable.run();
    }

    @Override
    public void run(Entity entity, Consumer<ScheduledTask> task, Runnable retired) {
        Bukkit.getScheduler().runTask(this.plugin, (a) -> task.accept(new BukkitScheduledTask(a)));
    }

    @Override
    public void execute(Entity entity, Runnable run, Runnable retired, long delay) {
        run.run();
    }

    @Override
    public void runTaskTimer(Entity entity, Consumer<ScheduledTask> task, Runnable retired, long initialDelayTicks, long periodTicks) {
        Bukkit.getScheduler().runTaskTimer(this.plugin, (a) -> task.accept(new BukkitScheduledTask(a)), initialDelayTicks, periodTicks);
    }

    @Override
    public ScheduledTask runTaskTimer(Entity entity, Runnable task, Runnable retired, long initialDelayTicks, long periodTicks) {
        return new BukkitScheduledTask(Bukkit.getScheduler().runTaskTimer(this.plugin, task, initialDelayTicks, periodTicks));
    }

    @Override
    public void runLater(Entity entity, Consumer<ScheduledTask> task, Runnable retired, long delayTicks) {
        Bukkit.getScheduler().runTaskLater(this.plugin, (a) -> task.accept(new BukkitScheduledTask(a)), delayTicks);
    }

    @Override
    public ScheduledTask runLater(Entity entity, Runnable task, Runnable retired, long delayTicks) {
        return new BukkitScheduledTask(Bukkit.getScheduler().runTaskLater(this.plugin, task, delayTicks));
    }

    @Override
    public boolean isOwnedByCurrentRegion(Location location) {
        return true;
    }

    @Override
    public void cancelAll() {
        Bukkit.getScheduler().cancelTasks(this.plugin);
    }
}
