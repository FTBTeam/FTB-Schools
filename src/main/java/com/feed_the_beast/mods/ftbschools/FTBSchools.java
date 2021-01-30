package com.feed_the_beast.mods.ftbschools;

import com.feed_the_beast.mods.ftbschools.block.FTBSchoolsBlocks;
import com.feed_the_beast.mods.ftbschools.block.FTBSchoolsItems;
import com.feed_the_beast.mods.ftbschools.world.SchoolChunkGenerator;
import com.feed_the_beast.mods.ftbschools.world.SchoolManager;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.commands.Commands;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
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

        forgeBus.addListener((FMLServerStartingEvent event) -> SchoolManager.init());

        forgeBus.addListener((RegisterCommandsEvent event) -> {
            event.getDispatcher().register(
                    Commands.literal("enter_school")
                            .then(Commands.argument("school", StringArgumentType.word())
                                    .then(Commands.argument("force_reset", BoolArgumentType.bool())
                                            .executes((ctx) -> {
                                                SchoolManager.enterSchool(ctx.getSource().getPlayerOrException(),
                                                        ctx.getArgument("school", String.class),
                                                        ctx.getArgument("force_reset", Boolean.class));
                                                return Command.SINGLE_SUCCESS;
                                            }))
                                    .executes((ctx) -> {
                                        SchoolManager.enterSchool(ctx.getSource().getPlayerOrException(),
                                                ctx.getArgument("school", String.class));
                                        return Command.SINGLE_SUCCESS;
                                    })
                            ));
        });
    }

    public static ResourceLocation id(String path) {
        return new ResourceLocation(MOD_ID, path);
    }
}
