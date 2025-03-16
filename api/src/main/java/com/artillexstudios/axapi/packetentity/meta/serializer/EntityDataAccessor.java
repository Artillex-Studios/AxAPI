package com.artillexstudios.axapi.packetentity.meta.serializer;

public record EntityDataAccessor<T>(int id, EntityDataSerializers<T> serializers) {
}
