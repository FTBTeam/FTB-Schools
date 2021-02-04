package com.feed_the_beast.mods.ftbschools.world;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.RegistryAccess;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.NoiseColumn;
import net.minecraft.world.level.StructureFeatureManager;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.StructureSettings;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.Optional;

public class SchoolChunkGenerator extends ChunkGenerator {

    public static final Codec<SchoolChunkGenerator> CODEC = RecordCodecBuilder.create((instance) ->
            instance.group(
                    BiomeSource.CODEC.fieldOf("biome_source")
                            .forGetter(gen -> gen.biomeSource)
            ).apply(instance, instance.stable(SchoolChunkGenerator::new)));

    public SchoolChunkGenerator(BiomeSource biomeSource) {
        super(biomeSource, new StructureSettings(Optional.empty(), Collections.emptyMap()));
    }

    @Override
    protected Codec<? extends ChunkGenerator> codec() {
        return CODEC;
    }

    @Override
    public ChunkGenerator withSeed(long l) {
        return this;
    }

    @Override
    public void buildSurfaceAndBedrock(WorldGenRegion arg, ChunkAccess arg2) {
    }

    @Nullable
    @Override
    public BlockPos findNearestMapFeature(ServerLevel arg, StructureFeature<?> arg2, BlockPos arg3, int i, boolean bl) {
        return null;
    }

    @Override
    public void applyBiomeDecoration(WorldGenRegion arg, StructureFeatureManager arg2) {
    }

    @Override
    public void createStructures(RegistryAccess arg, StructureFeatureManager arg2, ChunkAccess arg3, StructureManager arg4, long l) {
    }

    @Override
    public void fillFromNoise(LevelAccessor arg, StructureFeatureManager arg2, ChunkAccess arg3) {
    }

    @Override
    public int getBaseHeight(int i, int j, Heightmap.Types arg) {
        return 0;
    }

    @Override
    public BlockGetter getBaseColumn(int i, int j) {
        return new NoiseColumn(new BlockState[0]);
    }
}
