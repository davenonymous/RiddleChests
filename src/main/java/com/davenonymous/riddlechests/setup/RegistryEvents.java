package com.davenonymous.riddlechests.setup;

import com.davenonymous.libnonymous.base.RecipeData;
import com.davenonymous.riddlechests.recipe.alphabets.AlphabetInfo;
import com.davenonymous.riddlechests.recipe.alphabets.AlphabetRecipeHelper;
import com.davenonymous.riddlechests.recipe.loottable.LootTableMappingInfo;
import com.davenonymous.riddlechests.recipe.loottable.LootTableMappingRecipeHelper;
import com.davenonymous.riddlechests.recipe.riddles.RiddleInfo;
import com.davenonymous.riddlechests.recipe.riddles.RiddleRecipeHelper;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.client.event.RegisterClientReloadListenersEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static com.davenonymous.riddlechests.RiddleChests.MODID;

// You can use EventBusSubscriber to automatically subscribe events on the contained class (this is subscribing to the MOD
// Event bus for receiving Registry Events)
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class RegistryEvents {

    @SubscribeEvent
    public static void createNewRegistry(RegistryEvent.NewRegistry event) {
        Registration.riddleRecipeType = createRecipeType(RiddleInfo.class, "word");
        Registration.alphabetRecipeType = createRecipeType(AlphabetInfo.class, "alphabet");
        Registration.lootMappingRecipeType = createRecipeType(LootTableMappingInfo.class, "loottable_mapping");

        Registration.riddleRecipeHelper = new RiddleRecipeHelper();
        Registration.alphabetRecipeHelper = new AlphabetRecipeHelper();
        Registration.lootMappingRecipeHelper = new LootTableMappingRecipeHelper();
    }

    private static <T extends RecipeData> RecipeType<T> createRecipeType(Class<T> type, String id) {
        ResourceLocation recipeTypeResource = new ResourceLocation(MODID, id);
        RecipeType<T> reg = Registry.register(Registry.RECIPE_TYPE, recipeTypeResource, new RecipeType<T>() {
            @Override
            public String toString() {
                return recipeTypeResource.toString();
            }
        });

        return reg;
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