package com.artillexstudios.axapi.packetentity.meta;

import com.artillexstudios.axapi.packetentity.meta.serializer.Accessors;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Pose;

import java.util.Objects;
import java.util.Optional;

public class EntityMeta {
    protected final Metadata metadata;

    public EntityMeta(Metadata metadata) {
        this.metadata = metadata;

        this.defineDefaults0();
    }

    public void reset() {
        this.metadata.reset();
    }

    public void resetSilently() {
        this.metadata.resetSilently();
    }

    public void silent(boolean silent) {
        this.metadata.set(Accessors.SILENT, silent);
    }

    public boolean silent() {
        return this.metadata.get(Accessors.SILENT);
    }

    public void hasNoGravity(boolean hasNoGravity) {
        this.metadata.set(Accessors.HAS_NO_GRAVITY, hasNoGravity);
    }

    public boolean hasNoGravity() {
        return this.metadata.get(Accessors.HAS_NO_GRAVITY);
    }

    public boolean customNameVisible() {
        return this.metadata.get(Accessors.CUSTOM_NAME_VISIBLE);
    }

    public void customNameVisible(boolean customNameVisible) {
        this.metadata.set(Accessors.CUSTOM_NAME_VISIBLE, customNameVisible);
    }

    public void name(Component name) {
        if (name == null || name == Component.empty()) {
            this.metadata.set(Accessors.CUSTOM_NAME, Optional.empty());
            return;
        }

        Optional<Component> metaComponent = this.metadata.get(Accessors.CUSTOM_NAME);
        Component component = metaComponent.orElse(null);
        if (!Objects.equals(name, component)) {
            this.metadata.set(Accessors.CUSTOM_NAME, Optional.of(name));
        }
    }

    public void sneaking(boolean sneaking) {
        setSharedFlag(1, sneaking);
    }

    public void onFire(boolean onFire) {
        setSharedFlag(0, onFire);
    }

    public void sprinting(boolean sprinting) {
        setSharedFlag(3, sprinting);
    }

    public void swimming(boolean swimming) {
        setSharedFlag(4, swimming);
    }

    public void invisible(boolean invisible) {
        setSharedFlag(5, invisible);
    }

    public void glowing(boolean glowing) {
        setSharedFlag(6, glowing);
    }

    public void elytraFlying(boolean elytra) {
        setSharedFlag(7, elytra);
    }

    public boolean elytraFlying() {
        return getSharedFlag(7);
    }

    public boolean glowing() {
        return getSharedFlag(6);
    }

    public boolean invisible() {
        return getSharedFlag(5);
    }

    public boolean swimming() {
        return getSharedFlag(4);
    }

    public boolean sprinting() {
        return getSharedFlag(3);
    }

    public boolean onFire() {
        return getSharedFlag(0);
    }

    public boolean sneaking() {
        return getSharedFlag(1);
    }

    public Optional<Component> name() {
        return this.metadata.get(Accessors.CUSTOM_NAME);
    }

    private boolean getSharedFlag(int index) {
        return (this.metadata.get(Accessors.SHARED_FLAGS) & 1 << index) != 0;
    }

    private void setSharedFlag(int index, boolean value) {
        byte b0 = this.metadata.get(Accessors.SHARED_FLAGS);
        if (value) {
            this.metadata.set(Accessors.SHARED_FLAGS, (byte) (b0 | 1 << index));
        } else {
            this.metadata.set(Accessors.SHARED_FLAGS, (byte) (b0 & ~(1 << index)));
        }
    }

    public Metadata metadata() {
        return metadata;
    }

    private void defineDefaults0() {
        this.metadata.define(Accessors.SHARED_FLAGS, (byte) 0);
        this.metadata.define(Accessors.CUSTOM_NAME, Optional.empty());
        this.metadata.define(Accessors.CUSTOM_NAME_VISIBLE, false);
        this.metadata.define(Accessors.SILENT, false);
        this.metadata.define(Accessors.POSE, Pose.STANDING);
        this.metadata.define(Accessors.HAS_NO_GRAVITY, false);

        this.defineDefaults();
    }

    protected void defineDefaults() {

    }
}
