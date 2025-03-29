package com.artillexstudios.shared.axapi.packet;

import com.artillexstudios.shared.axapi.utils.Version;

public record PacketType(String name, Version from, Version to) {
}
