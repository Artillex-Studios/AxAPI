package com.artillexstudios.axapi.config;

import com.artillexstudios.axapi.config.adapters.ConfigurationGetter;
import com.artillexstudios.axapi.config.adapters.TypeAdapter;
import com.artillexstudios.axapi.config.adapters.TypeAdapterHolder;
import com.artillexstudios.axapi.config.annotation.Comment;
import com.artillexstudios.axapi.config.annotation.ConfigurationPart;
import com.artillexstudios.axapi.config.annotation.Header;
import com.artillexstudios.axapi.config.annotation.Ignored;
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
import org.yaml.snakeyaml.comments.CommentLine;
import org.yaml.snakeyaml.comments.CommentType;
import org.yaml.snakeyaml.nodes.AnchorNode;
import org.yaml.snakeyaml.nodes.MappingNode;
import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.nodes.NodeTuple;
import org.yaml.snakeyaml.nodes.Tag;
import org.yaml.snakeyaml.reader.UnicodeReader;
import org.yaml.snakeyaml.representer.Representer;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.annotation.Annotation;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

public final class YamlConfiguration implements ConfigurationGetter {
    private final LinkedHashMap<String, Comment> comments = new LinkedHashMap<>();
    private final TypeAdapterHolder holder = new TypeAdapterHolder();
    private final Map<IntIntPair, ConfigurationUpdater> updaters;
    private final Path path;
    private final InputStream defaults;
    private final Class<? extends ConfigurationPart> clazz;
    private final Yaml yaml;
    private final int configVersion;
    private final String configVersionPath;
    private final KeyRenamer keyRenamer;
    private final YamlConstructor constructor;
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
        this.constructor = new YamlConstructor(loaderOptions);
        this.holder.registerAdapters(adapters);

