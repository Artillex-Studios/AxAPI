package com.artillexstudios.axapi.utils;

public class ResolvableProfile {
    private final GameProfile partialProfile;
    private final PlayerSkin.Patch patch;

    public ResolvableProfile(GameProfile partialProfile, PlayerSkin.Patch patch) {
        this.partialProfile = partialProfile;
        this.patch = patch;
    }
}
