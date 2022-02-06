package com.davenonymous.riddlechests.recipe.loottable;



import com.davenonymous.libnonymous.helper.BaseRecipeHelper;
import com.davenonymous.riddlechests.setup.Registration;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeManager;

import java.util.Set;
import java.util.stream.Collectors;

public class LootTableMappingRecipeHelper extends BaseRecipeHelper<LootTableMappingInfo> {
    public LootTableMappingRecipeHelper() {
        super(Registration.lootMappingRecipeType);
    }

    public Set<ResourceLocation> getLootTablesForCategory(ResourceLocation category, RecipeManager manager) {
        return this.getRecipeStream(manager).filter(m -> m.categoryId.equals(category)).flatMap(m -> m.mappedLootTables.stream()).collect(Collectors.toSet());
    }
}