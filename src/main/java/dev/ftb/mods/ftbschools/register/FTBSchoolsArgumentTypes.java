package dev.ftb.mods.ftbschools.register;

import dev.ftb.mods.ftbschools.FTBSchools;
import dev.ftb.mods.ftbschools.command.SchoolArgumentType;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.commands.synchronization.ArgumentTypeInfos;
import net.minecraft.commands.synchronization.SingletonArgumentInfo;
import net.minecraft.core.Registry;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class FTBSchoolsArgumentTypes {
    public static final DeferredRegister<ArgumentTypeInfo<?, ?>> ARG_TYPES
            = DeferredRegister.create(Registry.COMMAND_ARGUMENT_TYPE_REGISTRY, FTBSchools.MOD_ID);

    private static final RegistryObject<SingletonArgumentInfo<SchoolArgumentType>> SCHOOL_ARGUMENT_TYPE
            = ARG_TYPES.register("school",
            () -> ArgumentTypeInfos.registerByClass(SchoolArgumentType.class, SingletonArgumentInfo.contextFree(SchoolArgumentType::new)));
}
