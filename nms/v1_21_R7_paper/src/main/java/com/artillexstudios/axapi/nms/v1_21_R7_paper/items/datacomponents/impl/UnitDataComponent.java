package com.artillexstudios.axapi.nms.v1_21_R7_paper.items.datacomponents.impl;

import com.artillexstudios.axapi.items.component.type.Unit;

public final class UnitDataComponent implements DataComponentHandler<Unit, net.minecraft.util.Unit> {

    @Override
    public net.minecraft.util.Unit toNMS(Unit from) {
        return from == null ? null : net.minecraft.util.Unit.INSTANCE;
    }

    @Override
    public Unit fromNMS(net.minecraft.util.Unit data) {
        return data == null ? null : Unit.INSTANCE;
    }
}
