package com.artillexstudios.axapi.serializers;

public interface Serializer<T> {

    String serialize(T object);

    T deserialize(String value);
}
