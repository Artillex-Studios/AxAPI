package com.artillexstudios.axapi.entity.impl;

import com.artillexstudios.axapi.utils.RotationType;
import org.bukkit.util.EulerAngle;

public interface PacketArmorStand extends PacketLivingEntity {

    void setSmall(boolean small);

    void setMarker(boolean marker);

    void setHasArms(boolean hasArms);

    void setHasBasePlate(boolean hasBasePlate);

    void setRotation(RotationType rotationType, EulerAngle rotation);
}
