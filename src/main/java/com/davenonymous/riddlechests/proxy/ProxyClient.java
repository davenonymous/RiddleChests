package com.davenonymous.riddlechests.proxy;


import com.davenonymous.libnonymous.gui.config.WidgetGuiConfig;
import com.davenonymous.libnonymous.setup.IProxy;
import com.davenonymous.riddlechests.RiddleChests;
import com.davenonymous.riddlechests.gui.OpenRiddleChestScreen;
import com.davenonymous.riddlechests.setup.Config;
import com.davenonymous.riddlechests.setup.ModObjects;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import net.minecraftforge.fml.ExtensionPoint;
import net.minecraftforge.fml.ModList;

public class ProxyClient implements IProxy {
    @Override
    public void init() {
        ScreenManager.registerFactory(ModObjects.OPEN_RIDDLECHEST_CONTAINER, OpenRiddleChestScreen::new);

        ModList.get().getModContainerById(RiddleChests.MODID).ifPresent(c -> c.registerExtensionPoint(ExtensionPoint.CONFIGGUIFACTORY, () -> (mc, parent) -> {
            return new WidgetGuiConfig(parent, Config.COMMON_CONFIG, Config.CLIENT_CONFIG);
        }));
    }

    @Override
    public World getClientWorld() {
        return Minecraft.getInstance().world;
    }

    @Override
    public PlayerEntity getClientPlayer() {
        return Minecraft.getInstance().player;
    }
}
