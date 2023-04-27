package dev.ftb.mods.ftbschools;

import dev.ftb.mods.ftbschools.register.FTBSchoolsBlocks;
import dev.ftb.mods.ftbschools.register.FTBSchoolsChunkGenerators;
import dev.ftb.mods.ftbschools.register.FTBSchoolsItems;
import dev.ftb.mods.ftbschools.register.FTBSchoolsArgumentTypes;
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

    public static final Logger LOGGER = LogManager.getLogger("FTB Schools");

    public static FTBSchoolsProxy PROXY;

    public FTBSchools() {
        PROXY = DistExecutor.safeRunForDist(() -> FTBSchoolsProxy.Client::new, () -> FTBSchoolsProxy.Common::new);

        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();

        FTBSchoolsItems.ITEMS.register(modBus);
        FTBSchoolsBlocks.BLOCKS.register(modBus);
        FTBSchoolsChunkGenerators.CHUNK_GEN.register(modBus);
        FTBSchoolsArgumentTypes.ARG_TYPES.register(modBus);
    }

    public static ResourceLocation id(String path) {
        return new ResourceLocation(MOD_ID, path);
    }
}
