package com.davenonymous.riddlechests.gui;

import com.davenonymous.libnonymous.gui.framework.WidgetContainer;
import com.davenonymous.riddlechests.RiddleChests;
import com.davenonymous.riddlechests.block.RiddleChestTileEntity;
import com.davenonymous.riddlechests.setup.ModObjects;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;

public class OpenRiddleChestContainer extends WidgetContainer {
    public static ResourceLocation SLOTGROUP_CHEST = new ResourceLocation(RiddleChests.MODID, "chest_slots");
    public static int WIDTH = 190;
    public static int HEIGHT = 183;

    public World world;
    public BlockPos pos;
    public RiddleChestTileEntity chestTile;

    public OpenRiddleChestContainer(int id, PlayerInventory inv, World world, BlockPos pos) {
        super(ModObjects.OPEN_RIDDLECHEST_CONTAINER, id, inv);
        this.world = world;
        this.pos = pos;
        this.chestTile = ModObjects.RIDDLECHEST.getOwnTile(world, pos);
        HEIGHT = 205;
        WIDTH = 176;

        this.layoutPlayerInventorySlots(8, HEIGHT - 103);

        if(this.chestTile != null) {
            chestTile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(h -> {
                this.addSlotBox(SLOTGROUP_CHEST, h, 0, 8, -12, 9, 18, 6, 18);
            });
        }
    }
}
