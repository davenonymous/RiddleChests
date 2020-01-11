package com.davenonymous.riddlechests.gui;


import com.davenonymous.libnonymous.gui.framework.event.ValueChangedEvent;
import com.davenonymous.libnonymous.gui.framework.event.WidgetEventResult;
import com.davenonymous.libnonymous.gui.framework.widgets.WidgetPanel;
import com.davenonymous.libnonymous.gui.framework.widgets.WidgetSelectButton;
import com.davenonymous.riddlechests.riddles.RiddleInfo;
import com.davenonymous.riddlechests.setup.Config;
import net.minecraft.util.SoundEvents;

import java.util.*;
import java.util.stream.Collectors;

public class WidgetWordSolution extends WidgetPanel {
    private static final String choices = "AAABCDEEEFGHIIIJKLMNOOOPQRSSTUUUVWXYZ";
    private static final Set<String> choiceSet = new HashSet<>(Arrays.asList(choices.split("")));

    List<WidgetSelectButton<String>> charButtons = new ArrayList<>();

    String value;

    public WidgetWordSolution(RiddleInfo riddle) {
        this.value = riddle.original;
        Random seededRand = new Random(riddle.randomSeed);

        int buttonDim = 20;
        int xOffset = 0;
        for(int pos = 0; pos < riddle.original.length(); pos++) {
            Set<String> validChoices = new HashSet<>();
            validChoices.add(riddle.original.substring(pos, pos+1));
            validChoices.add(riddle.solution.substring(pos, pos+1));
            List<String> remainingChoices = choiceSet.stream().filter(c -> !validChoices.contains(c)).collect(Collectors.toList());
            while(validChoices.size() <= Config.ADDITIONAL_CHARS.get() && remainingChoices.size() > 0) {
                int choice = seededRand.nextInt(remainingChoices.size());
                validChoices.add(remainingChoices.get(choice));
                remainingChoices.remove(choice);
            }

            WidgetSelectButton<String> selectButton = new WidgetSelectButton<>();
            selectButton.setClickSound(SoundEvents.UI_STONECUTTER_TAKE_RESULT);
            selectButton.setWidth(20);
            selectButton.setHeight(20);
            selectButton.addChoice(validChoices);
            selectButton.setX(xOffset);
            selectButton.setValue(riddle.original.substring(pos, pos+1));
            selectButton.addListener(ValueChangedEvent.class, (event, widget) -> {
                String oldValue = value;
                String newValue = calculateValue();
                this.value = newValue;
                this.fireEvent(new ValueChangedEvent<>(oldValue, newValue));
                return WidgetEventResult.CONTINUE_PROCESSING;
            });

            charButtons.add(selectButton);
            this.add(selectButton);

            xOffset += buttonDim + 1;
        }
        this.setWidth(xOffset-1);
        this.setHeight(buttonDim);
    }

    public String getValue() {
        return value;
    }

    public String calculateValue() {
        StringBuilder builder = new StringBuilder();
        charButtons.forEach(button -> builder.append(button.getValue()));
        return builder.toString();
    }
}
