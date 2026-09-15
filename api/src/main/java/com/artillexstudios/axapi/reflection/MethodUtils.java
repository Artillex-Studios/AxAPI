package com.artillexstudios.axapi.reflection;

import com.artillexstudios.axapi.utils.UncheckedUtils;
import com.artillexstudios.axapi.utils.logging.LogUtils;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import org.jspecify.annotations.Nullable;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public enum MethodUtils {
    INSTANCE;

    private final LoadingCache<MethodCacheKey, Method> METHOD_CACHE = Caffeine.newBuilder()
            .maximumSize(50)
            .build(this::getMethod);

    private Method getMethod(MethodCacheKey key) {
        try {
            return key.clazz().getDeclaredMethod(key.method(), key.params());
        } catch (NoSuchMethodException exception) {
            LogUtils.error("Failed to find method {}!", key.method(), exception);
            return null;
        }
    }

    @Nullable
    public Method getMethod(Class<?> clazz, String method) {
        return this.getMethod(clazz, method, new Class[0]);
    }

    @Nullable
    public Method getMethod(Class<?> clazz, String method, Class<?>... params) {
        return this.METHOD_CACHE.get(new MethodCacheKey(clazz, method, params));
    }

    public <T> T invokeStaticMethod(Method method, Object... params) {
        return this.invokeMethod(method, null, params);
    }

    public <T> T invokeMethod(Method method, T instance, Object... params) {
        try {
            return UncheckedUtils.unsafeCast(method.invoke(instance, params));
        } catch (IllegalAccessException | InvocationTargetException exception) {
            LogUtils.error("Failed to invok method {} on instance {} with arguments {}!", method, instance, params);
            return null;
        }
    }

    private record MethodCacheKey(Class<?> clazz, String method, Class<?>[] params) {

    }
}
