package com.artillexstudios.axapi.entity.impl;

import org.bukkit.Color;
import org.bukkit.Particle;

public interface PacketAreaEffectCloud extends PacketEntity {

    void setRadius(float radius);

    void setPoint(boolean point);

    void setColor(Color color);

    <T> void setParticle(Particle particle, T data);
}
