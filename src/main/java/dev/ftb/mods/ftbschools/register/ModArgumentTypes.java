package dev.ftb.mods.ftbschools.register;

import com.mojang.brigadier.arguments.ArgumentType;
import dev.ftb.mods.ftbschools.FTBSchools;
import dev.ftb.mods.ftbschools.command.SchoolArgumentType;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.commands.synchronization.ArgumentTypeInfos;
import net.minecraft.core.Registry;
import net.minecraftforge.registries.DeferredRegister;

public class ModArgumentTypes {
    public static final DeferredRegister<ArgumentTypeInfo<?, ?>> ARG_TYPES
            = DeferredRegister.create(Registry.COMMAND_ARGUMENT_TYPE_REGISTRY, FTBSchools.MOD_ID);

    private static <T extends ArgumentType<?>> void register(String name, Class<T> type, ArgumentTypeInfo<T, ?> serializer )
    {
        ARG_TYPES.register( name, () -> ArgumentTypeInfos.registerByClass( type, serializer ) );
    }

    static {
        register("school", SchoolArgumentType.class, new SchoolArgumentType.Info());
    }
}
