package com.davenonymous.riddlechests.setup;

import com.davenonymous.riddlechests.RiddleChests;
import com.davenonymous.riddlechests.gui.OpenRiddleChestScreen;
import com.davenonymous.riddlechests.gui.RiddleChestScreen;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = RiddleChests.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientSetup {
	public static void init(FMLClientSetupEvent event) {
		event.enqueueWork(() -> {
            /*
            MenuScreens.register(Registration.POWERGEN_CONTAINER.get(), PowergenScreen::new);
            ItemBlockRenderTypes.setRenderLayer(Registration.POWERGEN.get(), RenderType.translucent());
            PowergenRenderer.register();
             */
			MenuScreens.register(Registration.OPEN_RIDDLECHEST_CONTAINER.get(), OpenRiddleChestScreen::new);
			MenuScreens.register(Registration.RIDDLECHEST_CONTAINER.get(), RiddleChestScreen::new);
			//BlockEntityRenderers.register(Registration.BONSAI_POT_BLOCKENTITY.get(), BonsaiPotRenderer::new);
		});
	}

	/*
	@SubscribeEvent
	public static void onModelRegistryEvent(ModelRegistryEvent event) {
		ModelLoaderRegistry.registerLoader(BonsaiPotModelLoader.BONSAIPOT_LOADER, new BonsaiPotModelLoader());
	}

	 */
}