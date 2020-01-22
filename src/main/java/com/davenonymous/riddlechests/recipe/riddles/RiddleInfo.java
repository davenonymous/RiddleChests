package com.davenonymous.riddlechests.recipe.riddles;

import com.davenonymous.libnonymous.utils.RecipeData;
import com.davenonymous.riddlechests.setup.ModObjects;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

public class RiddleInfo extends RecipeData {
    private final ResourceLocation id;

    public ResourceLocation alphabet;
    public ResourceLocation category;
    public String lang;
    public String original;
    public String solution;
    public List<String> lines;
    public long randomSeed;

    public RiddleInfo(ResourceLocation id) {
        this.id = id;
        this.lines = new ArrayList<>();
    }

    @Override
    public ResourceLocation getId() {
        return this.id;
    }

    @Override
    public IRecipeSerializer<?> getSerializer() {
        return ModObjects.riddleRecipeSerializer;
    }

    @Override
    public IRecipeType<?> getType() {
        return ModObjects.riddleRecipeType;
    }
}
