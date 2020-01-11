package com.davenonymous.riddlechests.event;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraftforge.eventbus.api.Event;

public class GenerateChestEvent extends Event {
    private final IWorld world;
    private final BlockPos pos;

    public GenerateChestEvent(IWorld world, BlockPos pos) {
        this.world = world;
        this.pos = pos;
    }

    public IWorld getWorld() {
        return world;
    }

    public BlockPos getPos() {
        return pos;
    }
}
