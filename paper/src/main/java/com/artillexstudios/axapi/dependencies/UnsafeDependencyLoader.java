package com.artillexstudios.axapi.dependencies;

import com.artillexstudios.axapi.reflection.FieldAccessor;
import com.artillexstudios.axapi.utils.UncheckedUtils;
import com.artillexstudios.axapi.utils.logging.LogUtils;
import sun.misc.Unsafe;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;
import java.util.List;

public final class UnsafeDependencyLoader {
    private final Unsafe unsafe = FieldAccessor.builder()
            .disableAccessChecking()
            .withClass(Unsafe.class)
            .withField("theUnsafe")
            .build()
            .get(null, Unsafe.class);
    private final FieldAccessor urlClassPathAccessor = FieldAccessor.builder()
            .disableAccessChecking()
            .withClass(URLClassLoader.class)
            .withField("ucp")
            .build();
    private final FieldAccessor unopenedUrlsAccessor = FieldAccessor.builder()
            .disableAccessChecking()
            .withClass("jdk.internal.loader.URLClassPath")
            .withField("unopenedUrls")
            .build();
    private final FieldAccessor pathAccessor = FieldAccessor.builder()
            .disableAccessChecking()
            .withClass("jdk.internal.loader.URLClassPath")
            .withField("path")
            .build();

    private void addURL(URLClassLoader classLoader, URL url) {
        long urlClassPathOffset = this.unsafe.objectFieldOffset(this.urlClassPathAccessor.getReflectedField());
        Object classPath = this.unsafe.getObject(classLoader, urlClassPathOffset);
        long unopenedUrlsOffset = this.unsafe.objectFieldOffset(this.unopenedUrlsAccessor.getReflectedField());
        List<URL> unopenedUrls = UncheckedUtils.unsafeCast(this.unsafe.getObject(classPath, unopenedUrlsOffset));
        long pathOffset = this.unsafe.objectFieldOffset(this.pathAccessor.getReflectedField());
        List<URL> path = UncheckedUtils.unsafeCast(this.unsafe.getObject(classPath, pathOffset));
        unopenedUrls.add(url);
        path.add(url);
    }

    public void loadUnsafeLibrary(URLClassLoader classLoader, Path path) {
        try {
            this.addURL(classLoader, path.toUri().toURL());
        } catch (MalformedURLException exception) {
            LogUtils.error("An exception occurred while loading library!", exception);
        }
    }
}
