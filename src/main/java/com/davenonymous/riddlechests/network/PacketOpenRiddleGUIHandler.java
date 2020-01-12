package com.davenonymous.riddlechests.network;

import com.davenonymous.riddlechests.gui.RiddleChestScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketOpenRiddleGUIHandler {

    public void accept(Supplier<NetworkEvent.Context> ctx, BlockPos pos) {
        Minecraft.getInstance().displayGuiScreen(new RiddleChestScreen(pos));
    }
}
