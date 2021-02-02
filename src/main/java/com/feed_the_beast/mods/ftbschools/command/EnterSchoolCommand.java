package com.feed_the_beast.mods.ftbschools.command;

import com.feed_the_beast.mods.ftbschools.data.SchoolManager;
import com.feed_the_beast.mods.ftbschools.data.SchoolType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

public class EnterSchoolCommand {
    public static LiteralArgumentBuilder<CommandSourceStack> get() {
        return Commands.literal("enter_school")
                .then(Commands.argument("school", SchoolArgumentType.school())
                        .executes(ctx -> enterSchool(ctx.getSource(), SchoolArgumentType.getSchool(ctx, "school")))
                );
    }

    public static int enterSchool(CommandSourceStack stack, SchoolType type) throws CommandSyntaxException {
        SchoolManager.INSTANCE.enterSchool(stack.getPlayerOrException(), type);
        return 1;
    }
}
