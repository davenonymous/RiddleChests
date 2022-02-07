package com.davenonymous.riddlechests.config;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;

import java.util.Arrays;
import java.util.List;

public class CommonConfig {
	public static final String CATEGORY_GENERAL = "general";

	private static final ForgeConfigSpec.Builder COMMON_BUILDER = new ForgeConfigSpec.Builder();

	public static ForgeConfigSpec COMMON_CONFIG;

	public static ForgeConfigSpec.IntValue ADDITIONAL_CHARS;
	public static ForgeConfigSpec.DoubleValue CHANCE_TO_REPLACE_CHEST;
	public static ForgeConfigSpec.ConfigValue<List<String>> NEVER_REPLACE_LOOTTABLES;
	public static ForgeConfigSpec.ConfigValue<List<String>> DISABLE_RIDDLE_CATEGORIES;

	public static void register() {
		COMMON_BUILDER.comment("General Settings").push(CATEGORY_GENERAL);
		setupGeneralConfig(COMMON_BUILDER);
		COMMON_BUILDER.pop();
		COMMON_CONFIG = COMMON_BUILDER.build();

		ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, COMMON_CONFIG);
	}

	private static void setupGeneralConfig(ForgeConfigSpec.Builder b) {
		ADDITIONAL_CHARS = b
				.comment("How many extra characters for each riddle?")
				.defineInRange("extraChars", 9, 1, 25);

		CHANCE_TO_REPLACE_CHEST = b
				.comment("How likely is a vanilla loot chest being replaced by a riddle chest?")
				.defineInRange("chestReplaceChance", 0.125d, 0.0d, 1.0d);

		NEVER_REPLACE_LOOTTABLES = b
				.comment("Never replace vanilla chests that have one of these loot tables assigned")
				.define("loottableBlacklist", Arrays.asList("minecraft:chests/shipwreck_map", "minecraft:chests/spawn_bonus_chest"), o -> true);

		DISABLE_RIDDLE_CATEGORIES = b
				.comment("Categories listed here will not be loaded. Add 'riddlechests:betrayal_at_krondor' to disable the built in riddles. Make sure to have at least one loaded category!")
				.define("disableCategories", Arrays.asList(""), o -> true);
	}

}