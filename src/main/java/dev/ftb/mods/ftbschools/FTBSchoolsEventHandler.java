package dev.ftb.mods.ftbschools;

import dev.ftb.mods.ftbschools.command.SchoolCommands;
import dev.ftb.mods.ftbschools.data.SchoolManager;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.event.server.ServerAboutToStartEvent;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.event.server.ServerStoppedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = FTBSchools.MOD_ID)
public class FTBSchoolsEventHandler {
    @SubscribeEvent
    public static void serverAboutToStart(ServerAboutToStartEvent event) {
        SchoolManager.INSTANCE = new SchoolManager(event.getServer());
    }

    @SubscribeEvent
    public static void registerCommands(RegisterCommandsEvent event) {
        event.getDispatcher().register(SchoolCommands.get());
    }

    @SubscribeEvent
    public static void serverStarted(ServerStartedEvent event) {
        SchoolManager.INSTANCE.loadAll();
    }

    @SubscribeEvent
    public static void serverStopped(ServerStoppedEvent event) {
        SchoolManager.INSTANCE.saveAll();
        SchoolManager.INSTANCE = null;
    }

    @SubscribeEvent
    public static void levelSaved(LevelEvent.Save event) {
        SchoolManager.INSTANCE.saveAll();
    }
}
