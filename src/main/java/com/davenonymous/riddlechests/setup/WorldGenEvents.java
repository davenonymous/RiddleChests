package com.davenonymous.riddlechests.setup;


import com.davenonymous.riddlechests.config.CommonConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;

import java.lang.reflect.Field;

import static net.minecraft.world.level.block.Block.UPDATE_ALL;

public class WorldGenEvents {
    private static Field FIELD_LOOTTABLE;

    static {
        FIELD_LOOTTABLE = ObfuscationReflectionHelper.findField(RandomizableContainerBlockEntity.class, "f_59605_");
        if(FIELD_LOOTTABLE != null) {
            FIELD_LOOTTABLE.setAccessible(true);
        }
    }

    private static ResourceLocation getLootTableFromLockableLootTileEntity(RandomizableContainerBlockEntity tile) {
        try {
            return (ResourceLocation) FIELD_LOOTTABLE.get(tile);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return null;
    }


    private static void tryReplaceChest(WorldGenRegion world, BlockPos pos) {


        BlockEntity tile = world.getBlockEntity(pos);
        if(tile == null) {
            return;
        }

        // Isn't a lockable chest or something in the first place
        if(!(tile instanceof RandomizableContainerBlockEntity)) {
            return;
        }

        // TODO: Is blacklisted because of its loottable?
        /*
        ResourceLocation originalLocation = getLootTableFromLockableLootTileEntity((LockableLootTileEntity)tile);
        CommonConfig.updateBlacklistedChests();
        if(originalLocation != null && CommonConfig.blacklistedChests.contains(originalLocation)) {
            return;
        }

         */

        // Check chance against config value
        double rolled = world.getRandom().nextDouble();
        if(rolled > CommonConfig.CHANCE_TO_REPLACE_CHEST.get()) {
            return;
        }

        // Actually replace the chest

        // Has original direction?
        Direction newDirection = Direction.from2DDataValue(world.getRandom().nextInt(4));
        BlockState originalState = world.getBlockState(pos);
        if(originalState.hasProperty(ChestBlock.FACING)) {
            newDirection = originalState.getValue(ChestBlock.FACING);
        }

        // Create blockstate and place in the world
        BlockState newState = Registration.RIDDLECHEST.get().defaultBlockState().setValue(BlockStateProperties.HORIZONTAL_FACING, newDirection);
        world.removeBlock(pos, false);
        world.setBlock(pos, newState, UPDATE_ALL);
    }
}