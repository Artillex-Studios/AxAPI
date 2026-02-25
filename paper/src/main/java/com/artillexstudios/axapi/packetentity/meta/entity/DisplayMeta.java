package com.artillexstudios.axapi.packetentity.meta.entity;

import com.artillexstudios.axapi.packetentity.meta.EntityMeta;
import com.artillexstudios.axapi.packetentity.meta.Metadata;
import com.artillexstudios.axapi.packetentity.meta.serializer.Accessors;
import com.artillexstudios.axapi.utils.Quaternion;
import com.artillexstudios.axapi.utils.Vector3f;

public abstract class DisplayMeta extends EntityMeta {

    public DisplayMeta(Metadata metadata) {
        super(metadata);
    }

    public void positionInterpolationDuration(int duration) {
        this.metadata.set(Accessors.POSITION_INTERPOLATION_DURATION, duration);
    }

    public int positionInterpolationDuration() {
        return this.metadata.get(Accessors.POSITION_INTERPOLATION_DURATION);
    }

    public void transformationInterpolationDuration(int duration) {
        this.metadata.set(Accessors.TRANSFORMATION_INTERPOLATION_DURATION, duration);
    }

    public int transformationInterpolationDuration() {
        return this.metadata.get(Accessors.TRANSFORMATION_INTERPOLATION_DURATION);
    }

    public void billboardConstrain(BillboardConstrain constrain) {
        this.metadata.set(Accessors.BILLBOARD_CONSTRAIN, (byte) constrain.ordinal());
    }

    public BillboardConstrain billboardConstrain() {
        return BillboardConstrain.values()[this.metadata.get(Accessors.BILLBOARD_CONSTRAIN)];
    }

    public void scale(Vector3f scale) {
        this.metadata.set(Accessors.SCALE, scale);
    }

    public void translation(Vector3f translation) {
        this.metadata.set(Accessors.TRANSLATION, translation);
    }

    public Vector3f translation() {
        return this.metadata.get(Accessors.TRANSLATION);
    }

    public Vector3f scale() {
        return this.metadata.get(Accessors.SCALE);
    }

    public void interpolationDelay(int interpolationDelay) {
        this.metadata.set(Accessors.INTERPOLATION_DELAY, interpolationDelay);
    }

    public int interpolationDelay() {
        return this.metadata.get(Accessors.INTERPOLATION_DELAY);
    }

    public void rotationLeft(Quaternion translation) {
        this.metadata.set(Accessors.ROTATION_LEFT, translation);
    }

    public Quaternion rotationLeft() {
        return this.metadata.get(Accessors.ROTATION_LEFT);
    }

    public void rotationRight(Quaternion translation) {
        this.metadata.set(Accessors.ROTATION_RIGHT, translation);
    }

    public Quaternion rotationRight() {
        return this.metadata.get(Accessors.ROTATION_RIGHT);
    }

    public void brightnessOverride(int brightness) {
        this.metadata.set(Accessors.BRIGHTNESS_OVERRIDE, brightness);
    }

    public int brightnessOverride() {
        return this.metadata.get(Accessors.BRIGHTNESS_OVERRIDE);
    }

    public void viewRange(float viewRange) {
        this.metadata.set(Accessors.VIEW_RANGE, viewRange);
    }

    public float viewRange() {
        return this.metadata.get(Accessors.VIEW_RANGE);
    }

    public void shadowRadius(float shadowRadius) {
        this.metadata.set(Accessors.SHADOW_RADIUS, shadowRadius);
    }

    public float shadowRadius() {
        return this.metadata.get(Accessors.SHADOW_RADIUS);
    }

    public void shadowStrength(float shadowStrength) {
        this.metadata.set(Accessors.SHADOW_STRENGTH, shadowStrength);
    }

    public float shadowStrength() {
        return this.metadata.get(Accessors.SHADOW_STRENGTH);
    }

    public void width(float width) {
        this.metadata.set(Accessors.WIDTH, width);
    }

    public float width() {
        return this.metadata.get(Accessors.WIDTH);
    }

    public void height(float height) {
        this.metadata.set(Accessors.HEIGHT, height);
    }

    public float height() {
        return this.metadata.get(Accessors.HEIGHT);
    }

    public void glowColorOverride(int glowColorOverride) {
        this.metadata.set(Accessors.GLOW_COLOR_OVERRIDE, glowColorOverride);
    }

    public int glowColorOverride() {
        return this.metadata.get(Accessors.GLOW_COLOR_OVERRIDE);
    }

    protected void defineDefaults() {
        this.metadata.define(Accessors.INTERPOLATION_DELAY, 0);
        this.metadata.define(Accessors.TRANSFORMATION_INTERPOLATION_DURATION, 0);
        this.metadata.define(Accessors.POSITION_INTERPOLATION_DURATION, 0);
        this.metadata.define(Accessors.TRANSLATION, new Vector3f(0, 0, 0));
        this.metadata.define(Accessors.SCALE, new Vector3f(1, 1, 1));
        this.metadata.define(Accessors.ROTATION_LEFT, new Quaternion(0, 0, 0, 1));
        this.metadata.define(Accessors.ROTATION_RIGHT, new Quaternion(0, 0, 0, 1));
        this.metadata.define(Accessors.BILLBOARD_CONSTRAIN, (byte) 0);
        this.metadata.define(Accessors.BRIGHTNESS_OVERRIDE, -1);
        this.metadata.define(Accessors.VIEW_RANGE, 1.0f);
        this.metadata.define(Accessors.SHADOW_RADIUS, 0.0f);
        this.metadata.define(Accessors.SHADOW_STRENGTH, 1.0f);
        this.metadata.define(Accessors.WIDTH, 0.0f);
        this.metadata.define(Accessors.HEIGHT, 0.0f);
        this.metadata.define(Accessors.GLOW_COLOR_OVERRIDE, -1);
    }

    public enum BillboardConstrain {
        FIXED,
        VERTICAL,
        HORIZONTAL,
        CENTER;
    }
}
