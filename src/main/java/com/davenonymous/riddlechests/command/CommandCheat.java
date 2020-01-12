package com.davenonymous.riddlechests.command;

import com.davenonymous.libnonymous.utils.RaytraceHelper;
import com.davenonymous.riddlechests.block.RiddleChestTileEntity;
import com.davenonymous.riddlechests.riddles.RiddleInfo;
import com.davenonymous.riddlechests.setup.ModObjects;
import com.davenonymous.riddlechests.util.Logz;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.server.ServerWorld;

public class CommandCheat implements Command<CommandSource> {
    private static final CommandCheat CMD = new CommandCheat();

    public static ArgumentBuilder<CommandSource, ?> register(CommandDispatcher<CommandSource> dispatcher) {
        return Commands.literal("cheat")
                .requires(cs -> cs.hasPermissionLevel(2))
                .executes(CMD);
    }


    @Override
    public int run(CommandContext<CommandSource> context) throws CommandSyntaxException {
        ServerWorld world = context.getSource().getWorld();

        BlockRayTraceResult trace = RaytraceHelper.rayTrace(context.getSource().getWorld(), context.getSource().asPlayer());
        BlockPos tracePos = trace.getPos();

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
        return 0;
    }
}
