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
import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;

public class CommandCheat implements Command<CommandSourceStack> {
    private static final CommandCheat CMD = new CommandCheat();

    public static ArgumentBuilder<CommandSourceStack, ?> register(CommandDispatcher<CommandSourceStack> dispatcher) {
        return Commands.literal("cheat")
                .then(Commands.argument("pos", BlockPosArgument.blockPos()).requires(cs -> cs.hasPermission(0))
                .executes(CMD));
    }

    @Override
    public int run(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        var world = context.getSource().getLevel();
        BlockPos pos = BlockPosArgument.getLoadedBlockPos(context, "pos");

        if(!(world.getBlockEntity(pos) instanceof RiddleChestTileEntity riddleChest)) {
            context.getSource().sendFailure(new TranslatableComponent("riddle_chest.command.cheat.not_a_chest"));
            return 0;
        }

        RiddleInfo riddle = riddleChest.getRiddle();
        if(riddle == null) {
            context.getSource().sendFailure(new TextComponent("No riddle linked to this chest. This should not happen. Tell Dave!"));
            return 1;
        }

        context.getSource().sendSuccess(new TranslatableComponent("riddle_chest.command.cheat.solution_is", riddle.solution.toUpperCase()), false);
        return 0;
    }
}