package com.davenonymous.riddlechests;

import com.davenonymous.riddlechests.config.CommonConfig;
import com.davenonymous.riddlechests.setup.ClientSetup;
import com.davenonymous.riddlechests.setup.ForgeEventHandlers;
import com.davenonymous.riddlechests.setup.ModSetup;
import com.davenonymous.riddlechests.setup.Registration;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(RiddleChests.MODID)
public class RiddleChests {
    public static final Logger LOGGER = LogManager.getLogger();
    public static final String MODID = "riddlechests";

    public RiddleChests() {
        CommonConfig.register();

        Registration.init();

        MinecraftForge.EVENT_BUS.register(new ForgeEventHandlers());

        IEventBus modbus = FMLJavaModLoadingContext.get().getModEventBus();
        modbus.addListener(ModSetup::init);
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> modbus.addListener(ClientSetup::init));

    }


    /*
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

     */
}