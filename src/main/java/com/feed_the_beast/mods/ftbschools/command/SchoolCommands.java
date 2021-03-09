package com.feed_the_beast.mods.ftbschools.command;

import com.feed_the_beast.mods.ftbschools.data.SchoolManager;
import com.feed_the_beast.mods.ftbschools.data.SchoolType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.server.level.ServerPlayer;

public class SchoolCommands {
    public static LiteralArgumentBuilder<CommandSourceStack> get() {
        return Commands.literal("school")
                .then(Commands.literal("enter")
                        .then(Commands.argument("school", SchoolArgumentType.school())
                                .then(Commands.argument("player", EntityArgument.player())
                                        .requires(s -> s.hasPermission(2))
                                        .executes(ctx ->
                                                enterSchool(EntityArgument.getPlayer(ctx, "player"), SchoolArgumentType.getSchool(ctx, "school"))
                                        )
                                )
                                .executes(ctx ->
                                        enterSchool(ctx.getSource().getPlayerOrException(), SchoolArgumentType.getSchool(ctx, "school"))
                                )
                        ))
                .then(Commands.literal("leave")
                        .executes(ctx ->
                                leaveSchool(ctx.getSource().getPlayerOrException(), true)
                        )
                )
                .then(Commands.literal("complete")
                        .requires(s -> s.hasPermission(2))
                        .then(Commands.argument("player", EntityArgument.player())
                                .executes(ctx ->
                                        leaveSchool(EntityArgument.getPlayer(ctx, "player"), false)
                                )
                        )
                        .executes(ctx ->
                                leaveSchool(ctx.getSource().getPlayerOrException(), false)
                        )
                );
    }

    public static int enterSchool(ServerPlayer player, SchoolType type) throws CommandSyntaxException {
        SchoolManager.INSTANCE.enterSchool(player, type);
        return 1;
    }

    public static int leaveSchool(ServerPlayer player, boolean droppedOut) throws CommandSyntaxException {
        SchoolManager.INSTANCE.leaveSchool(player, true);
        return 1;
    }
}
