package com.davenonymous.riddlechests.gui;

import com.davenonymous.libnonymous.gui.framework.GUI;
import com.davenonymous.libnonymous.gui.framework.WidgetContainerScreen;
import com.davenonymous.libnonymous.gui.framework.WidgetSlot;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;


public class OpenRiddleChestScreen extends WidgetContainerScreen<OpenRiddleChestContainer> {
    public OpenRiddleChestScreen(OpenRiddleChestContainer container, Inventory inv, Component name) {
        super(container, inv, name);
        this.renderTitle = false;
        this.renderInventoryTitle = false;
    }

    @Override
    protected GUI createGUI() {
        int width = OpenRiddleChestContainer.WIDTH;
        int height = OpenRiddleChestContainer.HEIGHT;

        GUI gui = new GUI(0, 0, width, height);

        // Bind all slots to the matching widgets
        for(Slot slot : this.menu.slots) {
            if(!(slot instanceof WidgetSlot)) {
                continue;
            }

            WidgetSlot widgetSlot = (WidgetSlot)slot;
            widgetSlot.bindToWidget(gui);
        }

        return gui;
    }
}