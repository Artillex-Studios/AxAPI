package com.artillexstudios.axapi.hologram;

import com.artillexstudios.axapi.collections.ThreadSafeList;

public abstract class Hologram {
    private final ThreadSafeList<HologramPage> pages = new ThreadSafeList<>();


}
