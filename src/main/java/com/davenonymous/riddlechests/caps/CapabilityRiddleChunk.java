package com.davenonymous.riddlechests.caps;

import com.davenonymous.libnonymous.reflections.ChestBlockEntityReflection;
import com.davenonymous.riddlechests.RiddleChests;
import com.davenonymous.riddlechests.config.CommonConfig;
import com.davenonymous.riddlechests.setup.Registration;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.TickTask;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class CapabilityRiddleChunk {
	public static final ResourceLocation RIDDLE_CHUNK = new ResourceLocation(RiddleChests.MODID, "riddle_chunk");

	public static final Capability<IRiddleChunk> RIDDLE_CHUNK_CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {});

	@SubscribeEvent
	public static void registerCapability(RegisterCapabilitiesEvent event) {
		event.register(IRiddleChunk.class);
	}

	public static void riddleChunk(LevelChunk chunk, ServerLevel level) {
		for(var entry : chunk.getBlockEntities().entrySet()) {
			var pos = entry.getKey();
			var entity = entry.getValue();
			if(entity instanceof ChestBlockEntity chest) {
				BlockState originalState = chunk.getBlockState(pos);
				final Direction newDirection = originalState.hasProperty(ChestBlock.FACING) ? originalState.getValue(ChestBlock.FACING) : Direction.NORTH;

				level.getServer().tell(new TickTask(1, () -> {
					RiddleChests.LOGGER.debug("Maybe replacing chest at {}", pos);
					var lootTable = ChestBlockEntityReflection.getLootTableFromChest(chest);

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

					ChestBlockEntityReflection.removeLootTableFromChest(chest);
					chunk.removeBlockEntity(pos);
					chunk.setBlockState(pos, newState, false);
				}));
			}
		}
	}
}