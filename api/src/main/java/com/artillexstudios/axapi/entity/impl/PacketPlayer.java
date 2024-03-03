package com.artillexstudios.axapi.entity.impl;

import com.artillexstudios.axapi.utils.Hand;

public interface PacketPlayer extends PacketLivingEntity {

    void setCapeEnabled(boolean cape);

    void setJacketEnabled(boolean jacket);

    void setLeftSleeveEnabled(boolean sleeve);

    void setRightSleeveEnabled(boolean sleeve);

    void setLeftPantsLegEnabled(boolean pants);

    void setRightPantsLegEnabled(boolean pants);

    void setHatEnabled(boolean hat);

    void setMainHand(Hand hand);
}
