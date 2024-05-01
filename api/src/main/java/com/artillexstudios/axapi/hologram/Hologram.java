package com.artillexstudios.axapi.hologram;

import com.artillexstudios.axapi.collections.ThreadSafeList;
import com.artillexstudios.axapi.utils.ClassUtils;
import com.artillexstudios.axapi.utils.Pair;
import com.artillexstudios.axapi.utils.placeholder.Placeholder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.WeakHashMap;

public class Hologram {
    private final WeakHashMap<Player, Integer> playerPages = new WeakHashMap<>();
    private final ObjectArrayList<Placeholder> placeholders = new ObjectArrayList<>(5);
    private final ThreadSafeList<HologramPage> pages = new ThreadSafeList<>();
    private final double lineSpace;
    private Location location;
    private String id;

    public Hologram(Location location, String id) {
        this(location, id, 0.75);
    }

    public Hologram(Location location, String id, double lineSpace) {
        this.lineSpace = lineSpace;
        this.location = location;
        this.id = id;

        if (ClassUtils.INSTANCE.classExists("me.clip.placeholderapi.PlaceholderAPI")) {
            addPlaceholder(new Placeholder(PlaceholderAPI::setPlaceholders));
        }
    }

    public void teleport(Location location) {
        this.location = location;

        for (int i = 0; i < pages.size(); i++) {
            HologramPage page = pages.get(i);
            page.realign();
        }
    }

    public void addLines(List<String> content, HologramLine.Type type) {
        for (String s : content) {
            addLine(s, type);
        }
    }

    public void addLine(String content, HologramLine.Type type) {
        HologramPage page;
        if (pages.isEmpty()) {
            page = newPage();
        } else {
            page = pages.get(0);
        }

        page.addLine(content, type);
    }

    public void addLines(List<Pair<String, HologramLine.Type>> lines) {
        for (Pair<String, HologramLine.Type> line : lines) {
            addLine(line.getFirst(), line.getSecond());
        }
    }

    public void remove() {
        for (int i = 0; i < pages.size(); i++) {
            HologramPage page = pages.get(i);
            page.remove();
        }
    }

    /**
     * Set a line of a hologram
     * @param pageIndex The page, starting at 0
     * @param lineIndex The line, starting at 0
     * @param content The content
     */
    public void setLine(int pageIndex, int lineIndex, String content) {
        HologramPage page = pages.get(pageIndex);
        HologramLine line = page.getLine(lineIndex);
        line.setContent(content);
    }

    public void addPlaceholder(Placeholder placeholder) {
        this.placeholders.add(placeholder);

        for (int i = 0; i < pages.size(); i++) {
            HologramPage page = this.pages.get(i);
            page.addPlaceholder(placeholder);
        }
    }

    public ObjectArrayList<Placeholder> placeholders() {
        return placeholders;
    }

    public HologramPage newPage() {
        HologramPage page = new HologramPage(this);
        addPage(page);
        return page;
    }

    public void setLine(int lineIndex, String content) {
        setLine(0, lineIndex, content);
    }

    public void addPage(HologramPage page) {
        this.pages.add(page);

        for (int i = 0; i < placeholders.size(); i++) {
            Placeholder placeholder = this.placeholders.get(i);
            page.addPlaceholder(placeholder);
        }
    }

    public void removePage(HologramPage page) {
        this.pages.remove(page);
    }


    public HologramPage page(int pageIndex) {
        return this.pages.get(pageIndex);
    }

    public void changePage(Player player, PageChangeDirection direction) {
        if (pages.isEmpty() || pages.size() == 1) {
            // No other page
            return;
        }

        Integer page = playerPages.get(player);

        if (direction == PageChangeDirection.BACK) {
            int current = page == null ? 0 : page;

            if (current - 1 <= 0) {
                // Already on first page
                return;
            }

            HologramPage previousPage = pages.get(current);
            previousPage.hide(player);

            HologramPage newPage = pages.get(current - 1);
            newPage.show(player);
            playerPages.put(player, current - 1);
        } else {
            HologramPage previousPage = pages.get(page == null ? 0 : page);
            previousPage.hide(player);

            HologramPage newPage = pages.get((page == null ? 0 : page) + 1);
            newPage.show(player);
            playerPages.put(player, (page == null ? 0 : page) + 1);
        }
    }

    public double lineSpace() {
        return lineSpace;
    }

    public Location location() {
        return location;
    }

    public String id() {
        return id;
    }

    public enum PageChangeDirection {
        BACK,
        FORWARD
    }
}
