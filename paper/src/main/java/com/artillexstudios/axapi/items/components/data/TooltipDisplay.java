package com.artillexstudios.axapi.items.components.data;

import com.artillexstudios.axapi.items.components.DataComponent;

import java.util.Collection;

public record TooltipDisplay(boolean hideTooltip, Collection<DataComponent<?>> hiddenComponents) {
}
