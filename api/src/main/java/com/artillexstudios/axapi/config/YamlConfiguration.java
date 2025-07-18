package com.artillexstudios.axapi.config;

import com.artillexstudios.axapi.config.adapters.MapConfigurationGetter;
import com.artillexstudios.axapi.config.adapters.TypeAdapter;
import com.artillexstudios.axapi.config.adapters.TypeAdapterHolder;
import com.artillexstudios.axapi.config.adapters.other.ObjectAdapter;
import com.artillexstudios.axapi.config.annotation.Comment;
import com.artillexstudios.axapi.config.annotation.ConfigurationPart;
import com.artillexstudios.axapi.config.annotation.Serializable;
import com.artillexstudios.axapi.config.reader.ClassConfigurationReader;
import com.artillexstudios.axapi.config.reader.FileConfigurationReader;
import com.artillexstudios.axapi.config.reader.Handler;
import com.artillexstudios.axapi.config.renamer.KeyRenamer;
import com.artillexstudios.axapi.config.renamer.LowerKebabCaseRenamer;
import com.artillexstudios.axapi.config.service.Formatter;
import com.artillexstudios.axapi.config.service.Writer;
import com.artillexstudios.axapi.config.service.implementation.FileCreator;
import com.artillexstudios.axapi.config.service.implementation.FileWriter;
import com.artillexstudios.axapi.config.service.implementation.YamlFormatter;
import com.artillexstudios.axapi.reflection.ClassUtils;
import com.artillexstudios.axapi.utils.UncheckedUtils;
import com.artillexstudios.axapi.utils.logging.LogUtils;
import it.unimi.dsi.fastutil.Pair;
import it.unimi.dsi.fastutil.ints.IntIntPair;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.representer.Representer;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Consumer;

public final class YamlConfiguration<T extends ConfigurationPart> extends MapConfigurationGetter {
    private final LinkedHashMap<String, Object> contents;
    private final Map<String, Comment> comments = new HashMap<>();
    private final TypeAdapterHolder holder = new TypeAdapterHolder();
    private final YamlConfiguration.Builder<T> builder;
    private final YamlConstructor constructor;
    private final FileCreator creator;
    private final Handler reader;
    private final Yaml yaml;

    YamlConfiguration(YamlConfiguration.Builder<T> builder) {
        super(new LinkedHashMap<>());
        this.contents = UncheckedUtils.unsafeCast(this.wrapped());
        this.builder = builder;
        this.creator = new FileCreator(this.builder.writer);
        this.constructor = new YamlConstructor(builder.loaderOptions);
        this.holder.registerAdapters(builder.adapters);
        this.yaml = new Yaml(this.constructor, new Representer(builder.dumperOptions), builder.dumperOptions, builder.loaderOptions);
        this.reader = this.builder.clazz == null ? new FileConfigurationReader(this.yaml, this.constructor, this.holder) : new ClassConfigurationReader(this.yaml, this.constructor, this.holder, this.builder.keyRenamer, this.builder.clazz);
    }

    public static <T extends ConfigurationPart> YamlConfiguration.Builder<T> of(Path path, Class<T> clazz) {
        return new Builder<>(path, clazz);
    }

    public static YamlConfiguration.Builder<?> of(Path path) {
        return new Builder<>(path, null);
    }

    public boolean load() {
        boolean createFile = this.creator.create(this.builder.path, this.builder.defaults);
        if (!createFile) {
            return false;
        }

        if (this.builder.configVersionPath != null && this.runUpdaters()) {
            this.save();
        }

        try (BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(this.builder.path.toFile()))) {
            Pair<Map<String, Object>, Map<String, Comment>> read = this.reader.read(bufferedInputStream, null);
            this.contents.putAll(read.first());

            this.comments.putAll(read.second());
        } catch (IOException exception) {
            return false;
        }

