package com.feed_the_beast.mods.ftbschools.world;

import com.feed_the_beast.mods.ftbschools.FTBSchools;
import com.feed_the_beast.mods.ftbschools.data.SchoolData;
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

    public static void enterSchool(ServerPlayer player, String school) {
        enterSchool(player, school, false);
    }

    public static void enterSchool(ServerPlayer player, String school, boolean forceReset) {
        if (SchoolPlayerData.getActiveSchool(player) != -1 && !forceReset) {
            FTBSchools.LOGGER.warn("Player {} already has an active school!", player.getDisplayName().getString());
            return;
        }

        if (!schoolTypes.containsKey(school)) {
            FTBSchools.LOGGER.warn("School {} doesn't exist!", school);
            return;
        }

        SchoolType type = schoolTypes.get(school);
        ServerLevel schoolLevel = player.server.getLevel(type.getDimension());

        // TODO: save and clear inventory
        BlockPos origin = Util.getCenterOfRegion(SchoolData.INSTANCE.nextId(type.properties.night));

        Vec3 spawnPos = Vec3.upFromBottomCenterOf(origin.offset(type.spawnPos), 1);
        player.sendMessage(new TextComponent("Successfully generated new school @ " + spawnPos), UUID.randomUUID());

        type.build(schoolLevel, origin);
        player.teleportTo(schoolLevel, spawnPos.x, spawnPos.y, spawnPos.z, type.spawnFacing.toYRot(), 0);
    }

}
