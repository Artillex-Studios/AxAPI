package com.artillexstudios.axapi.packetentity.meta.entity;

import com.artillexstudios.axapi.items.component.type.ProfileProperties;
import com.artillexstudios.axapi.packetentity.meta.Metadata;
import com.artillexstudios.axapi.packetentity.meta.serializer.Accessors;
import com.artillexstudios.axapi.utils.GameProfile;
import com.artillexstudios.axapi.utils.PlayerSkin;
import com.artillexstudios.axapi.utils.ResolvableProfile;
import net.kyori.adventure.text.Component;

import java.util.Optional;
import java.util.UUID;

public class MannequinMeta extends AvatarMeta {

    public MannequinMeta(Metadata metadata) {
        super(metadata);
    }

    public ResolvableProfile resolvableProfile() {
        return this.metadata.get(Accessors.PROFILE);
    }

    public void resolvableProfile(ResolvableProfile resolvableProfile) {
        this.metadata.set(Accessors.PROFILE, resolvableProfile);
    }

    public boolean immovable() {
        return this.metadata.get(Accessors.IMMOVABLE);
    }

    public void immovable(boolean immovable) {
        this.metadata.set(Accessors.IMMOVABLE, immovable);
    }

    public Optional<Component> description() {
        return this.metadata.get(Accessors.DESCRIPTION);
    }

    public void description(Optional<Component> description) {
        this.metadata.set(Accessors.DESCRIPTION, description);
    }

    @Override
    protected void defineDefaults() {
        super.defineDefaults();
        this.metadata.define(Accessors.PROFILE, new ResolvableProfile(new GameProfile("", UUID.randomUUID(), new ProfileProperties(UUID.randomUUID(), "")), PlayerSkin.emptyPatch()));
        this.metadata.define(Accessors.IMMOVABLE, false);
        this.metadata.define(Accessors.DESCRIPTION, Optional.empty());
    }
}
