package com.feed_the_beast.mods.ftbschools.world;

import com.feed_the_beast.mods.ftbschools.FTBSchools;
import com.feed_the_beast.mods.ftbschools.block.FTBSchoolsBlocks;
import com.feed_the_beast.mods.ftbschools.block.SpawnMarkerBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

public class SchoolStructure {

    public final String name;

    public final BlockPos spawn;
    public final Direction spawnFacing;

    private SchoolStructure(String name, BlockPos spawn, Direction spawnFacing) {
        this.name = name;
        this.spawn = spawn;
        this.spawnFacing = spawnFacing;

        StructureManager.schools.merge(name, this, (a, b) -> {
            throw new IllegalStateException("A school with the specified name already exists!");
        });
    }

    @SuppressWarnings("UnusedReturnValue")
    public static SchoolStructure parse(String name, StructureTemplate template) {
        BlockPos spawn = null;
        Direction spawnFacing = Direction.NORTH;

        for (StructureTemplate.StructureBlockInfo info : template.filterBlocks(BlockPos.ZERO, new StructurePlaceSettings(), FTBSchoolsBlocks.SPAWN_MARKER.get())) {
            spawn = info.pos;
            spawnFacing = info.state.getValue(SpawnMarkerBlock.SPAWN_FACING);
        }

        if (spawn == null) {
            FTBSchools.LOGGER.warn("School structure {} has no spawn point set! Players will spawn at the origin!", name);
            spawn = BlockPos.ZERO;
        }

        return new SchoolStructure(name, spawn, spawnFacing);
    }
}
