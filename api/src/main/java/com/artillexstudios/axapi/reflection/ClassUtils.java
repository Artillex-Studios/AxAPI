package com.artillexstudios.axapi.reflection;

import com.artillexstudios.axapi.utils.CachingSupplier;
import com.artillexstudios.axapi.utils.logging.LogUtils;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import org.jspecify.annotations.NonNull;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;

public enum ClassUtils {
    INSTANCE;

    private final Supplier<LoadingCache<String, Class<?>>> cacheSupplier = CachingSupplier.create(() -> {
        return Caffeine.newBuilder()
                .maximumSize(50)
                .build(name -> {
                    try {
                        return Class.forName(name, false, this.getClass().getClassLoader());
                    } catch (ClassNotFoundException exception) {
                        return null;
                    }
                });
    });

    public boolean classExists(@NonNull String className) {
        return cacheSupplier.get().get(className) != null;
    }

    public Class<?> getClass(String clazz) {
        try {
            return Class.forName(clazz);
        } catch (ClassNotFoundException exception) {
            LogUtils.error("An unexpected error occurred while finding class {}!", clazz, exception);
            throw new RuntimeException(exception);
        }
    }

    public Class<?> getClassOrNull(String clazz) {
        try {
            return Class.forName(clazz);
        } catch (ClassNotFoundException exception) {
            return null;
        }
    }

    public boolean isClass(Class<?> clazz, String other) {
        return cacheSupplier.get().get(other) == clazz;
    }

    public boolean classEquals(Class<?> clazz, Class<?> other) {
        return clazz != null && clazz == other;
    }

    public <T> T create(Class<T> clazz, Object... arguments) {
        Class<?>[] classes = arguments.length == 0 ? new Class[0] : Arrays.stream(arguments)
                .map(Object::getClass)
                .toArray(Class[]::new);

        try {
            return clazz.getDeclaredConstructor(classes).newInstance(arguments);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException exception) {
            LogUtils.error("Failed to initialize class {} with arguments {}!", clazz.getName(), Arrays.toString(arguments));
            return null;
        }
    }

    public String debugClass(Class<?> clazz) {
        return this.debugClass(0, clazz);
    }

    private String debugClass(int indent, Class<?> clazz) {
        StringBuilder builder = new StringBuilder();
        builder.append("--- ").append(clazz.getName()).append(" ---").append(System.lineSeparator());
        builder.repeat("\t", indent).append("Interface: ").append(clazz.isInterface()).append(System.lineSeparator());
        builder.repeat("\t", indent).append("Record: ").append(clazz.isRecord()).append(System.lineSeparator());
        builder.repeat("\t", indent).append("Sealed: ").append(clazz.isSealed()).append(System.lineSeparator());
        builder.repeat("\t", indent).append("Member: ").append(clazz.isMemberClass()).append(System.lineSeparator());
        builder.repeat("\t", indent).append("Array: ").append(clazz.isArray()).append(System.lineSeparator());
        builder.repeat("\t", indent).append("Enum: ").append(clazz.isEnum()).append(System.lineSeparator());
        builder.repeat("\t", indent).append("Hidden: ").append(clazz.isHidden()).append(System.lineSeparator());
        builder.repeat("\t", indent).append("Implemented interfaces: ").append(String.join(", ", Arrays.stream(clazz.getInterfaces()).map(java.lang.Class::getName).toList()))
                .append(System.lineSeparator());
        builder.repeat("\t", indent).append("Subclasses: ").append(System.lineSeparator());
        for (Class<?> subClass : clazz.getClasses()) {
            builder.repeat("\t", indent + 1).append(debugClass(indent + 1, subClass)).append(System.lineSeparator());
        }
        builder.repeat("\t", indent).append("Fields: ").append(System.lineSeparator());
        for (Field declaredField : clazz.getDeclaredFields()) {
            builder.repeat("\t", indent + 1).append("Name: ").append(declaredField.getName()).append(System.lineSeparator());
            builder.repeat("\t", indent + 1).append("Modifiers: ").append(Modifier.toString(declaredField.getModifiers())).append(System.lineSeparator());
            builder.repeat("\t", indent + 1).append("Type: ").append(declaredField.getType()).append(System.lineSeparator());
            builder.repeat("\t", indent + 1).append("Generic type: ").append(declaredField.getGenericType()).append(System.lineSeparator());
            builder.repeat("\t", indent + 1).append("Annotated type: ").append(declaredField.getAnnotatedType()).append(System.lineSeparator());
        }
        builder.repeat("\t", indent).append("Methods: ").append(System.lineSeparator());
        for (Method method : clazz.getDeclaredMethods()) {
            builder.repeat("\t", indent + 1).append("Name: ").append(method.getName()).append(System.lineSeparator());
            builder.repeat("\t", indent + 1).append("Modifiers: ").append(Modifier.toString(method.getModifiers())).append(System.lineSeparator());
            builder.repeat("\t", indent + 1).append("Type: ").append(method.getReturnType()).append(System.lineSeparator());
            builder.repeat("\t", indent + 1).append("Generic type: ").append(method.getGenericReturnType()).append(System.lineSeparator());
            builder.repeat("\t", indent + 1).append("Annotated type: ").append(method.getAnnotatedReturnType()).append(System.lineSeparator());
            builder.repeat("\t", indent + 1).append("Parameter count: ").append(method.getParameterCount()).append(System.lineSeparator());
            builder.repeat("\t", indent + 1).append("Parameters: ").append(String.join(", ", Arrays.stream(method.getParameterTypes()).map(Class::getName).toList())).append(System.lineSeparator());
        }

        Set<Class<?>> superClasses = this.superClasses(clazz);
        builder.repeat("\t", indent).append("Super classes: ").append(System.lineSeparator());
        for (Class<?> superClass : superClasses) {
            builder.repeat("\t", indent + 1).append(debugClass(indent + 1, superClass)).append(System.lineSeparator());
        }

        return builder.toString();
    }

    public Class<?>[] interfaces(Class<?> clazz) {
        return clazz.getInterfaces();
    }

    public Set<Class<?>> superClasses(Class<?> clazz) {
        return this.superClasses(clazz, false);
    }

    public Set<Class<?>> superClasses(Class<?> clazz, boolean self) {
        Set<Class<?>> superClasses = new HashSet<>();
        if (self) {
            superClasses.add(clazz);
        }
        while (true) {
            clazz = clazz.getSuperclass();
            if (clazz == null || clazz == Object.class) {
                break;
            }

            superClasses.add(clazz);
        }

        return superClasses;
    }
}