        this.yaml = new Yaml(this.constructor, new Representer(dumperOptions), dumperOptions, loaderOptions);
    }

    public static YamlConfiguration.Builder of(Path path, Class<? extends ConfigurationPart> clazz) {
        return new Builder(path, clazz);
    }

    public static YamlConfiguration.Builder of(Path path) {
        return new Builder(path, null);
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
                } else {
                    this.save(null);
                }
            } catch (IOException exception) {
                LogUtils.error("Failed to read bytes from defaults stream!");
                return;
            }
        }

        try (BufferedInputStream stream = new BufferedInputStream(new FileInputStream(this.path.toFile())); UnicodeReader reader = new UnicodeReader(stream)) {
            this.config = new LinkedHashMap<>();
            this.load0("", (MappingNode) this.yaml.compose(reader), this.config);
        } catch (IOException exception) {
            LogUtils.error("An unexpected error occurred while loading yaml file for updating!", exception);
            return;
        }

        if (this.configVersionPath != null && this.runUpdaters()) {
            this.save();
        }

        LinkedHashMap<String, Object> map = new LinkedHashMap<>();
        this.updateFields(map, "", this.clazz);
        this.config = map;

        if (this.needsSaving) {
            this.save();
            this.needsSaving = false;
        }
    }

    private void load0(String path, MappingNode node, LinkedHashMap<String, Object> map) {
        if (node == null) {
            return;
        }

        this.constructor.flatten(node);

        for (NodeTuple nodeTuple : node.getValue()) {
            Node key = nodeTuple.getKeyNode();
            String keyString = String.valueOf(this.constructor.construct(key));
            Node value;

            for (value = nodeTuple.getValueNode(); value instanceof AnchorNode anchorNode; value = anchorNode.getRealNode()) {

            }

            String path1 = path.isEmpty() ? keyString : path + "." + keyString;
            if (value instanceof MappingNode mappingNode) {
                LinkedHashMap<String, Object> newSection = new LinkedHashMap<>();
                this.set0(map, keyString, newSection);
                this.load0(keyString, mappingNode, newSection);
            } else {
                this.set0(map, keyString, this.constructor.construct(value));
            }

            List<CommentLine> blockComments = key.getBlockComments();
            if (blockComments != null) {
                StringBuilder commentValue = new StringBuilder();
                for (int i = 0; i < blockComments.size(); i++) {
                    CommentLine blockComment = blockComments.get(i);
                    commentValue.append(blockComment.getValue());
                    if (i + 1 < blockComments.size()) {
                        commentValue.append('\n');
                    }
                }

                if (!commentValue.isEmpty()) {
                    this.comments.put(path1, new Comment() {

                        @Override
                        public Class<? extends Annotation> annotationType() {
                            return Comment.class;
                        }

                        @Override
                        public String value() {
                            return commentValue.toString();
                        }

                        @Override
                        public CommentType type() {
                            return CommentType.BLOCK;
                        }
                    });
                }
            }

            List<CommentLine> inlineComments = key.getInLineComments();
            if (inlineComments != null) {
                StringBuilder commentValue = new StringBuilder();
                for (int i = 0; i < inlineComments.size(); i++) {
                    CommentLine comment = inlineComments.get(i);
                    commentValue.append(comment.getValue());
                    if (i + 1 < inlineComments.size()) {
                        commentValue.append('\n');
                    }
                }

                if (!commentValue.isEmpty()) {
                    this.comments.put(path1, new Comment() {

                        @Override
                        public Class<? extends Annotation> annotationType() {
                            return Comment.class;
                        }

                        @Override
                        public String value() {
                            return commentValue.toString();
                        }

                        @Override
                        public CommentType type() {
                            return CommentType.INLINE;
                        }
                    });
                }
            }
        }

    }

    private void updateFields(LinkedHashMap<String, Object> map, String path, Class<? extends ConfigurationPart> original) {
        if (this.clazz == null) {
            return;
        }

        Class<?> clazz = original;
        List<Class<?>> classes = new ArrayList<>();
        do {
            classes.add(clazz);
            clazz = clazz.getSuperclass();
        } while (clazz.getSuperclass() != null && clazz.getSuperclass() != Object.class);
        Collections.reverse(classes);

        for (Class<?> cl : classes) {
            for (Field field : cl.getFields()) {
                if (Modifier.isFinal(field.getModifiers()) || field.isAnnotationPresent(Ignored.class)) {
                    continue;
                }

                Named named = field.getAnnotation(Named.class);
                Type type = field.getGenericType();
                String name = named != null ? named.value() : this.keyRenamer.rename(field.getName());
                try {
                    String path1 = path.isEmpty() ? name : path + "." + name;
                    Object value = this.get(path1);
                    if (value == null) {
                        value = field.get(null);
                        this.needsSaving = true;
                    }

                    Object deserialized = this.holder.deserialize(value, type);
                    this.set0(map, path1, deserialized);
                    field.set(null, deserialized);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }

            for (Method method : cl.getMethods()) {
                if (method.isAnnotationPresent(PostProcess.class)) {
                    try {
                        method.invoke(null);
                    } catch (IllegalAccessException | InvocationTargetException exception) {
                        LogUtils.error("Failed to call PostProcess method!", exception);
                    }
                }
            }

            for (Class<?> c : cl.getClasses()) {
                if (!ConfigurationPart.class.isAssignableFrom(cl)) {
                    continue;
                }

                Named named = c.getAnnotation(Named.class);
                String name = named == null ? this.keyRenamer.rename(cl.getSimpleName()) : named.value();
                this.updateFields(map, path.isEmpty() ? name : path + "." + name, (Class<? extends ConfigurationPart>) c);
            }
        }
    }

    public void move(String path, String newPath) {
        Object previous = this.get(path);
        this.remove(path);
        this.set(newPath, previous);
    }

    public void set(String path, Object value) {
        if (this.clazz == null) {
            value = this.holder.serialize(value, null);
        }

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

    @Override
    public <T> T get(String path, Class<T> clazz) {
        if (this.clazz == null) {
            return clazz.cast(this.holder.deserialize(this.get(path), clazz));
        }

        return clazz.cast(this.get(path));
    }

    public Set<String> keys() {
        return new HashSet<>(this.config.keySet());
    }

    public Object get(String path) {
        return this.get0(this.config, path);
    }

    public String dumpInternalData() {
        return this.dumpInternalData0(this.config);
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
                LogUtils.warn("Expected map class, but in reality it was: {}. Value: {}. Route: {}", found.getClass(), found, route[i]);
                return null;
            }

            parent = (Map<String, Object>) mapNode;
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
        LinkedHashMap<String, Object> map = new LinkedHashMap<>();
        this.save0(map, "", this.clazz);
        StringWriter writer = new StringWriter();
        MappingNode mappingNode = this.map(map, "");
        if (this.clazz != null) {
            Header header = this.clazz.getAnnotation(Header.class);
            if (header != null) {
                List<CommentLine> lines = new ArrayList<>();
                for (String string : header.value()) {
                    lines.add(new CommentLine(null, null, string, CommentType.BLOCK));
                }
                mappingNode.setBlockComments(lines);
            }
        }

        this.yaml.serialize(mappingNode, writer);
        this.save(writer.toString());
    }

    private void save0(LinkedHashMap<String, Object> map, String path, Class<? extends ConfigurationPart> original) {
        if (this.clazz == null) {
            map.putAll(this.config);
            return;
        }

        this.comments.clear();
        Class<?> clazz = original;
        List<Class<?>> classes = new ArrayList<>();
        do {
            classes.add(clazz);
            clazz = clazz.getSuperclass();
        } while (clazz.getSuperclass() != null && clazz.getSuperclass() != Object.class);
        Collections.reverse(classes);

        for (Class<?> c : classes) {
            for (Class<?> cl : c.getClasses()) {
                if (!ConfigurationPart.class.isAssignableFrom(cl)) {
                    continue;
                }

                Named named = cl.getAnnotation(Named.class);
                String name = named == null ? this.keyRenamer.rename(cl.getSimpleName()) : named.value();
                this.save0(map, path.isEmpty() ? name : path + "." + name, (Class<? extends ConfigurationPart>) cl);
            }

            Named n = c.getAnnotation(Named.class);
            String na = n == null ? this.keyRenamer.rename(c.getSimpleName()) : n.value();
            Comment classComment = c.getAnnotation(Comment.class);
            if (classComment != null) {
                this.comments.put(path.isEmpty() ? na : path + "." + na, classComment);
            }

            for (Field field : c.getFields()) {
                if (Modifier.isFinal(field.getModifiers()) || field.isAnnotationPresent(Ignored.class)) {
                    continue;
                }

                Comment comment = field.getAnnotation(Comment.class);
                Named named = field.getAnnotation(Named.class);
                Type type = field.getGenericType();
                String name = named != null ? named.value() : this.keyRenamer.rename(field.getName());
                try {
                    String path1 = path.isEmpty() ? name : path + "." + name;
                    Object serialized = this.holder.serialize(field.get(null), type);
                    if (comment != null) {
                        this.comments.put(path1, comment);
                    }
                    this.set0(map, path1, serialized);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        }
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
                if (stream != null) {
                    try (PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(temp)))) {
                        String[] lines = stream.split("\n");
                        for (int i = 0; i < lines.length; i++) {
                            String line = lines[i];
                            if (line.strip().startsWith("#")) {
                                if (this.getLeadingWhiteSpace(line) == 0 && i >= 1) {
                                    writer.println();
                                }
                                writer.println(this.toPrettyComment(line));
                                int j = i + 1;
                                while (j < lines.length) {
                                    String nextLine = lines[j];
                                    if (!nextLine.strip().startsWith("#")) {
                                        break;
                                    }

                                    writer.println(this.toPrettyComment(nextLine));
                                    j++;
                                    i++;
                                }
                            } else if (i >= 1 && this.getLeadingWhiteSpace(line) < this.getLeadingWhiteSpace(lines[i - 1]) && this.getLeadingWhiteSpace(line) == 0) {
                                writer.println();
                                writer.println(line);
                            } else {
                                writer.println(line);
                            }
                        }

                        writer.flush();
                    }
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

    private String toPrettyComment(String string) {
        int index = string.indexOf('#');
        if (index == -1 || index == string.length() - 1) {
            return string;
        }

        char ch = string.charAt(index + 1);
        // Already pretty
        if (Character.isWhitespace(ch)) {
            return string;
        }

        StringBuilder builder = new StringBuilder(string.length());
        for (int i = 0; i < string.length(); i++) {
            if (string.charAt(i) == '#') {
                builder.append('#').append(' ');
            } else {
                builder.append(string.charAt(i));
            }
        }

        return builder.toString();
    }

    private int getLeadingWhiteSpace(String string) {
        int whiteSpace = 0;
        for (int i = 0; i < string.length(); i++) {
            char ch = string.charAt(i);
            if (!Character.isWhitespace(ch) && ch != '-') {
                break;
            }

            whiteSpace++;
        }

        return whiteSpace;
    }

    private boolean runUpdaters() {
        Integer configVersion = (Integer) this.holder.deserialize(this.get(this.configVersionPath), Integer.class);
        configVersion = configVersion == null ? 1 : configVersion;
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

    private MappingNode map(Map<String, Object> map, String path) {
        List<NodeTuple> nodes = new ArrayList<>();
        Node key;
        Node value;
        for (Iterator<Map.Entry<String, Object>> iterator = map.entrySet().iterator(); iterator.hasNext(); nodes.add(new NodeTuple(key, value))) {
            Map.Entry<String, Object> entry = iterator.next();
            key = this.yaml.represent(entry.getKey());
            if (entry.getValue() instanceof Map<?, ?> m) {
                value = this.map((Map<String, Object>) m, path.isEmpty() ? entry.getKey() : path + "." + entry.getKey());
            } else {
                value = this.yaml.represent(entry.getValue());
            }

            Comment comment = this.comments.get(path.isEmpty() ? entry.getKey() : path + "." + entry.getKey());
            if (comment != null) {
                List<CommentLine> lines = new ArrayList<>();
                String[] split = comment.value().split("\n");
                for (String string : split) {
                    lines.add(new CommentLine(null, null, string, comment.type() == Comment.CommentType.BLOCK ? CommentType.BLOCK : CommentType.IN_LINE));
                }
                if (comment.type() == Comment.CommentType.BLOCK) {
                    key.setBlockComments(lines);
                } else {
                    key.setInLineComments(lines);
                }
            }
        }

        return new MappingNode(Tag.MAP, nodes, DumperOptions.FlowStyle.BLOCK);
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
            this.dumperOptions.setProcessComments(true);
            this.loaderOptions.setProcessComments(true);
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
