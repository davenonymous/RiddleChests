package com.davenonymous.riddlechests.setup;

import com.davenonymous.libnonymous.utils.RecipeHelper;
import com.davenonymous.riddlechests.RiddleChests;
import com.davenonymous.riddlechests.block.RiddleChestBlock;
import com.davenonymous.riddlechests.block.RiddleChestTileEntity;
import com.davenonymous.riddlechests.gui.OpenRiddleChestContainer;
import com.davenonymous.riddlechests.loottable.LootTableMappingRecipeHelper;
import com.davenonymous.riddlechests.loottable.LootTableMappingSerializer;
import com.davenonymous.riddlechests.riddles.RiddleRecipeHelper;
import com.davenonymous.riddlechests.riddles.RiddleSerializer;
import net.minecraft.block.Block;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;

// You can use EventBusSubscriber to automatically subscribe events on the contained class (this is subscribing to the MOD
// Event bus for receiving Registry Events)
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class RegistryEvents {

    @SubscribeEvent
    public static void onBlocksRegistry(final RegistryEvent.Register<Block> event) {
        IForgeRegistry<Block> registry = event.getRegistry();
        registry.register(new RiddleChestBlock().setRegistryName(RiddleChests.MODID, "riddlechest"));
    }

    @SubscribeEvent
    public static void onItemsRegistry(final RegistryEvent.Register<Item> event) {
        Item.Properties properties = new Item.Properties().group(ItemGroup.DECORATIONS);
        IForgeRegistry<Item> registry = event.getRegistry();
        registry.register(new BlockItem(ModObjects.RIDDLECHEST, properties).setRegistryName(RiddleChests.MODID, "riddlechest"));
    }

    @SubscribeEvent
    public static void onTileEntityRegistry(final RegistryEvent.Register<TileEntityType<?>> event) {
        IForgeRegistry<TileEntityType<?>> registry = event.getRegistry();
        registry.register(TileEntityType.Builder.create(RiddleChestTileEntity::new, ModObjects.RIDDLECHEST).build(null).setRegistryName("riddlechest"));
    }

    @SubscribeEvent
    public static void onRecipeRegistry(final RegistryEvent.Register<IRecipeSerializer<?>> event) {
        IForgeRegistry<IRecipeSerializer<?>> registry = event.getRegistry();

        ModObjects.riddleRecipeType = RecipeHelper.registerRecipeType(new ResourceLocation(RiddleChests.MODID, "word"));
        ModObjects.riddleRecipeSerializer = new RiddleSerializer();
        ModObjects.riddleRecipeHelper = new RiddleRecipeHelper();
        registry.register(ModObjects.riddleRecipeSerializer);

        ModObjects.lootTableMappingRecipeType = RecipeHelper.registerRecipeType(new ResourceLocation(RiddleChests.MODID, "loottable_mapping"));
        ModObjects.lootTableMappingSerializer = new LootTableMappingSerializer();
        ModObjects.lootTableMappingRecipeHelper = new LootTableMappingRecipeHelper();
        registry.register(ModObjects.lootTableMappingSerializer);

    }

    @SubscribeEvent
    public static void onContainerRegistry(final RegistryEvent.Register<ContainerType<?>> event) {
        IForgeRegistry<ContainerType<?>> registry = event.getRegistry();

        registry.register(IForgeContainerType.create((windowId, inv, data) -> {
            BlockPos pos = data.readBlockPos();
            return new OpenRiddleChestContainer(windowId, inv, RiddleChests.proxy.getClientWorld(), pos);
        }).setRegistryName(RiddleChests.MODID, "openriddlechest"));
    }
}
