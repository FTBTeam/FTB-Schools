package com.feed_the_beast.mods.ftbschools.world;

import com.feed_the_beast.mods.ftbschools.FTBSchools;
import com.feed_the_beast.mods.ftbschools.block.FTBSchoolsBlocks;
import com.feed_the_beast.mods.ftbschools.block.SchoolTypeMarkerBlock;
import com.feed_the_beast.mods.ftbschools.block.SpawnMarkerBlock;
import com.feed_the_beast.mods.ftbschools.util.StructureExcluded;
import com.google.common.collect.ImmutableList;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockIgnoreProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate.StructureBlockInfo;
import net.minecraftforge.fml.RegistryObject;

import static com.feed_the_beast.mods.ftbschools.world.SchoolManager.SchoolType;
import static com.feed_the_beast.mods.ftbschools.world.SchoolManager.schools;

public class SchoolStructure {

    private final StructureTemplate template;

    public final String name;

    public final BlockPos spawn;
    public final Direction spawnFacing;
    public final SchoolType type;

    private SchoolStructure(StructureTemplate template, String name, BlockPos spawn, Direction spawnFacing, SchoolType type) {
        this.template = template;
        this.name = name;
        this.spawn = spawn;
        this.spawnFacing = spawnFacing;
        this.type = type;

        schools.merge(name, this, (a, b) -> {
            throw new IllegalStateException("A school with the specified name already exists!");
        });
    }

    @SuppressWarnings("UnusedReturnValue")
    public static SchoolStructure parse(String name, StructureTemplate template) {
        BlockPos spawn = null;
        Direction spawnFacing = Direction.NORTH;
        SchoolType type = SchoolType.DAY;

        for (StructureBlockInfo info : template.filterBlocks(BlockPos.ZERO, new StructurePlaceSettings(), FTBSchoolsBlocks.SPAWN_MARKER.get())) {
            spawn = info.pos;
            spawnFacing = info.state.getValue(SpawnMarkerBlock.SPAWN_FACING);
            break;
        }

        if (spawn == null) {
            FTBSchools.LOGGER.warn("School structure {} has no spawn point set! Players will spawn at the origin!", name);
            spawn = BlockPos.ZERO;
        }

        for (StructureBlockInfo info : template.filterBlocks(BlockPos.ZERO, new StructurePlaceSettings(), FTBSchoolsBlocks.SCHOOL_TYPE_MARKER.get())) {
            type = info.state.getValue(SchoolTypeMarkerBlock.TYPE);
            break;
        }

        return new SchoolStructure(template, name, spawn, spawnFacing, type);
    }

    @SuppressWarnings("UnstableApiUsage")
    public void build(ServerLevel level, BlockPos origin) {
        StructurePlaceSettings settings = new StructurePlaceSettings();
        settings.addProcessor(BlockIgnoreProcessor.STRUCTURE_AND_AIR);
        settings.addProcessor(new BlockIgnoreProcessor(FTBSchoolsBlocks.BLOCKS.getEntries()
                .stream().map(RegistryObject::get).filter(StructureExcluded.class::isInstance)
                .collect(ImmutableList.toImmutableList())));
        template.placeInWorld(level, origin, settings, level.random);
    }
}
