package com.artillexstudios.axapi.particle.option;

import com.artillexstudios.axapi.particle.ParticleOption;
import com.artillexstudios.axapi.utils.Vector3f;

public record DustColorTransitionParticleOption(Vector3f color1, Vector3f color2, float scale) implements ParticleOption {
}
