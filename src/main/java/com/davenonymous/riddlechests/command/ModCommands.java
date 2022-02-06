package com.davenonymous.riddlechests.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.tree.LiteralCommandNode;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

public class ModCommands {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        LiteralCommandNode<CommandSourceStack> cmdBonsai = dispatcher.register(
                Commands.literal("riddlechests")
                        .then(CommandCheat.register(dispatcher))
        );
    }
}