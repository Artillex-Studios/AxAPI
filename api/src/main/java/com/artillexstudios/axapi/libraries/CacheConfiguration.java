package com.artillexstudios.axapi.libraries;

import com.artillexstudios.axapi.config.annotation.ConfigurationPart;

import java.util.ArrayList;
import java.util.List;

public class CacheConfiguration implements ConfigurationPart {
    public static List<Library> libraries = new ArrayList<>();
    public static List<Relocation> relocations = new ArrayList<>();
    public static List<Repository> repositories = new ArrayList<>();
}
