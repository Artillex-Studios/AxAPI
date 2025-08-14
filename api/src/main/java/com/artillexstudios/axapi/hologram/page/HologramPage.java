package com.artillexstudios.axapi.hologram.page;

import com.artillexstudios.axapi.events.PacketEntityInteractEvent;
import com.artillexstudios.axapi.hologram.Hologram;
import com.artillexstudios.axapi.hologram.HologramType;
import com.artillexstudios.axapi.packetentity.meta.EntityMeta;
import com.artillexstudios.axapi.placeholders.PlaceholderParameters;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.function.Consumer;

/**
 * Represents a content of a page of hologram.
 * this can represent multiple types of data.
 * <br>
 * These need to be spawned with the {@link HologramPage#spawn()} method.
 *
 * @param <T> The datatype.
 * @param <Z> The HologramPage for the datatype.
 */
public abstract class HologramPage<T, Z extends HologramType<T>> {
    private final PlaceholderParameters parameters = new PlaceholderParameters();
    private Consumer<PacketEntityInteractEvent> clickHandler;
    private Consumer<EntityMeta> metaHandler;
    private final Hologram hologram;
    private final boolean firstPage;
    private Location location;

    public HologramPage(Hologram hologram, boolean firstPage, Location location) {
        this.hologram = hologram;
        this.firstPage = firstPage;
        this.location = location;
    }

    /**
     * Set the data of this page.
     * @param content The data of this page.
     */
    public abstract void setContent(T content);

    /**
     * Get the data of this page.
     * @return The data of this page.
     */
    public abstract T getContent();

    /**
     * Get the type of the page.
     * @return The HologramPage of this page.
     */
    public abstract Z getType();

    /**
     * If the content contains placeholders.
     * @return A boolean, describing if this page contains placeholders.
     */
    public abstract boolean containsPlaceholders();

    /**
     * Method for determining if the content of this HologramPage
     * equals the content of the other.
     * @param other The HologramPage to check data equality with.
     * @return A boolean, describing if the two pages contain the same data.
     */
    public abstract boolean contentEquals(HologramPage<?, ?> other);

    public abstract void hide(Player player);

    public abstract void show(Player player);

    public abstract void teleport(Location location);

    public abstract void remove();

    public abstract void update();

    public abstract EntityMeta getEntityMeta();

    public void setClickHandler(Consumer<PacketEntityInteractEvent> clickHandler) {
        this.clickHandler = clickHandler;
    }

    public Consumer<PacketEntityInteractEvent> getClickHandler() {
        return this.clickHandler;
    }

    public void setEntityMetaHandler(Consumer<EntityMeta> metaHandler) {
        this.metaHandler = metaHandler;
    }

    public Consumer<EntityMeta> getEntityMetaHandler() {
        return this.metaHandler;
    }

    public abstract boolean spawn();

    public PlaceholderParameters getParameters() {
        return this.parameters;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public boolean isFirstPage() {
        return this.firstPage;
    }

    public Location getLocation() {
        return this.location;
    }

    protected Hologram getHologram() {
        return this.hologram;
    }
}
