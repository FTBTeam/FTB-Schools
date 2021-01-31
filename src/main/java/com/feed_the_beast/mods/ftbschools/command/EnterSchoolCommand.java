package com.feed_the_beast.mods.ftbschools.command;

import com.feed_the_beast.mods.ftbschools.data.SchoolType;
import com.feed_the_beast.mods.ftbschools.world.SchoolManager;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

public class EnterSchoolCommand {

    public static LiteralArgumentBuilder<CommandSourceStack> get() {
        return Commands.literal("enter_school")
                .then(Commands.argument("school", SchoolArgumentType.school())
                        .then(Commands.argument("force_reset", BoolArgumentType.bool())
                                .executes((ctx) -> {
                                    SchoolManager.enterSchool(ctx.getSource().getPlayerOrException(),
                                            ctx.getArgument("school", SchoolType.class),
                                            ctx.getArgument("force_reset", Boolean.class));
                                    return Command.SINGLE_SUCCESS;
                                }))
                        .executes((ctx) -> {
                            SchoolManager.enterSchool(ctx.getSource().getPlayerOrException(),
                                    ctx.getArgument("school", SchoolType.class));
                            return Command.SINGLE_SUCCESS;
                        })
                );
    }

}
