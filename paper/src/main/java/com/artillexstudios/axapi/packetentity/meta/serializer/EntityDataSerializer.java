package com.artillexstudios.axapi.packetentity.meta.serializer;

import com.artillexstudios.axapi.packet.FriendlyByteBuf;

public interface EntityDataSerializer<T> {

    void write(FriendlyByteBuf buf, T value);

    T read(FriendlyByteBuf buf);

    EntityDataSerializers.Type type();

    default EntityDataAccessor<T> createAccessor(int i) {
        return new EntityDataAccessor<>(i, this);
    }
}
