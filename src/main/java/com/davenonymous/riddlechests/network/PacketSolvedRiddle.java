package com.davenonymous.riddlechests.network;

import com.davenonymous.libnonymous.base.BaseNamedContainerProvider;
import com.davenonymous.libnonymous.base.BasePacket;
import com.davenonymous.libnonymous.serialization.Sync;
import com.davenonymous.riddlechests.block.RiddleChestTileEntity;
import com.davenonymous.riddlechests.gui.OpenRiddleChestContainer;
import com.davenonymous.riddlechests.setup.ModObjects;
import com.davenonymous.riddlechests.util.Logz;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.NetworkHooks;

import java.util.function.Supplier;

public class PacketSolvedRiddle extends BasePacket {
    @Sync
    BlockPos pos;

    public PacketSolvedRiddle(BlockPos pos) {
        super();
        this.pos = pos;
    }

    public PacketSolvedRiddle(PacketBuffer buf) {
        super(buf);
    }

    @Override
    public void doWork(Supplier<NetworkEvent.Context> ctx) {
        World world = ctx.get().getSender().getServerWorld();
        RiddleChestTileEntity chestTile = ModObjects.RIDDLECHEST.getOwnTile(world, pos);
        chestTile.solved = true;
        chestTile.generateLoot(ctx.get().getSender());
        chestTile.markDirty();

        world.playSound(null, pos.getX(), pos.getY(), pos.getZ(), SoundEvents.BLOCK_CHEST_OPEN, SoundCategory.BLOCKS, 0.5F, world.rand.nextFloat() * 0.1F + 0.9F);
        NetworkHooks.openGui(ctx.get().getSender(), new BaseNamedContainerProvider(
            new StringTextComponent("Riddle Chest"),
            (id, inv, playerEntity) -> new OpenRiddleChestContainer(id, inv, world, pos)
        ), pos);
    }
}
