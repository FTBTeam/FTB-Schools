package com.feed_the_beast.mods.ftbschools.data;

import com.feed_the_beast.mods.ftbschools.FTBSchools;
import com.feed_the_beast.mods.ftbschools.block.FTBSchoolsBlocks;
import com.feed_the_beast.mods.ftbschools.block.SpawnMarkerBlock;
import com.feed_the_beast.mods.ftbschools.util.StructureExcluded;
import com.google.common.collect.ImmutableList;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockIgnoreProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraftforge.fml.RegistryObject;

import java.util.function.Consumer;

public class SchoolType {

    public static final ResourceKey<Level> DAY_DIMENSION = ResourceKey.create(Registry.DIMENSION_REGISTRY, FTBSchools.id("school_day"));
    public static final ResourceKey<Level> NIGHT_DIMENSION = ResourceKey.create(Registry.DIMENSION_REGISTRY, FTBSchools.id("school_night"));

    public final String id;
    public final Properties properties;
    private final StructureTemplate template;

    public final BlockPos spawnPos;
    public final Direction spawnFacing;

    public SchoolType(String id, Properties properties, StructureTemplate template) {
        this.id = id;
        this.properties = properties;
        this.template = template;

        BlockPos spawnPos = null;
        Direction spawnFacing = Direction.NORTH;

        for (StructureTemplate.StructureBlockInfo info : template.filterBlocks(BlockPos.ZERO, new StructurePlaceSettings(), FTBSchoolsBlocks.SPAWN_MARKER.get())) {
            spawnPos = info.pos;
            spawnFacing = info.state.getValue(SpawnMarkerBlock.SPAWN_FACING);
            break;
        }

        if (spawnPos == null) {
            FTBSchools.LOGGER.warn("School structure {} has no spawn point set! Players will spawnPos at the origin!", id);
            spawnPos = BlockPos.ZERO;
        }

        this.spawnPos = spawnPos;
        this.spawnFacing = spawnFacing;
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

    public ResourceKey<Level> getDimension() {
        return properties.night ? NIGHT_DIMENSION : DAY_DIMENSION;
    }

    public static class Properties {
        public final boolean night;

        private Properties(boolean night) {
            this.night = night;
        }
    }

    public static class Builder {
        private boolean night = false;
        // TODO: Block Interaction callbacks

        public Builder(Consumer<Builder> fn) {
            fn.accept(this);
        }

        public Builder night() {
            night = true;
            return this;
        }

        public Properties build() {
            return new Properties(night);
        }
    }

}
