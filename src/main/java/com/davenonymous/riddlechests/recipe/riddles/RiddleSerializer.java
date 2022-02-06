package com.davenonymous.riddlechests.recipe.riddles;

import com.davenonymous.riddlechests.RiddleChests;

import com.davenonymous.riddlechests.config.CommonConfig;
import com.davenonymous.riddlechests.util.Logz;
import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.registries.ForgeRegistryEntry;


import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class RiddleSerializer extends ForgeRegistryEntry<RecipeSerializer<?>> implements RecipeSerializer<RiddleInfo> {
    private static final ResourceLocation defaultAlphabet = new ResourceLocation(RiddleChests.MODID, "alphabets/en_us");
    private static final String[] requiredProperties = new String[] {"lang", "category", "original", "solution", "riddle"};

    public RiddleSerializer() {
    }

    @Override
    public RiddleInfo fromJson(ResourceLocation recipeId, JsonObject json) {
        for(String required : requiredProperties) {
            if(!json.has(required)) {
                Logz.info(String.format("Invalid riddle! Missing property: %s", required));
                return null;
            }
        }

        RiddleInfo info = new RiddleInfo(recipeId);
        info.lang = "en_us";
        if(json.has("lang")) {
            info.lang = json.get("lang").getAsString();
        }

        info.alphabet = defaultAlphabet;
        if(json.has("alphabet")) {
            info.alphabet = new ResourceLocation(json.get("alphabet").getAsString());
        }

        String category = json.get("category").getAsString();
        if(CommonConfig.DISABLE_RIDDLE_CATEGORIES.get().contains(category)) {
            Logz.debug("Skipping riddle '{}'! Category '{}' is disabled");
            return null;
        }

        info.category = new ResourceLocation(json.get("category").getAsString());
        info.original = json.get("original").getAsString();
        info.solution = json.get("solution").getAsString();

        if(info.original.length() != info.solution.length()) {
            Logz.info(String.format("Invalid riddle! Length of original and solution does not match!"));
            return null;
        }

        List<String> riddleLines = new ArrayList<>();
        json.getAsJsonArray("riddle").forEach(l -> riddleLines.add(l.getAsString()));
        info.lines = riddleLines;
        info.randomSeed = stringToSeed(info.original + info.solution);

        return info;
    }

    @Nullable
    @Override
    public RiddleInfo fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
        RiddleInfo info = new RiddleInfo(recipeId);
        info.alphabet = buffer.readResourceLocation();
        info.category = buffer.readResourceLocation();
        info.lang = buffer.readUtf();
        info.original = buffer.readUtf();
        info.solution = buffer.readUtf();
        info.randomSeed = stringToSeed(info.original + info.solution);
        info.lines = new ArrayList<>();
        int lineCount = buffer.readInt();
        for(int i = 0; i < lineCount; i++) {
            info.lines.add(buffer.readUtf());
        }
        return info;
    }



    @Override
    public void toNetwork(FriendlyByteBuf buffer, RiddleInfo recipe) {
        buffer.writeResourceLocation(recipe.alphabet);
        buffer.writeResourceLocation(recipe.category);
        buffer.writeUtf(recipe.lang);
        buffer.writeUtf(recipe.original);
        buffer.writeUtf(recipe.solution);
        buffer.writeInt(recipe.lines.size());
        recipe.lines.forEach(buffer::writeUtf);
    }

    private static long stringToSeed(String s) {
        if (s == null) {
            return 0;
        }
        long hash = 0;
        for (char c : s.toCharArray()) {
            hash = 31L*hash + c;
        }
        return hash;
    }
}