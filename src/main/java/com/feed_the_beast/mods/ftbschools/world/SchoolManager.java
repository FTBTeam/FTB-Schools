package com.feed_the_beast.mods.ftbschools.world;

import com.feed_the_beast.mods.ftbschools.FTBSchools;
import com.feed_the_beast.mods.ftbschools.data.SchoolPlayerData;
import com.feed_the_beast.mods.ftbschools.data.SchoolType;
import com.feed_the_beast.mods.ftbschools.kubejs.FTBSchoolsEvents;
import com.feed_the_beast.mods.ftbschools.kubejs.LoadSchoolsEventJS;
import com.feed_the_beast.mods.ftbschools.util.Util;
import com.google.common.collect.ImmutableMap;
import dev.latvian.kubejs.script.ScriptType;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.NbtIo;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.phys.Vec3;

import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

public class SchoolManager {

    public static Map<String, SchoolType> schoolTypes = ImmutableMap.of();

    public static SchoolType makeSchool(String id, Consumer<SchoolType.Builder> props) {
        SchoolType.Properties p = new SchoolType.Builder(props).build();

        StructureTemplate template = new StructureTemplate();
        Util.tryIO(() -> template.load(NbtIo.readCompressed(Util.CONFIG.resolve(id + ".nbt").toFile())));

        return new SchoolType(id, p, template);
    }

    public static void init() {
        ImmutableMap.Builder<String, SchoolType> schools = ImmutableMap.builder();

        new LoadSchoolsEventJS(schools).post(ScriptType.SERVER, FTBSchoolsEvents.LOAD_SCHOOLS);
        SchoolManager.schoolTypes = schools.build();

        FTBSchools.LOGGER.info(SchoolManager.schoolTypes);
    }

    public static void enterSchool(ServerPlayer player, SchoolType school) {
        enterSchool(player, school, false);
    }

    public static void enterSchool(ServerPlayer player, SchoolType school, boolean reset) {
        SchoolPlayerData data = SchoolPlayerData.get(player);

        if (school.equals(data.getSchool()) && !reset) {
            FTBSchools.LOGGER.warn("Player {} is already in that school!", player.getDisplayName().getString());
            return;
        }

        boolean newSchool = reset || !data.hasStarted(school);

        ServerLevel schoolLevel = player.server.getLevel(school.getDimension());

        // TODO: save and clear inventory

        BlockPos origin = Util.getCenterOfRegion(data.setSchool(school, newSchool));
        Vec3 spawnPos = Vec3.upFromBottomCenterOf(origin.offset(school.spawnPos), 1);

        if (newSchool) {
            school.build(schoolLevel, origin);
            player.sendMessage(new TextComponent("Successfully generated new school @ " + spawnPos), UUID.randomUUID());
        } else {
            player.sendMessage(new TextComponent("Successfully generated new school @ " + spawnPos), UUID.randomUUID());
        }

        player.teleportTo(schoolLevel, spawnPos.x, spawnPos.y, spawnPos.z, school.spawnFacing.toYRot(), 0);
    }

}
