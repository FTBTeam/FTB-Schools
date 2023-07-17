package dev.ftb.mods.ftbschools;

import dev.ftb.mods.ftbschools.register.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(FTBSchools.MOD_ID)
public class FTBSchools {
    public static final String MOD_ID = "ftbschools";

    public static final Logger LOGGER = LogManager.getLogger("FTB Schools");

    public static FTBSchoolsProxy PROXY;

    public static boolean curiosAvailable;

    public FTBSchools() {
        PROXY = DistExecutor.safeRunForDist(() -> FTBSchoolsProxy.Client::new, () -> FTBSchoolsProxy.Common::new);

        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();

        ModItems.ITEMS.register(modBus);
        ModBlocks.BLOCKS.register(modBus);
        ModChunkGenerators.CHUNK_GEN.register(modBus);
        ModArgumentTypes.ARG_TYPES.register(modBus);
        ModStructureProcessors.PROCESSORS.register(modBus);

        curiosAvailable = ModList.get().isLoaded("curios");
    }

    public static ResourceLocation id(String path) {
        return new ResourceLocation(MOD_ID, path);
    }
}
