package com.davenonymous.riddlechests.caps.replacers;

import com.davenonymous.riddlechests.RiddleChests;
import com.davenonymous.riddlechests.config.CommonConfig;
import com.davenonymous.riddlechests.setup.Registration;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.TickTask;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.chunk.LevelChunk;
import noobanidus.mods.lootr.api.blockentity.ILootBlockEntity;

@ChestReplacer(mod = "lootr")
public class Lootr implements IChestReplacer {
	@Override
	public boolean replaceChest(BlockPos pos, BlockEntity entity, LevelChunk chunk, ServerLevel level) {
		if(!(entity instanceof ILootBlockEntity chest)) {
			return false;
		}

		BlockState originalState = chunk.getBlockState(pos);
		final Direction newDirection = originalState.hasProperty(ChestBlock.FACING) ? originalState.getValue(ChestBlock.FACING) : Direction.NORTH;

		level.getServer().tell(new TickTask(1, () -> {
			RiddleChests.LOGGER.debug("Maybe replacing lootr chest at {}", pos);
			var lootTable = chest.getTable();

			// Only replace loot chests
			if(lootTable == null) {
				RiddleChests.LOGGER.debug("  - Not replacing chests without loottable!");
				return;
			}

			var ignoreLootTables = CommonConfig.NEVER_REPLACE_LOOTTABLES.get();
			if(ignoreLootTables.contains(lootTable.toString())) {
				RiddleChests.LOGGER.debug("  - Not replacing chests with loottable: {}", lootTable.toString());
				return;
			}

			// Check chance against config value
			double rolled = level.getRandom().nextDouble();
			if(rolled > CommonConfig.CHANCE_TO_REPLACE_CHEST.get()) {
				RiddleChests.LOGGER.debug("  - Rolled to low: {} <= {}", rolled, CommonConfig.CHANCE_TO_REPLACE_CHEST.get());
				return;
			}

			RiddleChests.LOGGER.debug("Actually replacing chest at {}", pos);
			BlockState newState = Registration.RIDDLECHEST.get().defaultBlockState().setValue(BlockStateProperties.HORIZONTAL_FACING, newDirection);

			chunk.removeBlockEntity(pos);
			chunk.setBlockState(pos, newState, false);
		}));

		return true;
	}
}