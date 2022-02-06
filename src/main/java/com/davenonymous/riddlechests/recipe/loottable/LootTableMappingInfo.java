package com.davenonymous.riddlechests.recipe.loottable;


import com.davenonymous.libnonymous.base.RecipeData;
import com.davenonymous.riddlechests.setup.Registration;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;

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
    public RecipeSerializer<?> getSerializer() {
        return Registration.lootMappingSerializer.get();
    }

    @Override
    public RecipeType<?> getType() {
        return Registration.lootMappingRecipeType;
    }
}