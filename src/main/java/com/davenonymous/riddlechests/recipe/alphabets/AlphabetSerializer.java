package com.davenonymous.riddlechests.recipe.alphabets;

import com.davenonymous.riddlechests.RiddleChests;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistryEntry;

import javax.annotation.Nullable;

public class AlphabetSerializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<AlphabetInfo> {
    private static final String[] requiredProperties = new String[] { "validChars" };

    public AlphabetSerializer() {
        this.setRegistryName(RiddleChests.MODID, "alphabet");
    }

    @Override
    public AlphabetInfo read(ResourceLocation recipeId, JsonObject json) {
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
    public AlphabetInfo read(ResourceLocation recipeId, PacketBuffer buffer) {
        AlphabetInfo result = new AlphabetInfo(recipeId);
        result.validCharacters = buffer.readString();
        return result;
    }

    @Override
    public void write(PacketBuffer buffer, AlphabetInfo recipe) {
        buffer.writeString(recipe.validCharacters);
    }
}
