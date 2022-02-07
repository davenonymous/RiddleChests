package com.davenonymous.riddlechests.setup;


import com.davenonymous.libnonymous.reflections.ChestBlockEntityReflection;
import com.davenonymous.riddlechests.command.ModCommands;
import com.davenonymous.riddlechests.config.CommonConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import static net.minecraft.world.level.block.Block.UPDATE_ALL;

public class ForgeEventHandlers {

	@SubscribeEvent
	public void onRegisterCommands(RegisterCommandsEvent event) {
		ModCommands.register(event.getDispatcher());
	}

	@SubscribeEvent
	public void onPlayerUse(PlayerInteractEvent.RightClickBlock event) {
		if(event.getWorld().isClientSide()) {
			return;
		}

		var state = event.getWorld().getBlockState(event.getPos());
		if(state.getBlock() != Blocks.CHEST) {
			return;
		}

		var entity = event.getWorld().getBlockEntity(event.getPos());
		if(entity instanceof ChestBlockEntity chest) {
			ResourceLocation lootTable = ChestBlockEntityReflection.getLootTableFromChest(chest);
			if(lootTable != null) {
				if(tryReplaceChest(event.getWorld(), event.getPos())) {
					// TODO: Play some particle effect & aaaaaaaaaaa sound
					event.setCanceled(true);
				}
			}
		}

		return;
	}

	public static boolean tryReplaceChest(Level world, BlockPos pos) {

		BlockEntity tile = world.getBlockEntity(pos);
		if(tile == null) {
			return false;
		}

		// Isn't a lockable chest or something in the first place
		if(!(tile instanceof RandomizableContainerBlockEntity chest)) {
			return false;
		}

		// TODO: Test: Is blacklisted because of its loottable?
		var lootTable = ChestBlockEntityReflection.getLootTableFromChest(chest);
		var ignoreLootTables = CommonConfig.NEVER_REPLACE_LOOTTABLES.get();
		if(lootTable != null && ignoreLootTables.contains(lootTable.toString())) {
			return false;
		}

		// Check chance against config value
		double rolled = world.getRandom().nextDouble();
		if(rolled > CommonConfig.CHANCE_TO_REPLACE_CHEST.get()) {
			return false;
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
		ChestBlockEntityReflection.removeLootTableFromChest(chest);
		world.removeBlock(pos, false);
		world.setBlock(pos, newState, UPDATE_ALL);
		return true;
	}
}