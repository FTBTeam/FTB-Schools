package com.feed_the_beast.mods.ftbschools;

import com.feed_the_beast.mods.ftbschools.block.FTBSchoolsBlocks;
import com.feed_the_beast.mods.ftbschools.block.FTBSchoolsItems;
import com.feed_the_beast.mods.ftbschools.world.SchoolChunkGenerator;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(FTBSchools.MOD_ID)
public class FTBSchools {
    public static final String MOD_ID = "ftbschools";

    public static final Logger LOGGER = LogManager.getLogger();

    public static FTBSchoolsProxy PROXY;

    public FTBSchools() {
        Registry.register(Registry.CHUNK_GENERATOR, id("school"), SchoolChunkGenerator.CODEC);

        PROXY = DistExecutor.safeRunForDist(() -> FTBSchoolsProxy.Client::new, () -> FTBSchoolsProxy.Common::new);

        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();

        FTBSchoolsItems.ITEMS.register(modBus);
        FTBSchoolsBlocks.BLOCKS.register(modBus);

        new FTBSchoolsEventHandler().init();
    }

    public static ResourceLocation id(String path) {
        return new ResourceLocation(MOD_ID, path);
    }
}
