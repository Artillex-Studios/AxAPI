package com.artillexstudios.axapi.dependency;

import com.artillexstudios.axapi.utils.Nameable;
import com.artillexstudios.axapi.utils.file.FileUtils;
import com.artillexstudios.axapi.utils.logging.LogUtils;

import java.io.File;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.concurrent.ConcurrentHashMap;

public final class DependencyContainer {
    private static final ConcurrentHashMap<Class<?>, DependencyHolder<?>> container = new ConcurrentHashMap<>();

    static {
        registerDefault(Nameable.class, () -> "");
        registerDefault(FileUtils.class, new FileUtils() {
            @Override
            public File getFolder() {
                try {
                    return Paths.get(this.getClass().getProtectionDomain().getCodeSource().getLocation().toURI()).toFile();
                } catch (URISyntaxException e) {
                    LogUtils.error("Failed to get the path of the file!");
                    throw new NullPointerException();
                }
            }
        });
    }

    public static <T> void register(Class<T> clazz, T instance) {
        DependencyHolder<?> dependencyHolder = container.get(clazz);
        if (dependencyHolder != null && !dependencyHolder.isDefault()) {
            throw new IllegalStateException("Failed to register dependency as it had already been registered!");
        }

        container.put(clazz, new DependencyHolder<>(instance, false));
    }

    public static <T> void registerDefault(Class<T> clazz, T instance) {
        DependencyHolder<?> dependencyHolder = container.get(clazz);
        if (dependencyHolder != null && !dependencyHolder.isDefault()) {
            throw new IllegalStateException("Failed to register dependency as it had already been registered!");
        }

        container.put(clazz, new DependencyHolder<>(instance, true));
    }

    public static <T> T getInstance(Class<T> clazz) {
        DependencyHolder<?> holder = container.get(clazz);
        if (holder == null) {
            throw new IllegalStateException("An instance of this class has not yet been registered!");
        }

        return clazz.cast(holder.dependency());
    }
}
