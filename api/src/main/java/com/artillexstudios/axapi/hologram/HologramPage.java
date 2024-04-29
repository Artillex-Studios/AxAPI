package com.artillexstudios.axapi.hologram;

import com.artillexstudios.axapi.collections.ThreadSafeList;
import com.artillexstudios.axapi.utils.placeholder.Placeholder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class HologramPage {
    private final ObjectArrayList<Placeholder> placeholders = new ObjectArrayList<>(5);
    private final ThreadSafeList<HologramLine> lines = new ThreadSafeList<>();
    private final Hologram hologram;

    public HologramPage(Hologram hologram) {
        this.hologram = hologram;
    }

    public void addPlaceholder(Placeholder placeholder) {
        this.placeholders.add(placeholder);

        for (int i = 0; i < lines.size(); i++) {
            HologramLine line = lines.get(i);
            line.addPlaceholder(placeholder);
        }
    }

    public ObjectArrayList<Placeholder> placeholders() {
        return placeholders;
    }

    public HologramLine getLine(int lineIndex) {
        return lines.get(lineIndex);
    }

    public boolean isFirstPage() {
        return hologram.page(0) == this;
    }

    public HologramLine addLine(String content, HologramLine.Type type) {
        HologramLine hologramLine = new HologramLine(this, getLocationRel(lines.size()), content, type);
        addLine(hologramLine);
        return hologramLine;
    }

    public void addLine(HologramLine line) {
        this.lines.add(line);

        ObjectArrayList<Placeholder> placeholders = hologram.placeholders();
        for (int i = 0; i < placeholders.size(); i++) {
            Placeholder placeholder = placeholders.get(i);
            line.addPlaceholder(placeholder);
        }

        for (int i = 0; i < this.placeholders.size(); i++) {
            Placeholder placeholder = this.placeholders.get(i);
            line.addPlaceholder(placeholder);
        }

        realign();
    }

    public void hide(Player player) {
        for (int i = 0; i < lines.size(); i++) {
            HologramLine line = lines.get(i);
            line.hide(player);
        }
    }

    public void show(Player player) {
        for (int i = 0; i < lines.size(); i++) {
            HologramLine line = lines.get(i);
            line.show(player);
        }
    }

    public void removeLine(int lineIndex) {
        lines.remove(lineIndex);
    }

    public void realign() {
        for (int i = 0; i < lines.size(); i++) {
            HologramLine line = lines.get(i);
            line.teleport(getLocationRel(i));
        }
    }

    public void remove() {
        for (int i = 0; i < lines.size(); i++) {
            HologramLine line = lines.get(i);
            line.remove();
        }

        lines.clear();
    }

    public Hologram hologram() {
        return hologram;
    }

    public ThreadSafeList<HologramLine> lines() {
        return lines;
    }

    private Location getLocationRel(int line) {
        return hologram.location().clone().add(0, -hologram.lineSpace() * line, 0);
    }
}
