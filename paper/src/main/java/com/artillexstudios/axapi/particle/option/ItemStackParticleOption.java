package com.artillexstudios.axapi.particle.option;

import com.artillexstudios.axapi.items.WrappedItemStack;
import com.artillexstudios.axapi.particle.ParticleOption;

public record ItemStackParticleOption(WrappedItemStack stack) implements ParticleOption {
}
