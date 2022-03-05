package com.davenonymous.riddlechests.setup;

import com.davenonymous.riddlechests.recipe.alphabets.AlphabetRecipeHelper;
import com.davenonymous.riddlechests.recipe.loottable.LootTableMappingRecipeHelper;
import com.davenonymous.riddlechests.recipe.riddles.RiddleRecipeHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.client.event.RegisterClientReloadListenersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

import static com.davenonymous.riddlechests.RiddleChests.MODID;

// You can use EventBusSubscriber to automatically subscribe events on the contained class (this is subscribing to the MOD
// Event bus for receiving Registry Events)
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class RegistryEvents {

    @SubscribeEvent
    static void onCommonSetup(FMLCommonSetupEvent event) {
        Registration.riddleRecipeType = RecipeType.register(new ResourceLocation(MODID, "word").toString());
        Registration.alphabetRecipeType = RecipeType.register(new ResourceLocation(MODID, "alphabet").toString());
        Registration.lootMappingRecipeType = RecipeType.register(new ResourceLocation(MODID, "loottable_mapping").toString());

        Registration.riddleRecipeHelper = new RiddleRecipeHelper();
        Registration.alphabetRecipeHelper = new AlphabetRecipeHelper();
        Registration.lootMappingRecipeHelper = new LootTableMappingRecipeHelper();
    }

    @SubscribeEvent
    public static void onClientReloadListener(RegisterClientReloadListenersEvent event) {
        var listener = new ResourceManagerReloadListener() {

            @Override
            public void onResourceManagerReload(ResourceManager pResourceManager) {

            }
        };
        event.registerReloadListener(listener);
    }


}