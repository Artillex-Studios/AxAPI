package com.artillexstudios.axapi.items.components.data;

import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionType;

import java.util.List;
import java.util.Optional;

public record PotionContents(Optional<PotionType> type, Optional<Integer> color, List<PotionEffect> effects, Optional<String> name) {

}
