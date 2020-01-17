package com.davenonymous.riddlechests.setup;


import com.davenonymous.riddlechests.event.GenerateChestEvent;
import com.davenonymous.riddlechests.event.SetLootTableEvent;
import net.minecraft.block.BlockState;
import net.minecraft.block.ChestBlock;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.LockableLootTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.gen.WorldGenRegion;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class WorldGenEvents {
    private static Field FIELD_LOOTTABLE;
    private static Map<Integer, Set<BlockPos>> alreadyProcessedChests;

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

    private static void addProcessedChestPosition(WorldGenRegion world, BlockPos pos) {
        if(alreadyProcessedChests == null) {
            alreadyProcessedChests = new HashMap<>();
        }

        int dim = world.getDimension().getType().getId();
        if(!alreadyProcessedChests.containsKey(dim)) {
            alreadyProcessedChests.put(dim, new HashSet<>());
        }

        alreadyProcessedChests.get(dim).add(pos);
    }

    private static boolean alreadyProcessed(WorldGenRegion world, BlockPos pos) {
        if(alreadyProcessedChests == null) {
            return false;
        }

        int dim = world.getDimension().getType().getId();
        if(!alreadyProcessedChests.containsKey(dim)) {
            return false;
        }

        return alreadyProcessedChests.get(dim).contains(pos);
    }

    private static void tryReplaceChest(WorldGenRegion world, BlockPos pos) {
        if(alreadyProcessed(world, pos)) {
            return;
        }

        addProcessedChestPosition(world, pos);

        TileEntity tile = world.getTileEntity(pos);
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
        double rolled = world.getRandom().nextDouble();
        if(rolled > Config.CHANCE_TO_REPLACE_CHEST.get()) {
            return;
        }

        // Actually replace the chest

        // Has original direction?
        Direction newDirection = Direction.byHorizontalIndex(world.getRandom().nextInt(4));
        BlockState originalState = world.getBlockState(pos);
        if(originalState.has(ChestBlock.FACING)) {
            newDirection = originalState.get(ChestBlock.FACING);
        }

        // Create blockstate and place in the world
        BlockState newState = ModObjects.RIDDLECHEST.getDefaultState().with(BlockStateProperties.HORIZONTAL_FACING, newDirection);
        world.removeBlock(pos, false);
        world.setBlockState(pos, newState, 3);
    }

    @SubscribeEvent
    public static void onSetLootTable(final SetLootTableEvent event) {
        if(event.getWorld() instanceof WorldGenRegion) {
            tryReplaceChest((WorldGenRegion) event.getWorld(), event.getPos());
        }
    }

    @SubscribeEvent
    public static void onGenerateChest(final GenerateChestEvent event) {
        if(event.getWorld() instanceof WorldGenRegion) {
            tryReplaceChest((WorldGenRegion) event.getWorld(), event.getPos());
        }
    }
}
