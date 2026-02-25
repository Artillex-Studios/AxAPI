package com.artillexstudios.axapi.particle.option;

import com.artillexstudios.axapi.particle.ParticleOption;
import com.artillexstudios.axapi.utils.Vector3f;

public record TrailParticleOption(double x, double y, double z, Vector3f color, int duration) implements ParticleOption {
}
