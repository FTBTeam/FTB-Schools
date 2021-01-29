package com.feed_the_beast.mods.ftbschools.world;

import com.feed_the_beast.mods.ftbschools.FTBSchools;
import com.feed_the_beast.mods.ftbschools.util.IOUtil;
import net.minecraft.nbt.NbtIo;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.SuffixFileFilter;

import java.util.HashMap;
import java.util.Map;

public class StructureManager {

    public static final Map<String, SchoolStructure> schools = new HashMap<>();

    public static void init() {
        FileUtils.iterateFiles(IOUtil.CONFIG.toFile(), new SuffixFileFilter(".nbt"), null).forEachRemaining(file -> {
            StructureTemplate template = new StructureTemplate();
            IOUtil.tryIO(() -> template.load(NbtIo.readCompressed(file)));

            SchoolStructure struct = SchoolStructure.parse(file.getName().replace(".nbt", ""), template);
            FTBSchools.LOGGER.info("School: {} (Spawn {}:{})", struct.name, struct.spawn, struct.spawnFacing);
        });
        FTBSchools.LOGGER.info(schools);
    }


}
