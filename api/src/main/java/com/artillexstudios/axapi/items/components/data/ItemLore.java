package com.artillexstudios.axapi.items.components.data;

import net.kyori.adventure.text.Component;

import java.util.List;

public sealed interface ItemLore permits ItemLore.Unstyled, ItemLore.Styled {

    static ItemLore create(List<Component> lines) {
        return new ItemLore.Unstyled(lines);
    }

    static ItemLore create(List<Component> lines, List<Component> styled) {
        return new ItemLore.Styled(lines, styled);
    }

    record Unstyled(List<Component> lines) implements ItemLore {

    }

    record Styled(List<Component> lines, List<Component> styledLines) implements ItemLore {

    }
}
