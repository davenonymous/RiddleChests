package com.davenonymous.riddlechests.setup;

import com.davenonymous.libnonymous.registration.RegistrationHelper;
import com.davenonymous.riddlechests.block.RiddleChestBlock;
import com.davenonymous.riddlechests.block.RiddleChestTileEntity;
import com.davenonymous.riddlechests.gui.OpenRiddleChestContainer;
import com.davenonymous.riddlechests.gui.RiddleChestContainer;
import com.davenonymous.riddlechests.recipe.alphabets.AlphabetInfo;
import com.davenonymous.riddlechests.recipe.alphabets.AlphabetRecipeHelper;
import com.davenonymous.riddlechests.recipe.alphabets.AlphabetSerializer;
import com.davenonymous.riddlechests.recipe.riddles.RiddleInfo;
import com.davenonymous.riddlechests.recipe.riddles.RiddleRecipeHelper;
import com.davenonymous.riddlechests.recipe.riddles.RiddleSerializer;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import static com.davenonymous.riddlechests.RiddleChests.MODID;

public class Registration {
	private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MODID);
	private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);
	private static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, MODID);
	private static final DeferredRegister<MenuType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.CONTAINERS, MODID);
	private static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, MODID);


	public static void init() {
		IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
		BLOCKS.register(bus);
		ITEMS.register(bus);
		BLOCK_ENTITIES.register(bus);
		CONTAINERS.register(bus);
		RECIPE_SERIALIZERS.register(bus);
	}

	public static RecipeType<RiddleInfo> riddleRecipeType;
	public static RiddleRecipeHelper riddleRecipeHelper;
	public static final RegistryObject<RecipeSerializer<?>> riddleRecipeSerializer  = RECIPE_SERIALIZERS.register("word", () -> new RiddleSerializer());

	public static RecipeType<AlphabetInfo> alphabetRecipeType;
	public static AlphabetRecipeHelper alphabetRecipeHelper;
	public static final RegistryObject<RecipeSerializer<?>> alphabetSerializer  = RECIPE_SERIALIZERS.register("alphabet", () -> new AlphabetSerializer());

	public static final Item.Properties ITEM_PROPERTIES = new Item.Properties().tab(CreativeModeTab.TAB_DECORATIONS);

	public static final RegistryObject<Block> RIDDLECHEST = BLOCKS.register("riddlechest", () -> new RiddleChestBlock());
	public static final RegistryObject<Item> RIDDLECHEST_ITEM = RegistrationHelper.fromBlock(RIDDLECHEST, ITEMS, ITEM_PROPERTIES);

	public static final RegistryObject<BlockEntityType<RiddleChestTileEntity>> RIDDLECHEST_BLOCKENTITY = BLOCK_ENTITIES.register("riddlechest", () -> BlockEntityType.Builder.of(RiddleChestTileEntity::new, RIDDLECHEST.get())
			.build(null));

	public static final RegistryObject<MenuType<OpenRiddleChestContainer>> OPEN_RIDDLECHEST_CONTAINER = CONTAINERS.register("openriddlechest", () -> IForgeMenuType.create((windowId, inv, data) -> new OpenRiddleChestContainer(windowId, data.readBlockPos(), inv, inv.player)));
	public static final RegistryObject<MenuType<RiddleChestContainer>> RIDDLECHEST_CONTAINER = CONTAINERS.register("riddlechest", () -> IForgeMenuType.create((windowId, inv, data) -> new RiddleChestContainer(windowId, data.readBlockPos(), inv, inv.player)));


}