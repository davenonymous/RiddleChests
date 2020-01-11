package com.davenonymous.riddlechests.gui;

import com.davenonymous.libnonymous.gui.framework.GUI;
import com.davenonymous.libnonymous.gui.framework.WidgetScreen;
import com.davenonymous.libnonymous.gui.framework.event.WidgetEventResult;
import com.davenonymous.libnonymous.gui.framework.widgets.WidgetTextBox;
import com.davenonymous.riddlechests.RiddleChests;
import com.davenonymous.riddlechests.block.RiddleChestTileEntity;
import com.davenonymous.riddlechests.network.Networking;
import com.davenonymous.riddlechests.riddles.RiddleInfo;
import com.davenonymous.riddlechests.setup.ModObjects;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;

public class RiddleChestScreen extends WidgetScreen {
    private BlockPos pos;

    public RiddleChestScreen(BlockPos pos) {
        super(new StringTextComponent("Riddle Chest"));
        this.pos = pos;
    }

    private GUI createErrorScreen(String message) {
        GUI gui = new GUI(0, 0, 250, 30);
        WidgetTextBox errorMsg = new WidgetTextBox(message, 0xFFFF0000);
        errorMsg.setDimensions(5, 5, 240, 9);
        gui.add(errorMsg);

        return gui;
    }

    @Override
    protected GUI createGUI() {
        RiddleChestTileEntity chestTile = ModObjects.RIDDLECHEST.getOwnTile(RiddleChests.proxy.getClientWorld(), pos);
        if(chestTile == null) {
            // Show error screen
            return createErrorScreen("Oops. Something went wrong!");
        }

        RiddleInfo riddle = chestTile.getRiddle();
        if(riddle == null) {
            return createErrorScreen("Oops. This chest has no riddle?! Tell Dave about this!");
        }

        int height = (int) (Minecraft.getInstance().mainWindow.getHeight() / Minecraft.getInstance().mainWindow.getGuiScaleFactor());
        int width = (int) (Minecraft.getInstance().mainWindow.getWidth() / Minecraft.getInstance().mainWindow.getGuiScaleFactor());
        RiddleChestGUI gui = new RiddleChestGUI(riddle, width, height);
        gui.addListener(RiddleSolvedEvent.class, (event, widget) -> {
            Networking.sendSolvedRiddleToServer(pos);
            return WidgetEventResult.HANDLED;
        });
        return gui;
    }
}
