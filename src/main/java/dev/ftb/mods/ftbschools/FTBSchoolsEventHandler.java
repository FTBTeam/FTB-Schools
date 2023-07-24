package dev.ftb.mods.ftbschools;

import com.mojang.brigadier.context.CommandContextBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.ftb.mods.ftbschools.command.SchoolArgumentType;
import dev.ftb.mods.ftbschools.command.SchoolCommands;
import dev.ftb.mods.ftbschools.data.SchoolData;
import dev.ftb.mods.ftbschools.data.SchoolManager;
import it.unimi.dsi.fastutil.objects.Object2LongOpenHashMap;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.CommandEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.event.server.ServerAboutToStartEvent;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.event.server.ServerStoppedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.UUID;

@Mod.EventBusSubscriber(modid = FTBSchools.MOD_ID)
public class FTBSchoolsEventHandler {
    private static final Object2LongOpenHashMap<UUID> pendingDropouts = new Object2LongOpenHashMap<>();

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
        if (player != null) {
            String cmd = context.getNodes().get(0).getNode().getName();
            SchoolData schoolData = SchoolManager.INSTANCE.currentSchool(player);
            if (schoolData != null && (SchoolManager.INSTANCE.commandBlacklist.isCommandDisabled(cmd) || schoolData.commandBlacklist.isCommandDisabled(cmd))) {
                if (!player.hasPermissions(2)) {
                    event.setException(SchoolArgumentType.COMMAND_DISABLED.create(cmd));
                    event.setCanceled(true);
                } else {
                    player.displayClientMessage(Component.translatable("ftbschools.message.commandExempt").withStyle(ChatFormatting.GOLD), true);
                }
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerDeath(LivingDeathEvent event) {
        if (event.getEntity() instanceof ServerPlayer serverPlayer) {
            SchoolData data = SchoolManager.INSTANCE.currentSchool(serverPlayer);
            if (data != null) {
                // defer this a tick; leaving school causes a player teleport, and teleporting the
                // player during a death event is likely to lead to problems
                pendingDropouts.put(serverPlayer.getUUID(), serverPlayer.getServer().getTickCount() + 1L);
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.player instanceof ServerPlayer serverPlayer && pendingDropouts.containsKey(event.player.getUUID())) {
            long when = pendingDropouts.getLong(serverPlayer.getUUID());
            if (when <= serverPlayer.getServer().getTickCount()) {
                try {
                    SchoolManager.INSTANCE.leaveSchool(serverPlayer, true);
                    pendingDropouts.removeLong(serverPlayer.getUUID());
                    serverPlayer.displayClientMessage(Component.translatable("ftbschools.message.leftDueToDeath",
                            serverPlayer.getGameProfile().getName()).withStyle(ChatFormatting.GOLD), false);
                } catch (CommandSyntaxException e) {
                    FTBSchools.LOGGER.error("Unexpected error trying to remove {} from school: {}", serverPlayer.getUUID(), e.getMessage());
                }
            }
        }
    }
}
