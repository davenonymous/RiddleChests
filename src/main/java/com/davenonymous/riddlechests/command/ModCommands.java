package com.davenonymous.riddlechests.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;

public class ModCommands {
    public static void register(CommandDispatcher<CommandSource> dispatcher) {
        LiteralCommandNode<CommandSource> cmdBonsai = dispatcher.register(
                Commands.literal("riddlechests")
                        .then(CommandCheat.register(dispatcher))
        );
    }
}
