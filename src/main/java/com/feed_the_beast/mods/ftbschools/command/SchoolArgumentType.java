package com.feed_the_beast.mods.ftbschools.command;

import com.feed_the_beast.mods.ftbschools.data.SchoolManager;
import com.feed_the_beast.mods.ftbschools.data.SchoolType;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;

import java.util.concurrent.CompletableFuture;

public class SchoolArgumentType implements ArgumentType<SchoolType> {

    private static final DynamicCommandExceptionType INVALID_TYPE = new DynamicCommandExceptionType((arg) ->
            new TranslatableComponent("command.school.notFound", arg));

    public static SchoolType getSchool(CommandContext<CommandSourceStack> ctx, String name) {
        return ctx.getArgument(name, SchoolType.class);
    }

    public static SchoolArgumentType school() {
        return new SchoolArgumentType();
    }

    @Override
    public SchoolType parse(StringReader reader) throws CommandSyntaxException {
        ResourceLocation id = ResourceLocation.read(reader);
        SchoolType type = SchoolManager.INSTANCE.schoolTypes.get(id);
        if (type == null) {
            throw INVALID_TYPE.createWithContext(reader, id);
        }
        return type;
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        return SharedSuggestionProvider.suggestResource(SchoolManager.INSTANCE.schoolTypes.keySet(), builder);
    }

}
