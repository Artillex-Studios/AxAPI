package com.artillexstudios.axapi.scheduler;

import com.artillexstudios.axapi.scheduler.impl.SchedulerHandler;
import org.bukkit.Location;
import org.bukkit.entity.Entity;

import java.util.function.Consumer;

public interface Scheduler {
    SchedulerHandler scheduler = new SchedulerHandler();

    static Scheduler get() {
        return scheduler.getScheduler();
    }

    void run(Consumer<ScheduledTask> task);

    void execute(Runnable runnable);

    void runAsync(Runnable runnable);

    void runLater(Consumer<ScheduledTask> task, long ticksLater);

    void runLaterAsync(Consumer<ScheduledTask> task, long ticksLater);

    void runTimer(Consumer<ScheduledTask> task, long delay, long period);

    void runAsyncTimer(Consumer<ScheduledTask> task, long delay, long period);

    void runAt(Location location, Consumer<ScheduledTask> task);

    void runTimerAt(Location location, Consumer<ScheduledTask> task, long delay, long period);

    void runLaterAt(Location location, Consumer<ScheduledTask> task, long ticksLater);

    void executeAt(Location location, Runnable runnable);

    void run(Entity entity, Consumer<ScheduledTask> task, Runnable retired);

    void execute(Entity entity, Runnable run, Runnable retired, long delay);

    void runTaskTimer(Entity entity, Consumer<ScheduledTask> task, Runnable retired, long initialDelayTicks, long periodTicks);

    void runLater(Entity entity, Consumer<ScheduledTask> task, Runnable retired, long delayTicks);

    void cancelAll();
}
