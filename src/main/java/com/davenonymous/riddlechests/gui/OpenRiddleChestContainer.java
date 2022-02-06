package com.davenonymous.riddlechests.gui;

import com.davenonymous.libnonymous.gui.framework.WidgetContainer;
import com.davenonymous.riddlechests.RiddleChests;
import com.davenonymous.riddlechests.block.RiddleChestTileEntity;

import com.davenonymous.riddlechests.setup.Registration;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.CapabilityItemHandler;

public class OpenRiddleChestContainer extends WidgetContainer {
    public static ResourceLocation SLOTGROUP_CHEST = new ResourceLocation(RiddleChests.MODID, "chest_slots");
    public static int WIDTH = 176;
    public static int HEIGHT = 205;

    public Level world;
    public BlockPos pos;
    public RiddleChestTileEntity chestTile;

    public OpenRiddleChestContainer(int id, BlockPos pos, Inventory playerInventory, Player player) {
        super(Registration.OPEN_RIDDLECHEST_CONTAINER.get(), id, playerInventory);
        this.world = player.level;
        this.pos = pos;
        this.chestTile = (RiddleChestTileEntity)player.getLevel().getBlockEntity(pos);
        HEIGHT = 205;

        this.layoutPlayerInventorySlots(8, HEIGHT - 103);

        if(this.chestTile != null) {
            chestTile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(h -> {
                this.addSlotBox(SLOTGROUP_CHEST, h, 0, 8, -12, 9, 18, 6, 18);
            });
        }
    }
}