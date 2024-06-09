package com.artillexstudios.axapi.items.component;

public interface DataComponent<T> {

    void apply(Object item, T t);

    T get(Object item);

}
