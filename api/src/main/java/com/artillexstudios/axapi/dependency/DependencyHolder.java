package com.artillexstudios.axapi.dependency;

public record DependencyHolder<T>(T dependency, boolean isDefault) {
}
