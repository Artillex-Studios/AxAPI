package com.artillexstudios.axapi.nms.v1_20_R1.entity;

import com.google.common.base.Preconditions;
import net.minecraft.core.particles.ParticleTypes;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.craftbukkit.v1_20_R1.CraftParticle;
import org.bukkit.entity.EntityType;

import java.util.function.Consumer;

public class PacketAreaEffectCloud extends PacketEntity implements com.artillexstudios.axapi.entity.impl.PacketAreaEffectCloud {

    public PacketAreaEffectCloud(Location location, Consumer<com.artillexstudios.axapi.entity.impl.PacketEntity> consumer) {
        super(EntityType.AREA_EFFECT_CLOUD, location, consumer);
    }

    @Override
    public void setRadius(float radius) {
        this.data.set(EntityData.AREA_EFFECT_CLOUD_RADIUS, radius);
    }

    @Override
    public void setPoint(boolean point) {
        this.data.set(EntityData.AREA_EFFECT_CLOUD_AS_POINT, point);
    }

    @Override
    public void setColor(Color color) {
        this.data.set(EntityData.AREA_EFFECT_CLOUD_COLOR, color.asRGB());
    }

    @Override
    public <T> void setParticle(Particle particle, T data) {
        this.data.set(EntityData.AREA_EFFECT_CLOUD_PARTICLE, CraftParticle.toNMS(particle, data));
    }

    @Override
    public void defineEntityData() {
        super.defineEntityData();
        this.data.define(EntityData.AREA_EFFECT_CLOUD_RADIUS, 0.5f);
        this.data.define(EntityData.AREA_EFFECT_CLOUD_COLOR, 0);
        this.data.define(EntityData.AREA_EFFECT_CLOUD_AS_POINT, false);
        this.data.define(EntityData.AREA_EFFECT_CLOUD_PARTICLE, ParticleTypes.ENTITY_EFFECT);
    }
}
