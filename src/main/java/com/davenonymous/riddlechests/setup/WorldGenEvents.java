package com.davenonymous.riddlechests.setup;


import com.davenonymous.riddlechests.event.GenerateChestEvent;
import com.davenonymous.riddlechests.util.Logz;
import net.minecraft.block.BlockState;
import net.minecraft.block.ChestBlock;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.LockableLootTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

import java.lang.reflect.Field;

public class WorldGenEvents {
    private static Field FIELD_LOOTTABLE;

    static {
        FIELD_LOOTTABLE = ObfuscationReflectionHelper.findField(LockableLootTileEntity.class, "field_184284_m");
        if(FIELD_LOOTTABLE != null) {
            FIELD_LOOTTABLE.setAccessible(true);
        }
    }

    private static ResourceLocation getLootTableFromLockableLootTileEntity(LockableLootTileEntity tile) {
        try {
            return (ResourceLocation) FIELD_LOOTTABLE.get(tile);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return null;
    }

    @SubscribeEvent
    public static void onGenerateChest(final GenerateChestEvent event) {
        TileEntity tile = event.getWorld().getTileEntity(event.getPos());
        if(tile == null) {
            return;
        }

        // Isn't a lockable chest or something in the first place
        if(!(tile instanceof LockableLootTileEntity)) {
            return;
        }

        // Is blacklisted because of its loottable?
        ResourceLocation originalLocation = getLootTableFromLockableLootTileEntity((LockableLootTileEntity)tile);
        Config.updateBlacklistedChests();
        if(originalLocation != null && Config.blacklistedChests.contains(originalLocation)) {
            return;
        }

        // Check chance against config value
        double rolled = event.getWorld().getRandom().nextDouble();
        if(rolled > Config.CHANCE_TO_REPLACE_CHEST.get() && false) {
            return;
        }

        // Actually replace the chest

        // Has original direction?
        Direction newDirection = Direction.byHorizontalIndex(event.getWorld().getRandom().nextInt(4));
        BlockState originalState = event.getWorld().getBlockState(event.getPos());
        if(originalState.has(ChestBlock.FACING)) {
            newDirection = originalState.get(ChestBlock.FACING);
        }

        // Create blockstate and place in the world
        BlockState newState = ModObjects.RIDDLECHEST.getDefaultState().with(BlockStateProperties.HORIZONTAL_FACING, newDirection);
        event.getWorld().removeBlock(event.getPos(), false);
        event.getWorld().setBlockState(event.getPos(), newState, 3);
    }
}
