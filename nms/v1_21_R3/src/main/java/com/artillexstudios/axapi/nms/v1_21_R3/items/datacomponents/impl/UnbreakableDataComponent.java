package com.artillexstudios.axapi.nms.v1_21_R3.items.datacomponents.impl;

import com.artillexstudios.axapi.items.component.type.Unit;
import net.minecraft.world.item.component.Unbreakable;

public final class UnbreakableDataComponent implements DataComponentHandler<Unit, Unbreakable> {

    @Override
    public Unbreakable toNMS(Unit from) {
        return new Unbreakable(false);
    }

    @Override
    public Unit fromNMS(Unbreakable data) {
        return Unit.INSTANCE;
    }
}