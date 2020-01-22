package com.davenonymous.riddlechests.recipe.loottable;

import com.davenonymous.libnonymous.base.BaseRecipeHelper;
import com.davenonymous.riddlechests.setup.ModObjects;
import net.minecraft.item.crafting.RecipeManager;
import net.minecraft.util.ResourceLocation;

import java.util.Set;
import java.util.stream.Collectors;

public class LootTableMappingRecipeHelper extends BaseRecipeHelper<LootTableMappingInfo> {
    public LootTableMappingRecipeHelper() {
        super(ModObjects.lootTableMappingRecipeType);
    }

    public Set<ResourceLocation> getLootTablesForCategory(ResourceLocation category, RecipeManager manager) {
        return this.getRecipeStream(manager).filter(m -> m.categoryId.equals(category)).flatMap(m -> m.mappedLootTables.stream()).collect(Collectors.toSet());
    }
}
