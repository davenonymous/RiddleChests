package com.davenonymous.riddlechests.datagen.server;

import com.davenonymous.riddlechests.RiddleChests;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class DatagenItemTags extends ItemTagsProvider {
	public DatagenItemTags(DataGenerator generator, BlockTagsProvider blockTags, ExistingFileHelper existingFileHelper) {
		super(generator, blockTags, RiddleChests.MODID, existingFileHelper);
	}

	@Override
	protected void addTags() {

	}

	@Override
	public String getName() {
		return "Riddle Chests Item Tags";
	}
}