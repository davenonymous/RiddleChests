package com.davenonymous.riddlechests.loottable;

import com.davenonymous.libnonymous.utils.RecipeData;
import com.davenonymous.riddlechests.setup.ModObjects;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.ResourceLocation;

import java.util.HashSet;
import java.util.Set;

public class LootTableMappingInfo extends RecipeData {
    private final ResourceLocation id;

    public ResourceLocation categoryId;
    public Set<ResourceLocation> mappedLootTables;

    public LootTableMappingInfo(ResourceLocation id) {
        this.id = id;
        this.mappedLootTables = new HashSet<>();
    }

    @Override
    public ResourceLocation getId() {
        return this.id;
    }

    @Override
    public IRecipeSerializer<?> getSerializer() {
        return ModObjects.lootTableMappingSerializer;
    }

    @Override
    public IRecipeType<?> getType() {
        return ModObjects.lootTableMappingRecipeType;
    }
}
