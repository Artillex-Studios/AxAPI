package com.artillexstudios.axapi.serializers;

public interface Serializer<T, Z> {

    Z serialize(T object);

    T deserialize(Z value);
}
