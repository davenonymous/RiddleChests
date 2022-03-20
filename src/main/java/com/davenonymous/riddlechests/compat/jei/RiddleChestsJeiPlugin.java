package com.davenonymous.riddlechests.compat.jei;

import com.davenonymous.riddlechests.RiddleChests;
import com.davenonymous.riddlechests.gui.OpenRiddleChestScreen;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import net.minecraft.resources.ResourceLocation;

@JeiPlugin
public class RiddleChestsJeiPlugin implements IModPlugin {
	public static final ResourceLocation PLUGIN_ID = new ResourceLocation(RiddleChests.MODID, "default");

	@Override
	public ResourceLocation getPluginUid() {
		return PLUGIN_ID;
	}

	@Override
	public void registerGuiHandlers(IGuiHandlerRegistration registration) {
		registration.addGuiScreenHandler(OpenRiddleChestScreen.class, guiScreen -> null);
	}
}