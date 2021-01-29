package com.feed_the_beast.mods.ftbschools;

import com.feed_the_beast.mods.ftbschools.block.FTBSchoolsBlocks;
import com.feed_the_beast.mods.ftbschools.block.FTBSchoolsItems;
import com.feed_the_beast.mods.ftbschools.world.SchoolChunkGenerator;
import com.feed_the_beast.mods.ftbschools.world.StructureManager;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(FTBSchools.MOD_ID)
public class FTBSchools {
    public static final String MOD_ID = "ftbschools";

    public static final Logger LOGGER = LogManager.getLogger();

    public FTBSchools() {
        Registry.register(Registry.CHUNK_GENERATOR, id("school"), SchoolChunkGenerator.CODEC);

        IEventBus forgeBus = MinecraftForge.EVENT_BUS;
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();

        FTBSchoolsItems.ITEMS.register(modBus);
        FTBSchoolsBlocks.BLOCKS.register(modBus);

        modBus.addListener((FMLCommonSetupEvent event) -> StructureManager.init());
    }

    public static ResourceLocation id(String path) {
        return new ResourceLocation(MOD_ID, path);
    }
}
