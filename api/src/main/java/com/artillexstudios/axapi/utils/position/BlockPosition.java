package com.artillexstudios.axapi.utils.position;

public interface BlockPosition {

    static BlockPosition of(long packedPos) {
        return new ImmutableBlockPosition(packedPos);
    }

    int x();

    int y();

    int z();

    long asLong();

    ImmutableBlockPosition immutable();

    MutableBlockPosition mutable();
}
