package com.feed_the_beast.mods.ftbschools;

import com.feed_the_beast.mods.ftbschools.world.SchoolChunkGenerator;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fml.common.Mod;

@Mod(FTBSchools.MOD_ID)
public class FTBSchools {
    public static final String MOD_ID = "ftbschools";

    public FTBSchools() {
        Registry.register(Registry.CHUNK_GENERATOR, id("school"), SchoolChunkGenerator.CODEC);
    }

    public static ResourceLocation id(String path) {
        return new ResourceLocation(MOD_ID, path);
    }
}
