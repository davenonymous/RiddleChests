package com.davenonymous.riddlechests.network;

import com.davenonymous.riddlechests.gui.RiddleChestScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketOpenRiddleGUIHandler {

    public void accept(Supplier<NetworkEvent.Context> ctx, BlockPos pos) {
        //Minecraft.getInstance().setScreen(new RiddleChestScreen());
    }
}