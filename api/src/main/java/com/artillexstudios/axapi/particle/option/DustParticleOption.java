package com.artillexstudios.axapi.particle.option;

import com.artillexstudios.axapi.particle.ParticleOption;
import com.artillexstudios.axapi.utils.Vector3f;

public record DustParticleOption(Vector3f color, float scale) implements ParticleOption {
}
