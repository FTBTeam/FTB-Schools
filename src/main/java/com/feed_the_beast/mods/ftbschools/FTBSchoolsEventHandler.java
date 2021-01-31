package com.feed_the_beast.mods.ftbschools;

import com.feed_the_beast.mods.ftbschools.command.EnterSchoolCommand;
import com.feed_the_beast.mods.ftbschools.data.SchoolData;
import com.feed_the_beast.mods.ftbschools.world.SchoolManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.world.WorldEvent;
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
        event.getDispatcher().register(EnterSchoolCommand.get());
    }

    private void serverStarted(FMLServerStartedEvent event) {
        SchoolData.INSTANCE.loadAll();
    }

    private void serverStopped(FMLServerStoppedEvent event) {
        SchoolData.INSTANCE.saveAll();
        SchoolData.INSTANCE = null;
    }

    private void levelSaved(WorldEvent.Save event) {
        SchoolData.INSTANCE.saveAll();
    }
}
