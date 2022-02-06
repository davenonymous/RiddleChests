package com.davenonymous.riddlechests.network;


import com.davenonymous.libnonymous.base.BaseLanguageProvider;
import com.davenonymous.libnonymous.base.BasePacket;
import com.davenonymous.libnonymous.serialization.Sync;
import com.davenonymous.riddlechests.block.RiddleChestTileEntity;
import com.davenonymous.riddlechests.gui.OpenRiddleChestContainer;
import com.davenonymous.riddlechests.setup.Registration;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkHooks;


import javax.annotation.Nullable;
import java.util.function.Supplier;

public class PacketSolvedRiddle extends BasePacket {
    @Sync
    BlockPos pos;

    public PacketSolvedRiddle(BlockPos pos) {
        super();
        this.pos = pos;
    }

    public PacketSolvedRiddle(FriendlyByteBuf buf) {
        super(buf);
    }

    @Override
    public void doWork(Supplier<NetworkEvent.Context> ctx) {
        Level world = ctx.get().getSender().getLevel();
        if(world.getBlockEntity(pos) instanceof RiddleChestTileEntity chestTile) {
            chestTile.solved = true;
            chestTile.generateLoot(ctx.get().getSender());
            chestTile.setChanged();

            world.playSound(null, pos.getX(), pos.getY(), pos.getZ(), SoundEvents.CHEST_OPEN, SoundSource.BLOCKS, 0.5F, world.random.nextFloat() * 0.1F + 0.9F);

            MenuProvider menuProvider = new MenuProvider() {
                @Override
                public Component getDisplayName() {
                    return new TranslatableComponent(BaseLanguageProvider.getContainerLanguageKey(Registration.OPEN_RIDDLECHEST_CONTAINER.get()));
                }

                @Nullable
                @Override
                public AbstractContainerMenu createMenu(int pContainerId, Inventory pInventory, Player pPlayer) {
                    return new OpenRiddleChestContainer(pContainerId, pos, pInventory, pPlayer);
                }
            };

            NetworkHooks.openGui(ctx.get().getSender(), menuProvider, pos);

        }
    }
}