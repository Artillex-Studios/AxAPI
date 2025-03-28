package com.artillexstudios.axapi.nms.wrapper;

import com.artillexstudios.axapi.selection.BlockSetter;
import com.artillexstudios.axapi.selection.ParallelBlockSetter;
import org.bukkit.World;

import java.util.List;

public interface WorldWrapper extends Wrapper<World> {

    BlockSetter setter();

    ParallelBlockSetter parallelSetter();

    List<ServerPlayerWrapper> players();
}
