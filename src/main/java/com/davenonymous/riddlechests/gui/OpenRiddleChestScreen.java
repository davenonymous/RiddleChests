package com.davenonymous.riddlechests.gui;

import com.davenonymous.libnonymous.gui.framework.GUI;
import com.davenonymous.libnonymous.gui.framework.WidgetContainerScreen;
import com.davenonymous.libnonymous.gui.framework.WidgetSlot;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.util.text.ITextComponent;

public class OpenRiddleChestScreen extends WidgetContainerScreen<OpenRiddleChestContainer> {
    public OpenRiddleChestScreen(OpenRiddleChestContainer container, PlayerInventory inv, ITextComponent name) {
        super(container, inv, name);
    }

    @Override
    protected GUI createGUI() {
        int width = OpenRiddleChestContainer.WIDTH;
        int height = OpenRiddleChestContainer.HEIGHT;

        GUI gui = new GUI(0, 0, width, height);

        // Bind all slots to the matching widgets
        for(Slot slot : this.container.inventorySlots) {
            if(!(slot instanceof WidgetSlot)) {
                continue;
            }

            WidgetSlot widgetSlot = (WidgetSlot)slot;
            widgetSlot.bindToWidget(gui);
        }

        return gui;
    }
}
