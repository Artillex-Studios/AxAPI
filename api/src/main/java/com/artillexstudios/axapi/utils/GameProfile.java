package com.artillexstudios.axapi.utils;

import com.artillexstudios.axapi.items.component.type.ProfileProperties;

import java.util.UUID;

public record GameProfile(String name, UUID uuid, ProfileProperties properties) {
}
