package com.davenonymous.riddlechests.riddles;

import com.davenonymous.riddlechests.RiddleChests;
import com.davenonymous.riddlechests.util.Logz;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistryEntry;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class RiddleSerializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<RiddleInfo> {
    private static final String[] requiredProperties = new String[] {"lang", "category", "original", "solution", "riddle"};

    public RiddleSerializer() {
        this.setRegistryName(new ResourceLocation(RiddleChests.MODID, "word"));
    }

    @Override
    public RiddleInfo read(ResourceLocation recipeId, JsonObject json) {
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
    public RiddleInfo read(ResourceLocation recipeId, PacketBuffer buffer) {
        RiddleInfo info = new RiddleInfo(recipeId);
        info.category = buffer.readResourceLocation();
        info.lang = buffer.readString();
        info.original = buffer.readString();
        info.solution = buffer.readString();
        info.randomSeed = stringToSeed(info.original + info.solution);
        info.lines = new ArrayList<>();
        int lineCount = buffer.readInt();
        for(int i = 0; i < lineCount; i++) {
            info.lines.add(buffer.readString());
        }
        return info;
    }

    @Override
    public void write(PacketBuffer buffer, RiddleInfo recipe) {
        buffer.writeResourceLocation(recipe.category);
        buffer.writeString(recipe.lang);
        buffer.writeString(recipe.original);
        buffer.writeString(recipe.solution);
        buffer.writeInt(recipe.lines.size());
        recipe.lines.forEach(s -> buffer.writeString(s));
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
