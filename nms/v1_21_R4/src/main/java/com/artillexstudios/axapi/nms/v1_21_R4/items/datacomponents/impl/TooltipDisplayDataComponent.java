package com.artillexstudios.axapi.nms.v1_21_R4.items.datacomponents.impl;

import com.artillexstudios.axapi.items.components.data.TooltipDisplay;
import it.unimi.dsi.fastutil.objects.ReferenceLinkedOpenHashSet;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.stream.Collectors;

public final class TooltipDisplayDataComponent implements DataComponentHandler<TooltipDisplay, net.minecraft.world.item.component.TooltipDisplay> {

    @Override
    public net.minecraft.world.item.component.TooltipDisplay toNMS(TooltipDisplay from) {
        return new net.minecraft.world.item.component.TooltipDisplay(from.hideTooltip(), from.hiddenComponents().stream().map(component -> {
                return BuiltInRegistries.DATA_COMPONENT_TYPE.get(ResourceLocation.fromNamespaceAndPath("minecraft", component)).orElseThrow().value();
            }).collect(Collectors.toCollection(ReferenceLinkedOpenHashSet::new))
        );
    }

    @Override
    public TooltipDisplay fromNMS(net.minecraft.world.item.component.TooltipDisplay data) {
        return new TooltipDisplay(data.hideTooltip(), data.hiddenComponents().stream().map(component -> {
                return BuiltInRegistries.DATA_COMPONENT_TYPE.getKey(component).getPath();
            }).collect(Collectors.toCollection(ArrayList::new))
        );
    }
}
