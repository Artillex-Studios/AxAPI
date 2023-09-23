package com.artillexstudios.axapi;

import com.artillexstudios.axapi.scheduler.Scheduler;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class AxPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        enable();
    }

    public void enable() {

    }

    @Override
    public void onLoad() {
        Scheduler.scheduler.init(this);

        load();
    }

    public void load() {

    }

    @Override
    public void onDisable() {
        disable();
    }

    public void disable() {

    }

    public void reload() {

    }

    public long reloadWithTime() {
        long start = System.currentTimeMillis();
        reload();

        return System.currentTimeMillis() - start;
    }
}