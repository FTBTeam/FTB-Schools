package dev.ftb.mods.ftbschools;

import com.mojang.brigadier.context.CommandContextBuilder;
import dev.ftb.mods.ftbschools.command.SchoolArgumentType;
import dev.ftb.mods.ftbschools.command.SchoolCommands;
import dev.ftb.mods.ftbschools.data.SchoolData;
import dev.ftb.mods.ftbschools.data.SchoolManager;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.CommandEvent;
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
        if (SchoolManager.INSTANCE != null) {
            SchoolManager.INSTANCE.saveAll();
            SchoolManager.INSTANCE = null;
        }
    }

    @SubscribeEvent
    public static void levelSaved(LevelEvent.Save event) {
        SchoolManager.INSTANCE.saveAll();
    }

    @SubscribeEvent
    public static void onCommand(CommandEvent event) {
        CommandContextBuilder<CommandSourceStack> context = event.getParseResults().getContext();
        ServerPlayer player = context.getSource().getPlayer();
        if (player != null && !player.hasPermissions(2)) {
            String cmd = context.getNodes().get(0).getNode().getName();
            SchoolData schoolData = SchoolManager.INSTANCE.currentSchool(player);
            if (schoolData != null && (SchoolManager.INSTANCE.commandBlacklist.isCommandDisabled(cmd) || schoolData.commandBlacklist.isCommandDisabled(cmd))) {
                event.setException(SchoolArgumentType.COMMAND_DISABLED.create(cmd));
                event.setCanceled(true);
            }
        }
    }
}
