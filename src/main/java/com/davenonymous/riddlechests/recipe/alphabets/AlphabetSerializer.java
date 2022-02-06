package com.davenonymous.riddlechests.recipe.alphabets;

import com.davenonymous.riddlechests.RiddleChests;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.registries.ForgeRegistryEntry;

import javax.annotation.Nullable;

public class AlphabetSerializer extends ForgeRegistryEntry<RecipeSerializer<?>> implements RecipeSerializer<AlphabetInfo> {
    private static final String[] requiredProperties = new String[] { "validChars" };

    public AlphabetSerializer() {
    }

    @Override
    public AlphabetInfo fromJson(ResourceLocation recipeId, JsonObject json) {
        for(String required : requiredProperties) {
            if(!json.has(required)) {
                throw new JsonParseException(String.format("Invalid alphabet! Missing property: %s", required));
            }
        }

        AlphabetInfo result = new AlphabetInfo(recipeId);
        StringBuilder validChars = new StringBuilder();
        for(JsonElement el : json.getAsJsonArray("validChars")) {
            if(!el.isJsonPrimitive()) {
                continue;
            }
            validChars.append(el.getAsString());
        }
        result.validCharacters = validChars.toString();

        return result;
    }

    @Nullable
    @Override
    public AlphabetInfo fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
        AlphabetInfo result = new AlphabetInfo(recipeId);
        result.validCharacters = buffer.readUtf();
        return result;
    }

    @Override
    public void toNetwork(FriendlyByteBuf buffer, AlphabetInfo recipe) {
        buffer.writeUtf(recipe.validCharacters);
    }
}