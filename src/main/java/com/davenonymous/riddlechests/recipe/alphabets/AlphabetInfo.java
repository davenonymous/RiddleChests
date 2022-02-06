package com.davenonymous.riddlechests.recipe.alphabets;

import com.davenonymous.libnonymous.base.RecipeData;
import com.davenonymous.riddlechests.setup.Registration;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;

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
    public RecipeSerializer<?> getSerializer() {
        return Registration.alphabetSerializer.get();
    }

    @Override
    public RecipeType<?> getType() {
        return Registration.alphabetRecipeType;
    }
}