package com.artillexstudios.axapi.libraries;

import java.net.URL;
import java.net.URLClassLoader;

public class RelocationClassLoader extends URLClassLoader {
    static {
        ClassLoader.registerAsParallelCapable();
    }

    public RelocationClassLoader(URL[] urls) {
        super(urls);
    }
}
