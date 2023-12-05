package com.artillexstudios.axapi.hologram;

import com.artillexstudios.axapi.hologram.impl.ComponentHologramLine;
import com.artillexstudios.axapi.hologram.impl.ItemStackHologramLine;
import com.artillexstudios.axapi.hologram.impl.SkullHologramLine;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.block.Skull;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public abstract class AbstractHologram implements Hologram {
    private final ObjectArrayList<HologramLine<?>> lines = new ObjectArrayList<>(20);
    private final double lineHeight;
    private final String id;
    private final UUID uuid;
    private Location location;

    public AbstractHologram(Location location, String id, double lineHeight) {
        this.location = location;
        this.id = id;
        this.lineHeight = lineHeight;
        this.uuid = UUID.randomUUID();
    }

    @NotNull
    @Override
    public UUID getID() {
        return this.uuid;
    }

    private Location getLocationRel(int line) {
        return location.clone().add(0, -lineHeight * line, 0);
    }

    @Override
    public <T> void addLine(@NotNull T content) {
        Location location = getLocationRel(lines.size());
        HologramLine<T> line = addLine(location, content);
        line.set(content);

        lines.add(line);
    }

    @Override
    public <T> void setLine(int line, @NotNull T content) {
        HologramLine<?> hologramLine = lines.get(line);

        if (hologramLine instanceof ComponentHologramLine && content instanceof Component) {
            ComponentHologramLine holoLine = (ComponentHologramLine) hologramLine;
            Component component = (Component) content;
            holoLine.set(component);
        } else if (hologramLine instanceof ItemStackHologramLine && content instanceof ItemStack) {
            ItemStackHologramLine holoLine = (ItemStackHologramLine) hologramLine;
            ItemStack itemStack = (ItemStack) content;
            holoLine.set(itemStack);
        } else if (hologramLine instanceof SkullHologramLine && content instanceof Skull) {
            SkullHologramLine holoLine = (SkullHologramLine) hologramLine;
            Skull skull = (Skull) content;
            holoLine.set(skull);
        }
    }

    @Override
    public HologramLine<?> getLine(int line) {
        return lines.get(line);
    }

    @NotNull
    @Override
    public Location getLocation() {
        return this.location;
    }

    @Override
    public void teleport(@NotNull Location location) {
        this.location = location;

        for (int i = 0; i < lines.size(); i++) {
            HologramLine<?> line = lines.get(i);
            line.teleport(getLocationRel(i));
        }
    }

    @Override
    public void show(@NotNull Player player) {
        for (HologramLine<?> line : lines) {
            line.show(player);
        }
    }

    @Override
    public void hide(@NotNull Player player) {
        for (HologramLine<?> line : lines) {
            line.hide(player);
        }
    }

    @Override
    public void remove() {
        for (HologramLine<?> line : lines) {
            line.remove();
        }
    }

    @Override
    public void removeLine(int line) {
        HologramLine<?> hologramLine = this.lines.remove(line);
        hologramLine.remove();
        teleport(location);
    }

    @Override
    public ObjectArrayList<HologramLine<?>> getLines() {
        return lines;
    }

    protected abstract <T> HologramLine<T> addLine(Location location, T content);

    public String getId() {
        return id;
    }
}
