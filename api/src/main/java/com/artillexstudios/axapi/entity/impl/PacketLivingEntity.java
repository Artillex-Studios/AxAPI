package com.artillexstudios.axapi.entity.impl;

import com.artillexstudios.axapi.utils.Hand;
import org.bukkit.Location;

public interface PacketLivingEntity extends PacketEntity {

    void setHandActive(boolean active);

    void setActiveHand(Hand hand);

    void setRiptide(boolean riptide);

    void setHealth(float health);

    void setSleepingBedLocation(Location location);

    void setArrows(int arrows);

    void setBeeStingers(int beeStingers);

    void setPotionEffectColor(int color);

    void setPotionEffectAmbient(boolean ambient);
}
