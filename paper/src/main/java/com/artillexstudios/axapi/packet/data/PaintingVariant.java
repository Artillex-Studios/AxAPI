package com.artillexstudios.axapi.packet.data;

import net.kyori.adventure.key.Key;

public record PaintingVariant(int index, int width, int height, Key key, boolean direct) {
}
