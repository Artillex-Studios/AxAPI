package com.artillexstudios.axapi.hologram;

/**
 * Represents a content of a page of hologram.
 *  his can represent multiple types of data.
 *
 * @param <T> The datatype.
 * @param <Z> The HologramContentType for the datatype.
 */
public interface HologramContent<T, Z extends HologramContentType<T>> {

    /**
     * Get the data of this content;
     * @return The data of this content.
     */
    T data();

    /**
     * Get the type of the content.
     * @return The HologramContentType of this content.
     */
    Z type();

    /**
     * If the content contains placeholders.
     * @return A boolean, describing if this content contains placeholders.
     */
    boolean containsPlaceholders();

    /**
     * Method for determining if the content of this HologramContent
     * equals the content of the other.
     * @param other The HologramContent to check data equality with.
     * @return A boolean, describing if the two contents contain the same data.
     */
    boolean dataEquals(HologramContent<?, ?> other);
}
