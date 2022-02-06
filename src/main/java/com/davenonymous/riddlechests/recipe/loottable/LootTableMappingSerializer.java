package com.davenonymous.riddlechests.recipe.loottable;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.registries.ForgeRegistryEntry;


import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.Set;

public class LootTableMappingSerializer extends ForgeRegistryEntry<RecipeSerializer<?>> implements RecipeSerializer<LootTableMappingInfo> {
    private static final String[] requiredProperties = new String[] {"lootTables", "category" };

    public LootTableMappingSerializer() {
    }

    @Override
    public LootTableMappingInfo fromJson(ResourceLocation recipeId, JsonObject json) {
        for(String required : requiredProperties) {
            if(!json.has(required)) {
                throw new JsonParseException(String.format("Invalid riddle! Missing property: %s", required));
            }
        }

        LootTableMappingInfo info = new LootTableMappingInfo(recipeId);
        info.categoryId = ResourceLocation.tryParse(json.get("category").getAsString());
        Set<ResourceLocation> mappedLootTables = new HashSet<>();
        for(JsonElement el : json.getAsJsonArray("lootTables")) {
            mappedLootTables.add(ResourceLocation.tryParse(el.getAsString()));
        }
        info.mappedLootTables = mappedLootTables;

        return info;
    }

    @Nullable
    @Override
    public LootTableMappingInfo fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
        LootTableMappingInfo info = new LootTableMappingInfo(recipeId);
        info.categoryId = buffer.readResourceLocation();
        int tableCount = buffer.readInt();
        Set<ResourceLocation> mappedLootTables = new HashSet<>();
        for (int i = 0; i < tableCount; i++) {
            mappedLootTables.add(buffer.readResourceLocation());
        }
        return info;
    }

    @Override
    public void toNetwork(FriendlyByteBuf buffer, LootTableMappingInfo recipe) {
        buffer.writeResourceLocation(recipe.categoryId);
        buffer.writeInt(recipe.mappedLootTables.size());
        recipe.mappedLootTables.stream().forEach(buffer::writeResourceLocation);
    }
}