package com.artillexstudios.axapi.updatechecker;

import com.artillexstudios.axapi.AxPlugin;
import com.artillexstudios.axapi.scheduler.ScheduledTask;
import com.artillexstudios.axapi.scheduler.Scheduler;
import com.artillexstudios.axapi.updatechecker.sources.UpdateCheckSource;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.time.Duration;
import java.util.function.BiConsumer;
import java.util.function.BooleanSupplier;

public final class UpdateChecker {
    private final UpdateCheckSource source;
    private final ArtifactVersion current;
    private ScheduledTask task;
    private String permission = "";
    private BooleanSupplier enabled = () -> false;
    private UpdateCheck lastCheck = null;
    private long timeOfLastCheck = 0;
    private Duration timeBetweenChecks = Duration.ofMinutes(5);
    private BiConsumer<CommandSender, UpdateCheck> consumer = (senders, check) -> {

    };

    public UpdateChecker(UpdateCheckSource source) {
        this.source = source;
        this.current = new ArtifactVersion(AxPlugin.getPlugin(AxPlugin.class).getDescription().getVersion());
    }

    public UpdateChecker onCheck(BiConsumer<CommandSender, UpdateCheck> consumer) {
        this.consumer = consumer;
        return this;
    }

    public UpdateChecker timeBetweenChecks(Duration duration) {
        this.timeBetweenChecks = duration;
        return this;
    }

    public UpdateChecker register(String permission, BooleanSupplier enabled) {
        this.permission = permission;
        this.enabled = enabled;

        Bukkit.getPluginManager().registerEvents(new Listener() {

            @EventHandler
            public void onPlayerJoinEvent(PlayerJoinEvent event) {
                if (!UpdateChecker.this.enabled.getAsBoolean()) {
                    return;
                }

                if (!event.getPlayer().hasPermission(UpdateChecker.this.permission)) {
                    return;
                }

                UpdateChecker.this.check(event.getPlayer());
            }
        }, AxPlugin.getPlugin(AxPlugin.class));
        return this;
    }

    public UpdateChecker check(CommandSender... sender) {
        Scheduler.get().runAsync(() -> {
            UpdateCheck check = UpdateChecker.this.lastCheck;
            if (check == null || timeOfLastCheck + timeBetweenChecks.toMillis() <= System.currentTimeMillis()) {
                check = this.source.check(this.current);
                UpdateChecker.this.lastCheck = check;
            }

            for (CommandSender commandSender : sender) {
                this.consumer.accept(commandSender, check);
            }
        });
        return this;
    }

    public UpdateChecker checkEvery(double hours) {
        if (this.task != null) {
            task.cancel();
        }

        double minutes = 60 * hours;
        double seconds = 60 * minutes;
        long ticks = (long) (20L * seconds);
        this.task = Scheduler.get().runAsyncTimer(() -> {
            this.check(Bukkit.getConsoleSender());
        }, ticks, ticks);

        return this;
    }

    public ArtifactVersion current() {
        return this.current;
    }
}
