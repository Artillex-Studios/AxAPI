package com.artillexstudios.axapi.utils;

import net.kyori.adventure.key.Key;

public interface ClientAsset {


    record ResourceTexture(Key id, Key texturePath) implements Texture {

        public ResourceTexture(Key id) {
            this(id, id != null ? Key.key(id.namespace(), "textures/" + id.key() + ".png") : null);
        }

    }

    interface Texture {

        Key texturePath();
    }
}
