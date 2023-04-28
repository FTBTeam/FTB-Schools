package dev.ftb.mods.ftbschools.register;

import dev.ftb.mods.ftbschools.FTBSchools;
import dev.ftb.mods.ftbschools.structure.NbtFixerProcessor;
import net.minecraft.core.Registry;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModStructureProcessors {
    public static final DeferredRegister<StructureProcessorType<?>> PROCESSORS
            = DeferredRegister.create(Registry.STRUCTURE_PROCESSOR_REGISTRY, FTBSchools.MOD_ID);

    public static final RegistryObject<StructureProcessorType<NbtFixerProcessor>> NBT_FIXER
            = PROCESSORS.register("nbt_fixer", () -> () -> NbtFixerProcessor.CODEC);
}
