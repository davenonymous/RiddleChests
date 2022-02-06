package com.davenonymous.riddlechests.network;

import com.davenonymous.libnonymous.base.BasePacket;
import com.davenonymous.libnonymous.serialization.Sync;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketOpenRiddleGUI extends BasePacket {
    @Sync
    BlockPos pos;

    public PacketOpenRiddleGUI(BlockPos pos) {
        super();
        this.pos = pos;
    }

    public PacketOpenRiddleGUI(FriendlyByteBuf buf) {
        super(buf);
    }

    @Override
    public void doWork(Supplier<NetworkEvent.Context> ctx) {
        new PacketOpenRiddleGUIHandler().accept(ctx, this.pos);
    }
}