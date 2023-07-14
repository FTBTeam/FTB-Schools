package dev.ftb.mods.ftbschools.command;

import com.google.gson.JsonObject;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import dev.ftb.mods.ftbschools.data.SchoolManager;
import dev.ftb.mods.ftbschools.data.SchoolType;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public class SchoolArgumentType implements ArgumentType<SchoolType> {
    private static final DynamicCommandExceptionType INVALID_TYPE
            = new DynamicCommandExceptionType(arg -> Component.translatable("command.school.notFound", arg));
    public static final DynamicCommandExceptionType COMMAND_DISABLED
            = new DynamicCommandExceptionType(arg -> Component.translatable("command.school.commandDisabled", arg));

    private final Supplier<Map<ResourceLocation, SchoolType>> schoolTypes;

    private SchoolArgumentType(Supplier<Map<ResourceLocation, SchoolType>> schoolTypes) {
        this.schoolTypes = schoolTypes;
    }

    public static SchoolType getSchool(CommandContext<CommandSourceStack> ctx, String name) {
        return ctx.getArgument(name, SchoolType.class);
    }

    public static SchoolArgumentType school() {
        return new SchoolArgumentType(() -> SchoolManager.INSTANCE.schoolTypes);
    }

    @Override
    public SchoolType parse(StringReader reader) throws CommandSyntaxException {
        ResourceLocation id = ResourceLocation.read(reader);
        if (!schoolTypes.get().containsKey(id)) {
            throw INVALID_TYPE.createWithContext(reader, id);
        }
        return schoolTypes.get().get(id);
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        return SharedSuggestionProvider.suggestResource(schoolTypes.get().keySet(), builder);
    }

    public static class Info implements ArgumentTypeInfo<SchoolArgumentType, Info.Template> {
        @Override
        public void serializeToNetwork(Template template, FriendlyByteBuf buffer) {
            buffer.writeMap(template.ids, FriendlyByteBuf::writeResourceLocation, (buf, schoolType) -> schoolType.toNetwork(buf));
        }

        @Override
        public Template deserializeFromNetwork(FriendlyByteBuf buffer) {
            return new Template(this, buffer.readMap(FriendlyByteBuf::readResourceLocation, SchoolType::fromNetwork));
        }

        @Override
        public Template unpack(SchoolArgumentType argument) {
            return new Template(this, argument.schoolTypes.get());
        }

        @Override
        public void serializeToJson(Template template, JsonObject json) {
            JsonObject map = new JsonObject();
            template.ids.forEach((id, schoolType) -> {
                map.add(id.toString(), schoolType.toJson());
            });
            json.add("school_types", map);
        }

        public record Template(Info info, Map<ResourceLocation, SchoolType> ids) implements ArgumentTypeInfo.Template<SchoolArgumentType> {
            @Override
            public SchoolArgumentType instantiate(CommandBuildContext context) {
                return new SchoolArgumentType(() -> ids);
            }

            @Override
            public ArgumentTypeInfo<SchoolArgumentType, ?> type() {
                return info;
            }
        }
    }
}
