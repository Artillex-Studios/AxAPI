package com.artillexstudios.axapi.config;

import com.artillexstudios.axapi.config.adapters.TypeAdapter;
import com.artillexstudios.axapi.config.adapters.TypeAdapterHolder;
import com.artillexstudios.axapi.config.annotation.ConfigurationPart;
import com.artillexstudios.axapi.config.annotation.Named;
import com.artillexstudios.axapi.config.annotation.PostProcess;
import com.artillexstudios.axapi.config.renamer.KeyRenamer;
import com.artillexstudios.axapi.config.renamer.LowerKebabCaseRenamer;
import com.artillexstudios.axapi.utils.LogUtils;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.ints.IntIntPair;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.representer.Representer;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Consumer;

public final class YamlConfiguration {
    private final TypeAdapterHolder holder = new TypeAdapterHolder();
    private final Map<IntIntPair, ConfigurationUpdater> updaters;
    private final Path path;
    private final InputStream defaults;
    private final Class<? extends ConfigurationPart> clazz;
    private final Yaml yaml;
    private final int configVersion;
    private final String configVersionPath;
    private final KeyRenamer keyRenamer;
    private LinkedHashMap<String, Object> config = null;
    private boolean needsSaving = false;

    YamlConfiguration(Path path, InputStream defaults, Class<? extends ConfigurationPart> clazz, DumperOptions dumperOptions, LoaderOptions loaderOptions, Map<IntIntPair, ConfigurationUpdater> updaters, int configVersion, String configVersionPath, Map<Class<?>, TypeAdapter<?, ?>> adapters, KeyRenamer keyRenamer) {
        this.path = path;
        this.defaults = defaults;
        this.clazz = clazz;
        this.updaters = updaters;
        this.configVersion = configVersion;
        this.configVersionPath = configVersionPath;
        this.keyRenamer = keyRenamer;
        this.holder.registerAdapters(adapters);

        this.yaml = new Yaml(new Constructor(loaderOptions), new Representer(dumperOptions), dumperOptions, loaderOptions);
    }

    public static YamlConfiguration.Builder of(Path path, Class<? extends ConfigurationPart> clazz) {
        return new Builder(path, clazz);
    }

    public void load() {
        if (Files.exists(this.path)) {
            if (Files.isDirectory(this.path)) {
                LogUtils.error("Failed to load file at {}! File is a directory!", this.path);
                try {
                    Files.delete(this.path);
                } catch (IOException exception) {
                    LogUtils.error("An unexpected error occurred while deleting directory which should have been a file!", exception);
                    return;
                }
            }
        } else {
            try {
                if (this.defaults != null) {
                    this.save(new String(this.defaults.readAllBytes(), StandardCharsets.UTF_8));
                }
            } catch (IOException exception) {
                LogUtils.error("Failed to read bytes from defaults stream!");
                return;
            }
        }

        try (BufferedInputStream stream = new BufferedInputStream(new FileInputStream(this.path.toFile())); InputStreamReader reader = new InputStreamReader(stream, StandardCharsets.UTF_8)) {
            this.config = new LinkedHashMap<>(this.yaml.load(reader));
        } catch (IOException exception) {
            LogUtils.error("An unexpected error occurred while loading yaml file for updating!", exception);
            return;
        }

        if (this.runUpdaters()) {
            this.save();
        }

        LinkedHashMap<String, Object> map = new LinkedHashMap<>();
        this.updateFields(map, "", this.clazz);
        LogUtils.warn("Contents: {}", map);
        if (this.needsSaving) {
            this.save();
        }

        this.config = map;
    }

