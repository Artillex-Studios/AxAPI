package com.artillexstudios.axapi.hologram;

import com.artillexstudios.axapi.collections.ThreadSafeList;

public class HologramPage {
    private final ThreadSafeList<HologramContent<?, ?>> contents = new ThreadSafeList<>();
}
