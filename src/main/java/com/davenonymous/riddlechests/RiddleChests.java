package com.davenonymous.riddlechests;

import com.davenonymous.riddlechests.config.CommonConfig;
import com.davenonymous.riddlechests.setup.ClientSetup;
import com.davenonymous.riddlechests.setup.ForgeEventHandlers;
import com.davenonymous.riddlechests.setup.ModSetup;
import com.davenonymous.riddlechests.setup.Registration;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
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

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        var recipeManager = event.getServer().getRecipeManager();

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