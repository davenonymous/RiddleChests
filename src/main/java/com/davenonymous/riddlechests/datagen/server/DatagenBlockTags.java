package com.davenonymous.riddlechests.datagen.server;


import com.davenonymous.riddlechests.RiddleChests;
import com.davenonymous.riddlechests.setup.Registration;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.tags.BlockTags;
import net.minecraftforge.common.data.ExistingFileHelper;

public class DatagenBlockTags extends BlockTagsProvider {
	public DatagenBlockTags(DataGenerator generator, ExistingFileHelper existingFileHelper) {
		super(generator, RiddleChests.MODID, existingFileHelper);
	}

	@Override
	protected void addTags() {
		m_206424_(BlockTags.MINEABLE_WITH_AXE).add(Registration.RIDDLECHEST.get());
		m_206424_(BlockTags.NEEDS_IRON_TOOL).add(Registration.RIDDLECHEST.get());
	}

	@Override
	public String getName() {
		return "Riddle Chests BlockTags";
	}
}