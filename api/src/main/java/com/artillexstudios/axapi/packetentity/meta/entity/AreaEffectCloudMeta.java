package com.artillexstudios.axapi.packetentity.meta.entity;

import com.artillexstudios.axapi.packetentity.meta.EntityMeta;
import com.artillexstudios.axapi.packetentity.meta.Metadata;
import com.artillexstudios.axapi.packetentity.meta.serializer.Accessors;
import com.artillexstudios.axapi.utils.ParticleArguments;
import org.bukkit.Particle;

public class AreaEffectCloudMeta extends EntityMeta {

    public AreaEffectCloudMeta(Metadata metadata) {
        super(metadata);
    }

    public void radius(float radius) {
        this.metadata.set(Accessors.AREA_EFFECT_CLOUD_RADIUS, radius);
    }

    public float radius() {
        return this.metadata.get(Accessors.AREA_EFFECT_CLOUD_RADIUS);
    }

    public void color(int color) {
        this.metadata.set(Accessors.AREA_EFFECT_CLOUD_COLOR, color);
    }

    public int color() {
        return this.metadata.get(Accessors.AREA_EFFECT_CLOUD_COLOR);
    }

    public void point(boolean point) {
        this.metadata.set(Accessors.AREA_EFFECT_CLOUD_POINT, point);
    }

    public boolean point() {
        return this.metadata.get(Accessors.AREA_EFFECT_CLOUD_POINT);
    }

    public void particle(ParticleArguments particle) {
        this.metadata.set(Accessors.AREA_EFFECT_CLOUD_PARTICLE, particle);
    }

    public ParticleArguments particle() {
        return this.metadata.get(Accessors.AREA_EFFECT_CLOUD_PARTICLE);
    }

    @Override
    protected void defineDefaults() {
        super.defineDefaults();
        this.metadata.define(Accessors.AREA_EFFECT_CLOUD_RADIUS, 0.5f);
        this.metadata.define(Accessors.AREA_EFFECT_CLOUD_COLOR, 0);
        this.metadata.define(Accessors.AREA_EFFECT_CLOUD_POINT, false);
        this.metadata.define(Accessors.AREA_EFFECT_CLOUD_PARTICLE, new ParticleArguments(Particle.CLOUD, null));
    }
}
