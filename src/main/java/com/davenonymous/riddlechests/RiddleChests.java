package com.davenonymous.riddlechests;

import com.davenonymous.libnonymous.setup.IProxy;
import com.davenonymous.riddlechests.command.ModCommands;
import com.davenonymous.riddlechests.network.Networking;
import com.davenonymous.riddlechests.proxy.ProxyClient;
import com.davenonymous.riddlechests.proxy.ProxyServer;
import com.davenonymous.riddlechests.recipe.loottable.LootTableMappingRecipeHelper;
import com.davenonymous.riddlechests.setup.Config;
import com.davenonymous.riddlechests.setup.ModObjects;
import com.davenonymous.riddlechests.setup.WorldGenEvents;
import com.davenonymous.riddlechests.util.Logz;
import net.minecraft.item.crafting.RecipeManager;
import net.minecraft.resources.IReloadableResourceManager;
import net.minecraft.resources.IResourceManagerReloadListener;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerAboutToStartEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;

@Mod(RiddleChests.MODID)
public class RiddleChests {
    public static final String MODID = "riddlechests";

    public static IProxy proxy = DistExecutor.runForDist(() -> () -> new ProxyClient(), () -> () -> new ProxyServer());

    public RiddleChests() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, Config.CLIENT_CONFIG);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.COMMON_CONFIG);

        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        MinecraftForge.EVENT_BUS.register(this);

        Config.loadConfig(Config.CLIENT_CONFIG, FMLPaths.CONFIGDIR.get().resolve(MODID + "-client.toml"));
        Config.loadConfig(Config.COMMON_CONFIG, FMLPaths.CONFIGDIR.get().resolve(MODID + "-common.toml"));
    }

    private void setup(final FMLCommonSetupEvent event) {
        Networking.registerMessages();
        MinecraftForge.EVENT_BUS.register(WorldGenEvents.class);
        proxy.init();
    }

    @SubscribeEvent
    public void serverLoad(FMLServerStartingEvent event) {
        ModCommands.register(event.getCommandDispatcher());
    }

    @SubscribeEvent(priority = EventPriority.LOW)
    public void startServer(FMLServerAboutToStartEvent event) {
        IReloadableResourceManager manager = event.getServer().getResourceManager();
        manager.addReloadListener((IResourceManagerReloadListener) resourceManager -> {
            RecipeManager recipeManager = event.getServer().getRecipeManager();
            if(!ModObjects.riddleRecipeHelper.hasRecipes(recipeManager)) {
                Logz.warn("Warning. No riddles loaded! This mod will not work properly!");
            }
            if(!ModObjects.alphabetRecipeHelper.hasRecipes(recipeManager)) {
                Logz.warn("Warning. No alphabets loaded! This mod will not work properly!");
            }
            if(!ModObjects.lootTableMappingRecipeHelper.hasRecipes(recipeManager)) {
                Logz.warn("Warning. No loot table mappings loaded! This mod will not work properly!");
            }

            Logz.info("Loaded {} riddles, {} loot table mappings and {} alphabets",
                ModObjects.riddleRecipeHelper.getRecipeCount(recipeManager),
                ModObjects.lootTableMappingRecipeHelper.getRecipeCount(recipeManager),
                ModObjects.alphabetRecipeHelper.getRecipeCount(recipeManager)
            );
        });
    }
}
