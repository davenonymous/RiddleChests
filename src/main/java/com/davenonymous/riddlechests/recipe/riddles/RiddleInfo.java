package com.davenonymous.riddlechests.recipe.riddles;


import com.davenonymous.libnonymous.base.RecipeData;
import com.davenonymous.riddlechests.setup.Registration;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;

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
    public RecipeSerializer<?> getSerializer() {
        return Registration.riddleRecipeSerializer.get();
    }

    @Override
    public RecipeType<?> getType() {
        return Registration.riddleRecipeType;
    }
}