package com.davenonymous.riddlechests.setup;


import com.davenonymous.riddlechests.RiddleChests;
import com.davenonymous.riddlechests.caps.CapabilityRiddleChunk;
import com.davenonymous.riddlechests.caps.RiddledCapabilityProvider;
import com.davenonymous.riddlechests.command.ModCommands;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraftforge.client.event.RecipesUpdatedEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ForgeEventHandlers {

	@SubscribeEvent
	public void attachToChunks(AttachCapabilitiesEvent<LevelChunk> event) {
		event.addCapability(CapabilityRiddleChunk.RIDDLE_CHUNK, new RiddledCapabilityProvider());
	}

	@SubscribeEvent
	public void onChunkLoad(ChunkEvent.Load event) {
		var world = event.getWorld();
		var chunk = event.getChunk();
		if(world.isClientSide() || !(world instanceof ServerLevel level)) {
			return;
		}

		if(!(chunk instanceof LevelChunk levelChunk)) {
			return;
		}

		var optionalRiddleChunk = levelChunk.getCapability(CapabilityRiddleChunk.RIDDLE_CHUNK_CAPABILITY);
		if(optionalRiddleChunk.isPresent()) {
			var riddle = optionalRiddleChunk.resolve().get();
			if(!riddle.isRiddled()) {
				CapabilityRiddleChunk.riddleChunk(levelChunk, level);
				riddle.setRiddled();
				chunk.setUnsaved(true);
			}
		}
	}

	@SubscribeEvent
	public void onRegisterCommands(RegisterCommandsEvent event) {
		ModCommands.register(event.getDispatcher());
	}

	@SubscribeEvent
	public void onRecipesUpdated(RecipesUpdatedEvent event) {
		var recipeManager = event.getRecipeManager();

		if(!Registration.riddleRecipeHelper.hasRecipes(recipeManager)) {
			RiddleChests.LOGGER.warn("Warning. No riddles loaded! This mod will not work properly!");
		}

		if(!Registration.alphabetRecipeHelper.hasRecipes(recipeManager)) {
			RiddleChests.LOGGER.warn("Warning. No alphabets loaded! This mod will not work properly!");
		}

		if(!Registration.lootMappingRecipeHelper.hasRecipes(recipeManager)) {
			RiddleChests.LOGGER.warn("Warning. No loot table mappings loaded! This mod will not work properly!");
		}

		RiddleChests.LOGGER.info("Loaded {} riddles, {} loot table mappings and {} alphabets",
				Registration.riddleRecipeHelper.getRecipeCount(recipeManager),
				Registration.lootMappingRecipeHelper.getRecipeCount(recipeManager),
				Registration.alphabetRecipeHelper.getRecipeCount(recipeManager)
		);
	}
}