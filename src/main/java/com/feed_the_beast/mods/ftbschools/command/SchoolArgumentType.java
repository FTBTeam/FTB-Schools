package com.feed_the_beast.mods.ftbschools.command;

import com.feed_the_beast.mods.ftbschools.data.SchoolType;
import com.feed_the_beast.mods.ftbschools.world.SchoolManager;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.network.chat.TextComponent;

import java.util.concurrent.CompletableFuture;

public class SchoolArgumentType implements ArgumentType<SchoolType> {

    private static final SimpleCommandExceptionType INVALID_TYPE = new SimpleCommandExceptionType(new TextComponent("School does not exist!"));

    public static SchoolType getRegistry(CommandContext<CommandSourceStack> ctx, String name) {
        return ctx.getArgument(name, SchoolType.class);
    }

    public static SchoolArgumentType school() {
        return new SchoolArgumentType();
    }

    @Override
    public SchoolType parse(StringReader reader) throws CommandSyntaxException {
        SchoolType type = SchoolManager.schoolTypes.get(reader.readString());
        if (type == null) {
            throw INVALID_TYPE.createWithContext(reader);
        }
        return type;
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        return SharedSuggestionProvider.suggest(SchoolManager.schoolTypes.keySet(), builder);
    }

}
