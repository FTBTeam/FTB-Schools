package com.feed_the_beast.mods.ftbschools.command;

import com.feed_the_beast.mods.ftbschools.data.SchoolManager;
import com.feed_the_beast.mods.ftbschools.data.SchoolType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

public class SchoolCommands {
    public static LiteralArgumentBuilder<CommandSourceStack> get() {
        return Commands.literal("school")
                .then(Commands.literal("enter")
                        .then(Commands.argument("school", SchoolArgumentType.school())
                                .executes(ctx -> enterSchool(ctx.getSource(), SchoolArgumentType.getSchool(ctx, "school")))
                        ))
                .then(Commands.literal("leave")
                        .executes(ctx -> leaveSchool(ctx.getSource()))
                );
    }

    public static int enterSchool(CommandSourceStack stack, SchoolType type) throws CommandSyntaxException {
        SchoolManager.INSTANCE.enterSchool(stack.getPlayerOrException(), type);
        return 1;
    }

    public static int leaveSchool(CommandSourceStack stack) throws CommandSyntaxException {
        SchoolManager.INSTANCE.leaveSchool(stack.getPlayerOrException());
        return 1;
    }
}
