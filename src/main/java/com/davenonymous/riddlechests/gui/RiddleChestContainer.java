package com.davenonymous.riddlechests.gui;

import com.davenonymous.libnonymous.gui.framework.WidgetContainer;
import com.davenonymous.riddlechests.block.RiddleChestTileEntity;
import com.davenonymous.riddlechests.setup.Registration;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class RiddleChestContainer extends WidgetContainer {

	public Level world;
	public BlockPos pos;
	public RiddleChestTileEntity chestTile;

	public RiddleChestContainer(int id, BlockPos pos, Inventory playerInventory, Player player) {
		super(Registration.RIDDLECHEST_CONTAINER.get(), id, playerInventory);
		this.world = player.level;
		this.pos = pos;
		this.chestTile = (RiddleChestTileEntity)player.getLevel().getBlockEntity(pos);
	}
}