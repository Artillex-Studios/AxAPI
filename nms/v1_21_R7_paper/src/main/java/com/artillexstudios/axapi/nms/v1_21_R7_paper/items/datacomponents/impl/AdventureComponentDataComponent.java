package com.artillexstudios.axapi.nms.v1_21_R7_paper.items.datacomponents.impl;

import com.artillexstudios.axapi.utils.ComponentSerializer;
import net.kyori.adventure.text.Component;

public final class AdventureComponentDataComponent implements DataComponentHandler<Component, net.minecraft.network.chat.Component> {

    @Override
    public net.minecraft.network.chat.Component toNMS(Component from) {
        return ComponentSerializer.INSTANCE.toVanilla(from);
    }

    @Override
    public Component fromNMS(net.minecraft.network.chat.Component data) {
        return ComponentSerializer.INSTANCE.fromVanilla(data);
    }
}
