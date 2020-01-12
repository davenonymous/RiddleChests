package com.davenonymous.riddlechests.network;

import com.davenonymous.riddlechests.RiddleChests;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class Networking {
    public static SimpleChannel INSTANCE;
    private static final String CHANNEL_NAME = "channel";
    private static int ID = 0;

    public static int nextID() {
        return ID++;
    }

    public static void registerMessages() {
        INSTANCE = NetworkRegistry.newSimpleChannel(new ResourceLocation(RiddleChests.MODID, CHANNEL_NAME), () -> "1.0", s -> true, s -> true);

        INSTANCE.registerMessage(nextID(), PacketOpenRiddleGUI.class, PacketOpenRiddleGUI::toBytes, PacketOpenRiddleGUI::new, PacketOpenRiddleGUI::handle);
        INSTANCE.registerMessage(nextID(), PacketSolvedRiddle.class, PacketSolvedRiddle::toBytes, PacketSolvedRiddle::new, PacketSolvedRiddle::handle);
    }

    public static void sendSolvedRiddleToServer(BlockPos pos) {
        INSTANCE.sendToServer(new PacketSolvedRiddle(pos));
    }

    public static void sendOpenRiddleChestPacketToClient(ServerPlayerEntity player, BlockPos pos) {
        INSTANCE.sendTo(new PacketOpenRiddleGUI(pos), player.connection.netManager, NetworkDirection.PLAY_TO_CLIENT);
    }
}
