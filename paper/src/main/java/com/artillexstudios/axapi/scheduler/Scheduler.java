package com.artillexstudios.axapi.scheduler;

import com.artillexstudios.axapi.scheduler.impl.SchedulerHandler;
import com.artillexstudios.axapi.utils.PaperUtils;
import org.bukkit.Location;
import org.bukkit.entity.Entity;

import java.util.function.Consumer;

public interface Scheduler {
    SchedulerHandler scheduler = new SchedulerHandler();

    static Scheduler get() {
        return scheduler.getScheduler();
    }

    void run(Consumer<ScheduledTask> task);

    ScheduledTask run(Runnable task);

    void execute(Runnable runnable);

    void runAsync(Runnable runnable);

    void runLater(Consumer<ScheduledTask> task, long ticksLater);

    ScheduledTask runLater(Runnable task, long ticksLater);

    void runLaterAsync(Consumer<ScheduledTask> task, long ticksLater);

    ScheduledTask runLaterAsync(Runnable task, long ticksLater);

    void runTimer(Consumer<ScheduledTask> task, long delay, long period);

    ScheduledTask runTimer(Runnable task, long delay, long period);

    void runAsyncTimer(Consumer<ScheduledTask> task, long delay, long period);

    ScheduledTask runAsyncTimer(Runnable task, long delay, long period);

    void runAt(Location location, Consumer<ScheduledTask> task);

    ScheduledTask runAt(Location location, Runnable task);

    void runTimerAt(Location location, Consumer<ScheduledTask> task, long delay, long period);

    ScheduledTask runTimerAt(Location location, Runnable task, long delay, long period);

    void runLaterAt(Location location, Consumer<ScheduledTask> task, long ticksLater);

    ScheduledTask runLaterAt(Location location, Runnable task, long ticksLater);

    void executeAt(Location location, Runnable runnable);

    void run(Entity entity, Consumer<ScheduledTask> task, Runnable retired);

    void execute(Entity entity, Runnable run, Runnable retired, long delay);

    void runTaskTimer(Entity entity, Consumer<ScheduledTask> task, Runnable retired, long initialDelayTicks, long periodTicks);

    ScheduledTask runTaskTimer(Entity entity, Runnable task, Runnable retired, long initialDelayTicks, long periodTicks);

    void runLater(Entity entity, Consumer<ScheduledTask> task, Runnable retired, long delayTicks);

    ScheduledTask runLater(Entity entity, Runnable task, Runnable retired, long delayTicks);

    /**
     * Run a task on the thread of the entity. This method runs the Runnable if this is called from the correct thread
     * or schedules it if not run from the entities thread.
     * @param entity The entity to run the task in relation to.
     * @param task The task to run.
     */
    default void runWith(Entity entity, Runnable task) {
        if ((PaperUtils.isFolia() && this.isOwnedByCurrentRegion(entity.getLocation())) || (!PaperUtils.isFolia() && this.isGlobalTickThread())) {
            task.run();
        } else {
            this.run(entity, t -> task.run(), () -> {});
        }
    }

    /**
     * If the calling thread is the Global tick thread on Folia, or the main thread on Bukkit servers,
     * the task is run, from other threads the task is scheduled.
     * @param task The task to run.
     */
    default void runGlobal(Runnable task) {
        if (this.isGlobalTickThread()) {
            task.run();
        } else {
            this.run(task);
        }
    }

    boolean isOwnedByCurrentRegion(Location location);

    boolean isGlobalTickThread();

    void cancelAll();

    boolean isRegionThread();
}
