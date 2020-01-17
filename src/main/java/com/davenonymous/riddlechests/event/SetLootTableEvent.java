package com.davenonymous.riddlechests.event;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.Event;

public class SetLootTableEvent extends Event {
    private final IWorld world;
    private final BlockPos pos;

    public SetLootTableEvent(IWorld world, BlockPos pos) {
        this.world = world;
        this.pos = pos;
    }

    public IWorld getWorld() {
        return world;
    }

    public BlockPos getPos() {
        return pos;
    }

    public static void fireEvent(IBlockReader world, BlockPos pos) {
        if(world instanceof IWorld) {
            MinecraftForge.EVENT_BUS.post(new SetLootTableEvent((IWorld)world, pos));
        }
    }
}
