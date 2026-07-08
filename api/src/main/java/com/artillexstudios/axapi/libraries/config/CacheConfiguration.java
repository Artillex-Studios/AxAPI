package com.artillexstudios.axapi.libraries.config;

import com.artillexstudios.axapi.config.annotation.ConfigurationPart;
import com.artillexstudios.axapi.libraries.Library;
import com.artillexstudios.axapi.libraries.Relocation;
import com.artillexstudios.axapi.libraries.Repository;

import java.util.ArrayList;
import java.util.List;

public class CacheConfiguration implements ConfigurationPart {
    public static List<Library> libraries = new ArrayList<>();
    public static List<Relocation> relocations = new ArrayList<>();
    public static List<Repository> repositories = new ArrayList<>();
}
