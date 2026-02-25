package com.artillexstudios.axapi.nms.wrapper;

public interface Wrapper<T> {

    default void update() {
        update(false);
    }

    void update(boolean force);

    T wrapped();

    Object asMinecraft();
}
