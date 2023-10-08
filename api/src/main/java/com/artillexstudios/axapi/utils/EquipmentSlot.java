package com.artillexstudios.axapi.utils;

public enum EquipmentSlot {
    MAIN_HAND(Type.HAND, 0),
    OFF_HAND(Type.HAND, 1),
    BOOTS(Type.ARMOR, 0),
    LEGGINGS(Type.ARMOR, 1),
    CHEST_PLATE(Type.ARMOR, 2),
    HELMET(Type.ARMOR, 3);

    private final Type type;
    private final int index;

    EquipmentSlot(Type type, int index) {
        this.type = type;
        this.index = index;
    }

    public Type getType() {
        return this.type;
    }

    public int getIndex() {
        return this.index;
    }

    public enum Type {
        HAND,
        ARMOR
    }
}
