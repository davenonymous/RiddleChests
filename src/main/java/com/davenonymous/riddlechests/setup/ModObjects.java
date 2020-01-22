package com.davenonymous.riddlechests.setup;

import com.davenonymous.riddlechests.RiddleChests;
import com.davenonymous.riddlechests.block.RiddleChestBlock;
import com.davenonymous.riddlechests.block.RiddleChestTileEntity;
import com.davenonymous.riddlechests.gui.OpenRiddleChestContainer;
import com.davenonymous.riddlechests.recipe.alphabets.AlphabetInfo;
import com.davenonymous.riddlechests.recipe.alphabets.AlphabetRecipeHelper;
import com.davenonymous.riddlechests.recipe.loottable.LootTableMappingInfo;
import com.davenonymous.riddlechests.recipe.loottable.LootTableMappingRecipeHelper;
import com.davenonymous.riddlechests.recipe.riddles.RiddleInfo;
import com.davenonymous.riddlechests.recipe.riddles.RiddleRecipeHelper;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.registries.ObjectHolder;

@ObjectHolder(RiddleChests.MODID)
public class ModObjects {
    @ObjectHolder("riddlechest")
    public static RiddleChestBlock RIDDLECHEST;

    @ObjectHolder("riddlechest")
    public static TileEntityType<RiddleChestTileEntity> RIDDLECHEST_TILE;

    @ObjectHolder("openriddlechest")
    public static ContainerType<OpenRiddleChestContainer> OPEN_RIDDLECHEST_CONTAINER;

    // RiddleInfo recipe
    public static IRecipeType<RiddleInfo> riddleRecipeType;
    public static IRecipeSerializer<RiddleInfo> riddleRecipeSerializer;
    public static RiddleRecipeHelper riddleRecipeHelper;

    // LootTableMapping recipe
    public static IRecipeType<LootTableMappingInfo> lootTableMappingRecipeType;
    public static IRecipeSerializer<LootTableMappingInfo> lootTableMappingSerializer;
    public static LootTableMappingRecipeHelper lootTableMappingRecipeHelper;

    // Alphabet recipe
    public static IRecipeType<AlphabetInfo> alphabetRecipeType;
    public static IRecipeSerializer<AlphabetInfo> alphabetSerializer;
    public static AlphabetRecipeHelper alphabetRecipeHelper;
}
