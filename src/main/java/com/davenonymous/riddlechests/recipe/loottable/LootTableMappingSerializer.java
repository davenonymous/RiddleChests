package com.davenonymous.riddlechests.recipe.loottable;

import com.davenonymous.riddlechests.RiddleChests;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistryEntry;

import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.Set;

public class LootTableMappingSerializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<LootTableMappingInfo> {
    private static final String[] requiredProperties = new String[] {"lootTables", "category" };

    public LootTableMappingSerializer() {
        this.setRegistryName(new ResourceLocation(RiddleChests.MODID, "loottable_mapping"));
    }

    @Override
    public LootTableMappingInfo read(ResourceLocation recipeId, JsonObject json) {
        for(String required : requiredProperties) {
            if(!json.has(required)) {
                throw new JsonParseException(String.format("Invalid riddle! Missing property: %s", required));
            }
        }

        LootTableMappingInfo info = new LootTableMappingInfo(recipeId);
        info.categoryId = ResourceLocation.tryCreate(json.get("category").getAsString());
        Set<ResourceLocation> mappedLootTables = new HashSet<>();
        for(JsonElement el : json.getAsJsonArray("lootTables")) {
            mappedLootTables.add(ResourceLocation.tryCreate(el.getAsString()));
        }
        info.mappedLootTables = mappedLootTables;

        return info;
    }

    @Nullable
    @Override
    public LootTableMappingInfo read(ResourceLocation recipeId, PacketBuffer buffer) {
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
    public void write(PacketBuffer buffer, LootTableMappingInfo recipe) {
        buffer.writeResourceLocation(recipe.categoryId);
        buffer.writeInt(recipe.mappedLootTables.size());
        recipe.mappedLootTables.stream().forEach(buffer::writeResourceLocation);
    }
}
