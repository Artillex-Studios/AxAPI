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
    public void runLaterAsync(Consumer<ScheduledTask> task, long ticksLater) {
        Bukkit.getScheduler().runTaskLaterAsynchronously(this.plugin, (a) -> task.accept(new BukkitScheduledTask(a)), ticksLater);
    }

    @Override
    public void runTimer(Consumer<ScheduledTask> task, long delay, long period) {
        Bukkit.getScheduler().runTaskTimer(this.plugin, (a) -> task.accept(new BukkitScheduledTask(a)), delay, period);
    }

    @Override
    public void runAsyncTimer(Consumer<ScheduledTask> task, long delay, long period) {
        Bukkit.getScheduler().runTaskTimerAsynchronously(this.plugin, (a) -> task.accept(new BukkitScheduledTask(a)), delay, period);
    }

    @Override
    public void runAt(Location location, Consumer<ScheduledTask> task) {
        Bukkit.getScheduler().runTask(this.plugin, (a) -> task.accept(new BukkitScheduledTask(a)));
    }

    @Override
    public void runTimerAt(Location location, Consumer<ScheduledTask> task, long delay, long period) {
        Bukkit.getScheduler().runTaskTimer(this.plugin, (a) -> task.accept(new BukkitScheduledTask(a)), delay, period);
    }

    @Override
    public void runLaterAt(Location location, Consumer<ScheduledTask> task, long ticksLater) {
        Bukkit.getScheduler().runTaskLater(this.plugin, (a) -> task.accept(new BukkitScheduledTask(a)), ticksLater);
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
    public void runLater(Entity entity, Consumer<ScheduledTask> task, Runnable retired, long delayTicks) {
        Bukkit.getScheduler().runTaskLater(this.plugin, (a) -> task.accept(new BukkitScheduledTask(a)), delayTicks);
    }

    @Override
    public void cancelAll() {
        Bukkit.getScheduler().cancelTasks(this.plugin);
    }
}
