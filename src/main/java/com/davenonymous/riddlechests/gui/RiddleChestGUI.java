package com.davenonymous.riddlechests.gui;

import com.davenonymous.libnonymous.gui.framework.GUI;
import com.davenonymous.libnonymous.gui.framework.GUIHelper;
import com.davenonymous.libnonymous.gui.framework.event.KeyReleasedEvent;
import com.davenonymous.libnonymous.gui.framework.event.ValueChangedEvent;
import com.davenonymous.libnonymous.gui.framework.event.WidgetEventResult;
import com.davenonymous.libnonymous.gui.framework.widgets.WidgetPanel;
import com.davenonymous.libnonymous.gui.framework.widgets.WidgetTextBox;
import com.davenonymous.riddlechests.RiddleChests;
import com.davenonymous.riddlechests.riddles.RiddleInfo;
import com.davenonymous.riddlechests.util.Logz;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.ResourceLocation;

public class RiddleChestGUI extends GUI {
    private static ResourceLocation backgroundTexture = new ResourceLocation(RiddleChests.MODID, "textures/gui/krondor.png");

    private final RiddleInfo riddle;
    public RiddleChestGUI(RiddleInfo riddle, int width, int height) {
        super(0, 0, width, height);

        this.riddle = riddle;

        this.add(createPanel());

        this.addListener(KeyReleasedEvent.class, (event, widget) -> {
            if(event.keyCode == 69) {
                Minecraft.getInstance().currentScreen.onClose();
            }
            return WidgetEventResult.CONTINUE_PROCESSING;
        });
    }

    private WidgetPanel createPanel() {
        WidgetPanel panel = new WidgetPanel();
        panel.setX(0);
        panel.setY(0);
        panel.setWidth(this.width);
        panel.setHeight(this.height);

        WidgetWordSolution wordInput = new WidgetWordSolution(riddle);
        wordInput.setX((panel.width - wordInput.width) / 2);
        wordInput.addListener(ValueChangedEvent.class, (event, widget) -> {
            String newValue = (String) event.newValue;
            if(riddle.solution.equalsIgnoreCase(newValue)) {
                this.fireEvent(new RiddleSolvedEvent());
            }

            return WidgetEventResult.CONTINUE_PROCESSING;
        });

        panel.add(wordInput);

        WidgetPanel linesPanel = new WidgetPanel();
        linesPanel.setSize(512, 512);

        int yOffset = 0;
        for(String line : riddle.lines) {
            WidgetTextBox textBox = new WidgetTextBox(line, 0xAAAAAA);

            int width = Minecraft.getInstance().fontRenderer.getStringWidth(line);
            int xOffset = (linesPanel.width - width) / 2;

            textBox.setWidth(width);
            textBox.setY(yOffset);
            textBox.setX(xOffset);

            linesPanel.add(textBox);
            yOffset += textBox.height+1;
        }

        linesPanel.adjustSizeToContent();
        linesPanel.setY((panel.height - linesPanel.height) / 2);
        linesPanel.setX((panel.width - linesPanel.width) / 2);

        wordInput.setY(linesPanel.y);
        linesPanel.setY(linesPanel.y + 28);

        this.add(linesPanel);

        return panel;
    }

    @Override
    protected void drawWindow(Screen screen) {
        super.drawWindow(screen);

        GlStateManager.disableLighting();

        GlStateManager.color4f(1f, 1f, 1f, 1f);
        screen.getMinecraft().getTextureManager().bindTexture(backgroundTexture);

        GUIHelper.drawStretchedTexture(0, 0, this.width, this.height, 0, 0, 256, 256);
    }
}
