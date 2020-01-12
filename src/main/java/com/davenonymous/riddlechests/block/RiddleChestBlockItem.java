package com.davenonymous.riddlechests.block;

import net.minecraft.block.Block;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class RiddleChestBlockItem extends BlockItem {
    public RiddleChestBlockItem(Block blockIn, Properties builder) {
        super(blockIn, builder);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);

        if(stack.hasTag() && stack.getTag().getBoolean("solved")) {
            tooltip.add(new TranslationTextComponent("riddle_chest.solved").applyTextStyle(TextFormatting.DARK_GREEN));
        } else {
            tooltip.add(new TranslationTextComponent("riddle_chest.unsolved").applyTextStyle(TextFormatting.DARK_RED));
        }
    }
}
