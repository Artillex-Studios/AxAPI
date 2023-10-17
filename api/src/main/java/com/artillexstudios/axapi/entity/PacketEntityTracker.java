package com.artillexstudios.axapi.entity;

import com.artillexstudios.axapi.entity.impl.PacketEntity;

public interface PacketEntityTracker {

    PacketEntity getById(int id);

    void addEntity(PacketEntity entity);

    void removeEntity(PacketEntity entity);

    void process();
}
