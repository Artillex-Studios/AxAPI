package com.artillexstudios.axapi.utils;

import com.artillexstudios.axapi.dependency.DependencyContainer;

public interface Nameable {

    static Nameable getInstance() {
        return DependencyContainer.getInstance(Nameable.class);
    }

    String getName();
}
