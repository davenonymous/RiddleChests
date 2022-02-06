package com.davenonymous.riddlechests.command;


import com.davenonymous.riddlechests.block.RiddleChestTileEntity;
import com.davenonymous.riddlechests.recipe.riddles.RiddleInfo;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

public class CommandCheat implements Command<CommandSourceStack> {
    private static final CommandCheat CMD = new CommandCheat();

    public static ArgumentBuilder<CommandSourceStack, ?> register(CommandDispatcher<CommandSourceStack> dispatcher) {
        return Commands.literal("cheat")
                .requires(cs -> cs.hasPermission(2))
                .executes(CMD);
    }


    @Override
    public int run(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        var world = context.getSource().getLevel();

        /*
        var trace = RaytraceHelper.rayTrace(context.getSource().getWorld(), context.getSource().asPlayer());
        var tracePos = trace.getPos();

        RiddleChestTileEntity tileChest = ModObjects.RIDDLECHEST.getOwnTile(world, tracePos);
        if(tileChest == null) {
            context.getSource().sendFeedback(new TranslationTextComponent("riddle_chest.command.cheat.not_looking_at_chest"), true);
            return 1;
        }

        RiddleInfo riddle = tileChest.getRiddle();
        if(riddle == null) {
            context.getSource().sendFeedback(new StringTextComponent("No riddle linked to this chest. This should not happen. Tell Dave!"), true);
            return 1;
        }

        context.getSource().sendFeedback(new TranslationTextComponent("riddle_chest.command.cheat.solution_is", riddle.solution.toUpperCase()), false);
        */
        return 0;
    }
}