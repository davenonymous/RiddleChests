package com.davenonymous.riddlechests.gui;

import com.davenonymous.libnonymous.gui.framework.GUI;
import com.davenonymous.libnonymous.gui.framework.WidgetContainerScreen;
import com.davenonymous.libnonymous.gui.framework.WidgetScreen;
import com.davenonymous.libnonymous.gui.framework.event.WidgetEventResult;
import com.davenonymous.libnonymous.gui.framework.widgets.WidgetTextBox;
import com.davenonymous.riddlechests.block.RiddleChestTileEntity;
import com.davenonymous.riddlechests.network.Networking;
import com.davenonymous.riddlechests.recipe.alphabets.AlphabetInfo;
import com.davenonymous.riddlechests.recipe.riddles.RiddleInfo;
import com.davenonymous.riddlechests.setup.Registration;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.entity.player.Inventory;


public class RiddleChestScreen extends WidgetContainerScreen<RiddleChestContainer> {

    public RiddleChestScreen(RiddleChestContainer container, Inventory inv, Component name) {
        super(container, inv, name);
        this.renderTitle = false;
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
        var level = Minecraft.getInstance().level;
        var someTile = level.getBlockEntity(menu.pos);
        if(!(someTile instanceof RiddleChestTileEntity)) {
            return createErrorScreen("Oops. Something went wrong!");
        }

        RiddleChestTileEntity chestTile = (RiddleChestTileEntity)someTile;

        RiddleInfo riddle = chestTile.getRiddle();
        if(riddle == null) {
            return createErrorScreen("Oops. This chest has no riddle?! Tell Dave about this!");
        }

        // Make sure the riddle knows what alphabet to use
        AlphabetInfo alphabetInfo = Registration.alphabetRecipeHelper.getRecipe(level.getRecipeManager(), riddle.alphabet);
        if(alphabetInfo == null) {
            return createErrorScreen("Oops. Unknown alphabet for this riddle!");
        }

        int height = (int) (Minecraft.getInstance().getWindow().getHeight() / Minecraft.getInstance().getWindow().getGuiScale());
        int width = (int) (Minecraft.getInstance().getWindow().getWidth() / Minecraft.getInstance().getWindow().getGuiScale());
        RiddleChestGUI gui = new RiddleChestGUI(riddle, alphabetInfo, width, height);
        gui.addListener(RiddleSolvedEvent.class, (event, widget) -> {
            Networking.sendSolvedRiddleToServer(menu.pos);
            return WidgetEventResult.HANDLED;
        });
        return gui;
    }
}