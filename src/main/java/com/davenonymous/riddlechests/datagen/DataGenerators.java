package com.davenonymous.riddlechests.datagen;

import com.davenonymous.riddlechests.RiddleChests;
import com.davenonymous.riddlechests.datagen.server.DatagenBlockTags;
import com.davenonymous.riddlechests.datagen.server.DatagenItemTags;
import com.davenonymous.riddlechests.datagen.server.DatagenLootTables;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;

@Mod.EventBusSubscriber(modid = RiddleChests.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGenerators {
	private static void generateServerData(DataGenerator generator, ExistingFileHelper existingFileHelper) {
		var blockTagsProvider = new DatagenBlockTags(generator, existingFileHelper);
		generator.addProvider(blockTagsProvider);
		generator.addProvider(new DatagenItemTags(generator, blockTagsProvider, existingFileHelper));
		generator.addProvider(new DatagenLootTables(generator));

	}

	private static void generateClientData(DataGenerator generator, ExistingFileHelper existingFileHelper) {
	}

	@SubscribeEvent
	public static void gatherData(GatherDataEvent event) {
		var generator = event.getGenerator();
		if(event.includeServer()) {
			generateServerData(generator, event.getExistingFileHelper());
		}

		if(event.includeClient()) {
			generateClientData(generator, event.getExistingFileHelper());
		}
	}
}