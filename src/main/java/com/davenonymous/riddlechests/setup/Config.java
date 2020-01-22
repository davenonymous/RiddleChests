package com.davenonymous.riddlechests.setup;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;

import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

@Mod.EventBusSubscriber
public class Config {
    public static final String CATEGORY_GENERAL = "general";

    private static final ForgeConfigSpec.Builder COMMON_BUILDER = new ForgeConfigSpec.Builder();
    private static final ForgeConfigSpec.Builder CLIENT_BUILDER = new ForgeConfigSpec.Builder();

    public static ForgeConfigSpec COMMON_CONFIG;
    public static ForgeConfigSpec CLIENT_CONFIG;

    public static ForgeConfigSpec.IntValue ADDITIONAL_CHARS;
    public static ForgeConfigSpec.DoubleValue CHANCE_TO_REPLACE_CHEST;
    public static ForgeConfigSpec.ConfigValue<List<String>> NEVER_REPLACE_LOOTTABLES;
    public static ForgeConfigSpec.ConfigValue<List<String>> DISABLE_RIDDLE_CATEGORIES;

    static {
        COMMON_BUILDER.comment("General Settings").push(CATEGORY_GENERAL);
        setupGeneralConfig(COMMON_BUILDER);
        COMMON_BUILDER.pop();
        COMMON_CONFIG = COMMON_BUILDER.build();

        CLIENT_BUILDER.comment("General Settings").push(CATEGORY_GENERAL);
        setupClientConfig(CLIENT_BUILDER);
        CLIENT_BUILDER.pop();
        CLIENT_CONFIG = CLIENT_BUILDER.build();
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

    private static void setupClientConfig(ForgeConfigSpec.Builder b) {
    }

    public static void loadConfig(ForgeConfigSpec spec, Path path) {
        final CommentedFileConfig configData = CommentedFileConfig.builder(path)
                .sync()
                .autosave()
                .writingMode(WritingMode.REPLACE)
                .build();

        configData.load();
        spec.setConfig(configData);
    }

    @SubscribeEvent
    public static void onLoad(final ModConfig.Loading configEvent) {
    }

    @SubscribeEvent
    public static void onReload(final ModConfig.ConfigReloading configEvent) {
    }

    public static void updateBlacklistedChests() {
        blacklistedChests = NEVER_REPLACE_LOOTTABLES.get().stream()
                .map(ResourceLocation::tryCreate)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }

    public static Set<ResourceLocation> blacklistedChests;
}
