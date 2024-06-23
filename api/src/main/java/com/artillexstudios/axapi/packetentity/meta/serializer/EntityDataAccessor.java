package com.artillexstudios.axapi.packetentity.meta.serializer;

public final class EntityDataAccessor<T> {
    private final int id;
    private final EntityDataSerializers<T> serializers;

    public EntityDataAccessor(int id, EntityDataSerializers<T> serializers) {
        this.id = id;
        this.serializers = serializers;
    }

    public int id() {
        return id;
    }

    public EntityDataSerializers<T> serializers() {
        return serializers;
    }
}
