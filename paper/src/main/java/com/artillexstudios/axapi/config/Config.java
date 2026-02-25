package com.artillexstudios.axapi.config;

import com.artillexstudios.axapi.utils.YamlUtils;
import dev.dejvokep.boostedyaml.YamlDocument;
import dev.dejvokep.boostedyaml.block.implementation.Section;
import dev.dejvokep.boostedyaml.settings.dumper.DumperSettings;
import dev.dejvokep.boostedyaml.settings.general.GeneralSettings;
import dev.dejvokep.boostedyaml.settings.loader.LoaderSettings;
import dev.dejvokep.boostedyaml.settings.updater.UpdaterSettings;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class Config {
    private YamlDocument configuration;

    public Config(File file) {
        this(file, null);
    }

    public Config(File file, InputStream defaults) {
        this(file, defaults, GeneralSettings.DEFAULT, LoaderSettings.DEFAULT, DumperSettings.DEFAULT, UpdaterSettings.DEFAULT);
    }

    public Config(File file, InputStream defaults, GeneralSettings generalSettings, LoaderSettings loaderSettings, DumperSettings dumperSettings, UpdaterSettings updaterSettings) {
        try {
            configuration = YamlDocument.create(file, defaults, generalSettings, loaderSettings, dumperSettings, updaterSettings);
        } catch (Exception exception) {
            YamlUtils.suggest(file);
        }
    }

    public <T> T get(String route) {
        return (T) configuration.get(route);
    }

    public <T> T get(String route, T def) {
        return (T) configuration.get(route, def);
    }

    public void set(String key, Object value) {
        configuration.set(key, value);
    }

    public void remove(String key) {
        configuration.remove(key);
    }

    public boolean getBoolean(String key) {
        return configuration.getBoolean(key);
    }

    public String getString(String key) {
        return configuration.getString(key);
    }

    public int getInt(String key) {
        return configuration.getInt(key);
    }

    public int getInt(String key, int def) {
        return configuration.getInt(key, def);
    }

    public long getLong(String key) {
        return configuration.getLong(key);
    }

    public long getLong(String key, long def) {
        return configuration.getLong(key, def);
    }

    public List<String> getStringList(String key) {
        return getList(key);
    }

    public List<String> getStringList(String key, List<String> def) {
        return getList(key, def);
    }

    public <T> List<T> getList(String key) {
        return (List<T>) configuration.getList(key);
    }

    public <T> List<T> getList(String key, List<T> def) {
        return (List<T>) configuration.getList(key, def);
    }

    public double getDouble(String key) {
        return configuration.getDouble(key);
    }

    public double getDouble(String key, double def) {
        return configuration.getDouble(key, def);
    }

    public float getDouble(String key, float def) {
        return configuration.getFloat(key, def);
    }

    public String getString(String key, String def) {
        return configuration.getString(key, def);
    }

    public boolean getBoolean(String key, boolean def) {
        return configuration.getBoolean(key, def);
    }

    public float getFloat(String key) {
        return configuration.getFloat(key);
    }

    public float getFloat(String key, float def) {
        return configuration.getFloat(key, def);
    }

    public <T, U> List<Map<T, U>> getMapList(String key) {
        List<Map<T, U>> listMap = new ArrayList<>();
        List<Map<?, ?>> list = configuration.getMapList(key);

        for (Map<?, ?> map : list) {
            HashMap<T, U> hashMap = new HashMap<>();
            map.forEach((k, v) -> hashMap.put((T) k, (U) v));

            listMap.add(hashMap);
        }

        return listMap;
    }

    public Section getSection(String key) {
        return configuration.getSection(key);
    }

    public <T> Optional<T> getOptional(String key) {
        return (Optional<T>) configuration.getOptional(key);
    }

    public Set<String> getKeys(boolean deep) {
        return configuration.getRoutesAsStrings(deep);
    }

    public boolean reload() {
        try {
            return configuration.reload();
        } catch (Exception e) {
            YamlUtils.suggest(configuration.getFile());
            return false;
        }
    }

    public void save() {
        try {
            configuration.save();
        } catch (IOException e) {
            YamlUtils.suggest(configuration.getFile());
        }
    }

    public YamlDocument getBackingDocument() {
        return this.configuration;
    }
}