    private void updateFields(LinkedHashMap<String, Object> map, String path, Class<? extends ConfigurationPart> original) {
        Class<?> clazz = original;
        do {
            clazz = clazz.getSuperclass();

            for (Field field : clazz.getFields()) {
                if (Modifier.isFinal(field.getModifiers())) {
                    continue;
                }

                Named named = field.getAnnotation(Named.class);
                Type type = field.getGenericType();
                String name = named != null ? named.value() : this.keyRenamer.rename(field.getName());
                try {
                    Object value = this.get(path + "." + name);
                    if (value == null) {
                        value = field.get(null);
                        this.needsSaving = true;
                    }

                    Object deserialized = this.holder.deserialize(value, type);
                    this.set0(map, path + "." + name, deserialized);
                    field.set(null, deserialized);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }

            for (Method method : clazz.getMethods()) {
                if (method.isAnnotationPresent(PostProcess.class)) {
                    try {
                        method.invoke(null);
                    } catch (IllegalAccessException | InvocationTargetException exception) {
                        LogUtils.error("Failed to call PostProcess method!", exception);
                    }
                }
            }

            for (Class<?> cl : clazz.getClasses()) {
                if (!ConfigurationPart.class.isAssignableFrom(cl)) {
                    continue;
                }

                this.updateFields(map, path.isEmpty() ? this.keyRenamer.rename(cl.getSimpleName()) : path + "." + this.keyRenamer.rename(cl.getSimpleName()), (Class<? extends ConfigurationPart>) cl);
            }
        } while (clazz.getSuperclass() != Object.class);
    }

    public void move(String path, String newPath) {
        Object previous = this.get(path);
        this.remove(path);
        this.set(newPath, previous);
    }

    public void set(String path, Object value) {
        this.set0(this.config, path, value);
    }

    private void set0(Map<String, Object> map, String path, Object value) {
        String[] route = path.split("\\.");
        if (route.length == 1) {
            map.put(route[0], value);
        } else {
            int i = 0;
            Map<String, Object> parent = map;
            while (i < route.length) {
                Map<String, Object> node = (Map<String, Object>) parent.get(route[i]);
                if (node == null) {
                    node = new LinkedHashMap<>();
                    parent.put(route[i], node);
                }

                parent = node;
                i++;
                if (i == route.length - 1) {
                    parent.put(route[i], value);
                    break;
                }
            }
        }
    }

    public <T> T get(String path, Class<T> clazz) {
        return clazz.cast(this.get(path));
    }

    public Object get(String path) {
        String[] route = path.split("\\.");
        if (route.length == 1) {
            return this.config.get(route[0]);
        }

        int i = 0;
        Map<String, Object> parent = this.config;
        while (i < route.length) {
            Map<String, Object> node = (Map<String, Object>) parent.get(route[i]);
            if (node == null) {
                return null;
            }

            parent = node;
            i++;
            if (i == route.length - 1) {
                return parent.get(route[i]);
            }
        }

        return null;
    }

    public void remove(String path) {
        String[] route = path.split("\\.");
        if (route.length == 1) {
            this.config.remove(route[0]);
            return;
        }

        int i = 0;
        Map<String, Object> parent = this.config;
        while (i < route.length) {
            Map<String, Object> node = (Map<String, Object>) parent.get(route[i]);
            if (node == null) {
                node = new LinkedHashMap<>();
                parent.put(route[i], node);
            }

            parent = node;
            i++;
            if (i == route.length - 1) {
                parent.remove(route[i]);
                break;
            }
        }
    }

    public void save() {
        this.save(this.yaml.dump(this.holder.serialize(this.config, Map.class))); // TODO: verify that this works
    }

    private void save(String stream) {
        try {
            File file = this.path.toFile();
            File parent = file.getParentFile();
            if (parent != null) {
                parent.mkdirs();
            }

            File temp = new File(parent, file.getName() + ".tmp");
            temp.delete();
            temp.createNewFile();
            try {
                try (BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(this.path.toFile()))) {
                    outputStream.write(stream.getBytes(StandardCharsets.UTF_8));
                }

                try {
                    Files.move(temp.toPath(), this.path, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
                } catch (AtomicMoveNotSupportedException exception) {
                    Files.move(temp.toPath(), this.path, StandardCopyOption.REPLACE_EXISTING);
                }
            } finally {
                temp.delete();
            }
        } catch (IOException exception) {
            LogUtils.error("An unexpected error occurred while saving file!", exception);
        }
    }

    private boolean runUpdaters() {
        int configVersion = this.get(this.configVersionPath, int.class);
        if (configVersion == this.configVersion) {
            // We don't need to update anything
            return false;
        }

        for (Map.Entry<IntIntPair, ConfigurationUpdater> entry : this.updaters.entrySet()) {
            if (entry.getKey().firstInt() == configVersion) {
                entry.getValue().update(this);
                configVersion = entry.getKey().secondInt();
                this.set(this.configVersionPath, configVersion);
            }
        }

        return true;
    }

    public static class Builder {
        private final Path path;
        private final Class<? extends ConfigurationPart> clazz;
        private final Map<IntIntPair, ConfigurationUpdater> updaters = new LinkedHashMap<>();
        private final Map<Class<?>, TypeAdapter<?, ?>> adapters = new HashMap<>();
        private InputStream defaults;
        private DumperOptions dumperOptions = new DumperOptions();
        private LoaderOptions loaderOptions = new LoaderOptions();
        private KeyRenamer keyRenamer = new LowerKebabCaseRenamer();
        private int configVersion = 0;
        private String configVersionPath = null;

        private Builder(Path path, Class<? extends ConfigurationPart> clazz) {
            this.path = path;
            this.clazz = clazz;
        }

        public Builder addUpdater(int fromVersion, int toVersion, ConfigurationUpdater updater) {
            this.updaters.put(IntIntPair.of(fromVersion, toVersion), updater);
            return this;
        }

        public Builder withDefaults(InputStream defaults) {
            this.defaults = defaults;
            return this;
        }

        public Builder withDumperOptions(Consumer<DumperOptions> dumperOptions) {
            dumperOptions.accept(this.dumperOptions);
            return this;
        }

        public Builder withLoaderOptions(Consumer<LoaderOptions> loaderOptions) {
            loaderOptions.accept(this.loaderOptions);
            return this;
        }

        public Builder configVersion(int configVersion, String configVersionPath) {
            this.configVersion = configVersion;
            this.configVersionPath = configVersionPath;
            return this;
        }

        public Builder registerAdapter(Class<?> clazz, TypeAdapter<?, ?> adapter) {
            this.adapters.put(clazz, adapter);
            return this;
        }

        public Builder withKeyRenamer(KeyRenamer keyRenamer) {
            this.keyRenamer = Preconditions.checkNotNull(keyRenamer, "Can't set to a null KeyRenamer!");
            return this;
        }

        public YamlConfiguration build() {
            return new YamlConfiguration(this.path, this.defaults, this.clazz, this.dumperOptions, this.loaderOptions, this.updaters, this.configVersion, this.configVersionPath, this.adapters, this.keyRenamer);
        }
    }
}
