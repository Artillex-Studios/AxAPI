package com.artillexstudios.axapi.utils.sound;

import net.kyori.adventure.key.Key;

public class SoundEvent {
    private final Key resourceLocation;
    private final float range;
    private final boolean useNewSystem;

    public static SoundEvent createVariableRange(Key resourceLocation) {
        return new SoundEvent(resourceLocation, 16.0f, false);
    }

    public static SoundEvent createFixedRange(Key resourceLocation, float range) {
        return new SoundEvent(resourceLocation, range, true);
    }

    SoundEvent(Key resourceLocation, float range, boolean useNewSystem) {
        this.resourceLocation = resourceLocation;
        this.range = range;
        this.useNewSystem = useNewSystem;
    }

    public Key getResourceLocation() {
        return this.resourceLocation;
    }

    public float getRange() {
        return this.range;
    }

    public boolean isUseNewSystem() {
        return this.useNewSystem;
    }
}
