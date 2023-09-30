package com.artillexstudios.axapi.nms.v1_19_R2.hologram;

import com.artillexstudios.axapi.hologram.AbstractHologram;
import com.artillexstudios.axapi.hologram.HologramLine;
import org.bukkit.Location;
import org.bukkit.block.Skull;
import org.bukkit.inventory.ItemStack;

public class Hologram extends AbstractHologram {

    public Hologram(Location location, String id, double lineHeight) {
        super(location, id, lineHeight);
    }

    @Override
    protected <T> HologramLine<T> addLine(Location location, T content) {
        if (content instanceof ItemStack) {
            return (HologramLine<T>) new ItemStackHologramLine(location);
        } else if (content instanceof Skull) {
            return (HologramLine<T>) new SkullHologramLine(location);
        }

        return (HologramLine<T>) new ComponentHologramLine(location);
    }
}
