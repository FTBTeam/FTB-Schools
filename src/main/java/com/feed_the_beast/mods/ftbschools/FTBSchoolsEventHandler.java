package com.feed_the_beast.mods.ftbschools;

import com.feed_the_beast.mods.ftbschools.data.SchoolData;
import com.feed_the_beast.mods.ftbschools.world.SchoolManager;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.commands.Commands;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.server.FMLServerAboutToStartEvent;
import net.minecraftforge.fml.event.server.FMLServerStartedEvent;
import net.minecraftforge.fml.event.server.FMLServerStoppedEvent;

public class FTBSchoolsEventHandler {

    public void init() {
        MinecraftForge.EVENT_BUS.addListener(this::serverAboutToStart);
        MinecraftForge.EVENT_BUS.addListener(this::registerCommands);
        MinecraftForge.EVENT_BUS.addListener(this::serverStarted);
        MinecraftForge.EVENT_BUS.addListener(this::serverStopped);
        MinecraftForge.EVENT_BUS.addListener(this::levelSaved);
    }

    private void serverAboutToStart(FMLServerAboutToStartEvent event) {
        SchoolData.INSTANCE = new SchoolData(event.getServer());
        SchoolManager.init();
    }

    private void registerCommands(RegisterCommandsEvent event) {
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
    }

    private void serverStarted(FMLServerStartedEvent event) {
        SchoolData.INSTANCE.load();
    }

    private void serverStopped(FMLServerStoppedEvent event) {
        SchoolData.INSTANCE.save();
        SchoolData.INSTANCE = null;
    }

    private void levelSaved(WorldEvent.Save event) {
        SchoolData.INSTANCE.save();
    }
}
