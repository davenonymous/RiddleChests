package com.davenonymous.riddlechests.recipe.alphabets;

import com.davenonymous.libnonymous.utils.RecipeData;
import com.davenonymous.riddlechests.setup.ModObjects;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.ResourceLocation;

public class AlphabetInfo extends RecipeData {
    private final ResourceLocation id;

    public String validCharacters;

    public AlphabetInfo(ResourceLocation id) {
        this.id = id;
    }

    @Override
    public ResourceLocation getId() {
        return this.id;
    }

    @Override
    public IRecipeSerializer<?> getSerializer() {
        return ModObjects.alphabetSerializer;
    }

    @Override
    public IRecipeType<?> getType() {
        return ModObjects.alphabetRecipeType;
    }
}
