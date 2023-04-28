package dev.ftb.mods.ftbschools.register;

import com.mojang.serialization.Codec;
import dev.ftb.mods.ftbschools.FTBSchools;
import dev.ftb.mods.ftbschools.world.SchoolChunkGenerator;
import net.minecraft.core.Registry;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModChunkGenerators {
    public static DeferredRegister<Codec<? extends ChunkGenerator>> CHUNK_GEN
            = DeferredRegister.create(Registry.CHUNK_GENERATOR_REGISTRY, FTBSchools.MOD_ID);

    public static final RegistryObject<Codec<SchoolChunkGenerator>> SCHOOL
            = CHUNK_GEN.register("school", () -> SchoolChunkGenerator.CODEC);
}
