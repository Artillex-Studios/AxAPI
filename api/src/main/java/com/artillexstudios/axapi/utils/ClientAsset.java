package com.artillexstudios.axapi.utils;

import net.kyori.adventure.key.Key;

public interface ClientAsset {


    record ResourceTexture(Key id, Key texturePath) implements Texture {

    }

    interface Texture {

        Key texturePath();
    }
}
