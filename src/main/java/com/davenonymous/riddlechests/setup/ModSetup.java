package com.davenonymous.riddlechests.setup;

import com.davenonymous.riddlechests.network.Networking;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

public class ModSetup {
	public static void init(FMLCommonSetupEvent event) {
		Networking.registerMessages();
	}
}