        this.save();
        return true;
    }

    public T create(T instance) {
        boolean createFile = this.creator.create(this.builder.path, this.builder.defaults);
        if (!createFile) {
            return null;
        }

        if (this.builder.configVersionPath != null && this.runUpdaters()) {
            this.save();
        }

        try (BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(this.builder.path.toFile()))) {
            Pair<Map<String, Object>, Map<String, Comment>> read = this.reader.read(bufferedInputStream, instance);
            this.contents.putAll(read.first());

            this.comments.putAll(read.second());
        } catch (IOException exception) {
            return null;
        }

        this.save();
        return instance;
    }

    public T create() {
        return this.create(ClassUtils.INSTANCE.create(this.builder.clazz));
    }

    public boolean save() {
        return this.builder.writer.write(this.builder.path, this.builder.formatter.format(this.reader.write(this.contents, this.comments)));
    }

    public void move(String path, String newPath) {
        Object previous = this.get(path);
        this.remove(path);
        this.set(newPath, previous);
    }

    public void set(String path, Object value) {
        if (this.builder.clazz == null) {
            value = this.holder.serialize(value, null);
        }

        this.set0(this.contents, path, value);
    }

    public Path path() {
        return this.builder.path;
    }

    private void set0(Map<String, Object> map, String path, Object value) {
        String[] route = path.split("\\.");
        if (route.length == 1) {
            map.put(route[0], value);
        } else {
            int i = 0;
            Map<String, Object> parent = map;
            while (i < route.length) {
                Map<String, Object> node;

                Object found = parent.get(route[i]);
                if (found == null) {
                    node = new LinkedHashMap<>();
                    found = node;
                    parent.put(route[i], node);
                }

                if (!(found instanceof Map<?, ?>)) {
                    LogUtils.warn("Expected map class, but in reality it was: {}. Value: {}. Route: {} Full path: {}", found.getClass(), found, route[i], path);
                    return;
                }
                node = UncheckedUtils.unsafeCast(found);

                parent = node;
                i++;
                if (i == route.length - 1) {
                    parent.put(route[i], value);
                    break;
                }
            }
        }
    }

    @Override
    public <Z> Z get(String path, Class<Z> clazz) {
        if (this.builder.clazz == null) {
            return clazz.cast(this.holder.deserialize(this.get(path), clazz));
        }

        return clazz.cast(this.get(path));
    }

    public Object get(String path) {
        return this.get0(this.contents, path);
    }

    public String dumpInternalData() {
        return this.dumpInternalData0(this.contents);
    }

    private String dumpInternalData0(Map<?, ?> map) {
        StringBuilder builder = new StringBuilder();
        for (Map.Entry<?, ?> entry : map.entrySet()) {
            if (entry.getValue() instanceof Map<?, ?> m) {
                builder.append(entry.getKey()).append("=").append(this.dumpInternalData0(m)).append(";class=").append(m.getClass()).append("\n");
            } else {
                builder.append(entry.getKey()).append(entry.getValue()).append(";class=").append(entry.getValue().getClass()).append("\n");
            }
        }

        return builder.toString();
    }

    private Object get0(Map<String, Object> map, String path) {
        String[] route = path.split("\\.");
        if (route.length == 1) {
            return map.get(route[0]);
        }

        int i = 0;
        Map<String, Object> parent = map;
        while (i < route.length) {
            Object found = parent.get(route[i]);
            if (found == null) {
                return null;
            }

            if (!(found instanceof Map<?, ?> mapNode)) {
                LogUtils.warn("Expected map class, but in reality it was: {}. Value: {}. Route: {} Full path: {}", found.getClass(), found, route[i], path);
                return null;
            }

            parent = UncheckedUtils.unsafeCast(mapNode);
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
            this.contents.remove(route[0]);
            return;
        }

        int i = 0;
        Map<String, Object> parent = this.contents;
        while (i < route.length) {
            Map<String, Object> node = UncheckedUtils.unsafeCast(parent.get(route[i]));
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

    private boolean runUpdaters() {
        Integer configVersion = this.getInteger(this.builder.configVersionPath);
        configVersion = configVersion == null ? 1 : configVersion;
        if (configVersion == this.builder.configVersion) {
            // We don't need to update anything
            return false;
        }

        for (Map.Entry<IntIntPair, com.artillexstudios.axapi.config.ConfigurationUpdater> entry : this.builder.updaters.entrySet()) {
            if (entry.getKey().firstInt() == configVersion) {
                entry.getValue().update(this);
                configVersion = entry.getKey().secondInt();
                this.set(this.builder.configVersionPath, configVersion);
            }
        }

        return true;
    }


    public static class Builder<T extends ConfigurationPart> {
        private final Path path;
        private final Class<T> clazz;
        private final Map<IntIntPair, com.artillexstudios.axapi.config.ConfigurationUpdater> updaters = new LinkedHashMap<>();
        private final Map<Class<?>, TypeAdapter<?, ?>> adapters = new HashMap<>();
        private InputStream defaults;
        private final DumperOptions dumperOptions = new DumperOptions();
        private final LoaderOptions loaderOptions = new LoaderOptions();
        private Formatter formatter = new YamlFormatter();
        private Writer writer = new FileWriter();
        private KeyRenamer keyRenamer = new LowerKebabCaseRenamer();
        private int configVersion = 0;
        private String configVersionPath = null;

        private Builder(Path path, Class<T> clazz) {
            this.path = path;
            this.clazz = clazz;
            this.dumperOptions.setProcessComments(true);
            this.loaderOptions.setProcessComments(true);
        }

        public Builder<T> addUpdater(int fromVersion, int toVersion, com.artillexstudios.axapi.config.ConfigurationUpdater updater) {
            this.updaters.put(IntIntPair.of(fromVersion, toVersion), updater);
            return this;
        }

        public Builder<T> withDefaults(InputStream defaults) {
            this.defaults = defaults;
            return this;
        }

        public Builder<T> withDumperOptions(Consumer<DumperOptions> dumperOptions) {
            dumperOptions.accept(this.dumperOptions);
            return this;
        }

        public Builder<T> withLoaderOptions(Consumer<LoaderOptions> loaderOptions) {
            loaderOptions.accept(this.loaderOptions);
            return this;
        }

        public Builder<T> configVersion(int configVersion, String configVersionPath) {
            this.configVersion = configVersion;
            this.configVersionPath = configVersionPath;
            return this;
        }

        public Builder<T> registerAdapter(Class<?> clazz, TypeAdapter<?, ?> adapter) {
            this.adapters.put(clazz, adapter);
            return this;
        }

        public Builder<T> withKeyRenamer(KeyRenamer keyRenamer) {
            this.keyRenamer = keyRenamer;
            return this;
        }

        public Builder<T> withFormatter(Formatter formatter) {
            this.formatter = formatter;
            return this;
        }

        public Builder<T> withWriter(Writer writer) {
            this.writer = writer;
            return this;
        }

        public KeyRenamer keyRenamer() {
            return this.keyRenamer;
        }

        public YamlConfiguration<T> build() {
            this.adapters.put(Serializable.class, new ObjectAdapter<>(this));
            return new YamlConfiguration<>(this);
        }
    }
}
