package com.artillexstudios.axapi.packet.data;

public enum SignTextSlot {
    BACK,
    FRONT;

    public static SignTextSlot get(boolean front) {
        return front ? FRONT : BACK;
    }
}
