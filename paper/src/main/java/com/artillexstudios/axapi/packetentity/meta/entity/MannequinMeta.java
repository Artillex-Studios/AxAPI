package com.artillexstudios.axapi.packetentity.meta.entity;

import com.artillexstudios.axapi.items.component.type.ProfileProperties;
import com.artillexstudios.axapi.packetentity.meta.Metadata;
import com.artillexstudios.axapi.packetentity.meta.serializer.Accessors;
import com.artillexstudios.axapi.utils.GameProfile;
import com.artillexstudios.axapi.utils.PlayerSkin;
import com.artillexstudios.axapi.utils.ResolvableProfile;

import java.util.Optional;
import java.util.UUID;

public class MannequinMeta extends AvatarMeta {

    public MannequinMeta(Metadata metadata) {
        super(metadata);
    }

    @Override
    protected void defineDefaults() {
        super.defineDefaults();
        this.metadata.define(Accessors.PROFILE, new ResolvableProfile(new GameProfile("", UUID.randomUUID(), new ProfileProperties(UUID.randomUUID(), "")), PlayerSkin.emptyPatch()));
        this.metadata.define(Accessors.IMMOVABLE, false);
        this.metadata.define(Accessors.DESCRIPTION, Optional.empty());
    }
}
