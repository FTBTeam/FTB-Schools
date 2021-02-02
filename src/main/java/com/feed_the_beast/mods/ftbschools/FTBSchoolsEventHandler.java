package com.feed_the_beast.mods.ftbschools;

import com.feed_the_beast.mods.ftbschools.command.EnterSchoolCommand;
import com.feed_the_beast.mods.ftbschools.data.SchoolServerData;
import com.feed_the_beast.mods.ftbschools.world.SchoolManager;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.server.FMLServerAboutToStartEvent;
import net.minecraftforge.fml.event.server.FMLServerStartedEvent;
import net.minecraftforge.fml.event.server.FMLServerStoppedEvent;

@Mod.EventBusSubscriber(modid = FTBSchools.MOD_ID)
public class FTBSchoolsEventHandler {
    @SubscribeEvent
    public static void serverAboutToStart(FMLServerAboutToStartEvent event) {
        SchoolServerData.INSTANCE = new SchoolServerData(event.getServer());
        SchoolManager.init();
    }

    @SubscribeEvent
    public static void registerCommands(RegisterCommandsEvent event) {
        event.getDispatcher().register(EnterSchoolCommand.get());
    }

    @SubscribeEvent
    public static void serverStarted(FMLServerStartedEvent event) {
        SchoolServerData.INSTANCE.loadAll();
    }

    @SubscribeEvent
    public static void serverStopped(FMLServerStoppedEvent event) {
        SchoolServerData.INSTANCE.saveAll();
        SchoolServerData.INSTANCE = null;
    }

    @SubscribeEvent
    public static void levelSaved(WorldEvent.Save event) {
        SchoolServerData.INSTANCE.saveAll();
    }
}
