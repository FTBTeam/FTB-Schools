package com.feed_the_beast.mods.ftbschools.world;

import com.feed_the_beast.mods.ftbschools.FTBSchools;
import com.feed_the_beast.mods.ftbschools.player.SchoolPlayerData;
import com.feed_the_beast.mods.ftbschools.util.IOUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.nbt.NbtIo;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.phys.Vec3;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.SuffixFileFilter;

import java.util.HashMap;
import java.util.Map;

public class SchoolManager {

    public static final Map<String, SchoolStructure> schools = new HashMap<>();

    public static void init() {
        FileUtils.iterateFiles(IOUtil.CONFIG.toFile(), new SuffixFileFilter(".nbt"), null).forEachRemaining(file -> {
            StructureTemplate template = new StructureTemplate();
            IOUtil.tryIO(() -> template.load(NbtIo.readCompressed(file)));

            SchoolStructure struct = SchoolStructure.parse(file.getName().replace(".nbt", ""), template);
            FTBSchools.LOGGER.info("School: {} (Spawn {}:{}){}", struct.name, struct.spawn, struct.spawnFacing, struct.type == SchoolType.NIGHT ? " (Night Time)" : "");
        });
        FTBSchools.LOGGER.info(schools);
    }

    public static void enterSchool(ServerPlayer player, String school) {
        enterSchool(player, school, false);
    }

    public static void enterSchool(ServerPlayer player, String school, boolean forceReset) {
        if (SchoolPlayerData.getActiveSchool(player) != -1 && !forceReset) {
            FTBSchools.LOGGER.warn("Player {} already has an active school!", player.getDisplayName().getString());
            return;
        }

        if (!schools.containsKey(school)) {
            FTBSchools.LOGGER.warn("School {} doesn't exist!", school);
            return;
        }

        // TODO: save and clear inventory
        BlockPos origin = BlockPos.ZERO; // TODO: keep track of existing schools, align them to region files

        SchoolStructure struct = schools.get(school);
        ServerLevel schoolLevel = player.server.getLevel(struct.type.LEVEL);
        Vec3 spawnPos = Vec3.upFromBottomCenterOf(origin.offset(struct.spawn), 1);

        struct.build(schoolLevel, origin);
        player.teleportTo(schoolLevel, spawnPos.x, spawnPos.y, spawnPos.z, struct.spawnFacing.toYRot(), 0);
    }

    public enum SchoolType implements StringRepresentable {
        DAY("day"),
        NIGHT("night");

        private final String name;

        public final ResourceKey<DimensionType> DIMENSION;
        public final ResourceKey<Level> LEVEL;

        SchoolType(String name) {
            this.name = name;
            DIMENSION = ResourceKey.create(Registry.DIMENSION_TYPE_REGISTRY, FTBSchools.id("school_" + name));
            LEVEL = ResourceKey.create(Registry.DIMENSION_REGISTRY, FTBSchools.id("school_" + name));
        }

        @Override
        public String getSerializedName() {
            return name;
        }
    }
}
