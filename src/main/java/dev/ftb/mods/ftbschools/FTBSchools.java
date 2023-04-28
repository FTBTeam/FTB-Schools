package dev.ftb.mods.ftbschools;

import dev.ftb.mods.ftbschools.register.ModBlocks;
import dev.ftb.mods.ftbschools.register.ModChunkGenerators;
import dev.ftb.mods.ftbschools.register.ModItems;
import dev.ftb.mods.ftbschools.register.ModArgumentTypes;
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

        ModItems.ITEMS.register(modBus);
        ModBlocks.BLOCKS.register(modBus);
        ModChunkGenerators.CHUNK_GEN.register(modBus);
        ModArgumentTypes.ARG_TYPES.register(modBus);
    }

    public static ResourceLocation id(String path) {
        return new ResourceLocation(MOD_ID, path);
    }
}
