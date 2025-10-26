package com.artillexstudios.axapi.items;

public interface HashedStack {

    boolean matches(WrappedItemStack stack, HashGenerator generator);
}
