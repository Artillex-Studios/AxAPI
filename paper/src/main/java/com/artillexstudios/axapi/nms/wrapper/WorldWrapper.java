package com.artillexstudios.axapi.nms.wrapper;

import com.artillexstudios.axapi.selection.BlockSetter;
import com.artillexstudios.axapi.selection.ParallelBlockSetter;
import org.bukkit.World;

import java.util.List;

public interface WorldWrapper extends Wrapper<World> {

    static WorldWrapper wrap(Object world) {
        return WrapperRegistry.WORLD.map(world);
    }

    BlockSetter setter();

    ParallelBlockSetter parallelSetter();

    List<ServerPlayerWrapper> players();
}
