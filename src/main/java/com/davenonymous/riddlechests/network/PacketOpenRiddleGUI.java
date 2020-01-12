package com.davenonymous.riddlechests.network;

import com.davenonymous.libnonymous.base.BasePacket;
import com.davenonymous.libnonymous.serialization.Sync;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketOpenRiddleGUI extends BasePacket {
    @Sync
    BlockPos pos;

    public PacketOpenRiddleGUI(BlockPos pos) {
        super();
        this.pos = pos;
    }

    public PacketOpenRiddleGUI(PacketBuffer buf) {
        super(buf);
    }

    @Override
    public void doWork(Supplier<NetworkEvent.Context> ctx) {
        new PacketOpenRiddleGUIHandler().accept(ctx, this.pos);
    }
}
