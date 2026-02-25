package com.artillexstudios.axapi.packet;

import com.artillexstudios.axapi.utils.Version;

public record PacketType(String name, Version from, Version to) {
}
