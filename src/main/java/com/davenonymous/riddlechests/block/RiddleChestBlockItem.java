package com.davenonymous.riddlechests.block;


import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

import java.util.List;

public class RiddleChestBlockItem extends BlockItem {
    public RiddleChestBlockItem(Block blockIn, Properties builder) {
        super(blockIn, builder);
    }

    @Override
    public void appendHoverText(ItemStack pStack, @org.jetbrains.annotations.Nullable Level pLevel, List<Component> pTooltip, TooltipFlag pFlag) {
        super.appendHoverText(pStack, pLevel, pTooltip, pFlag);

        if(pStack.hasTag() && pStack.getTag().getBoolean("solved")) {
            pTooltip.add(new TranslatableComponent("riddle_chest.solved").withStyle(ChatFormatting.DARK_GREEN));
        } else {
            pTooltip.add(new TranslatableComponent("riddle_chest.unsolved").withStyle(ChatFormatting.DARK_RED));
        }
    }

}