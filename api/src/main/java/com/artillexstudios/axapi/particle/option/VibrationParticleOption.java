package com.artillexstudios.axapi.particle.option;

import com.artillexstudios.axapi.particle.ParticleOption;
import com.artillexstudios.axapi.utils.position.BlockPosition;

public record VibrationParticleOption(int source, BlockPosition position, int entityId, float eyeHeight,
                                      int ticks) implements ParticleOption {
}
