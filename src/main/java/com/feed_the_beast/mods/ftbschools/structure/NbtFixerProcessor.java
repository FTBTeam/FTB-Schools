package com.feed_the_beast.mods.ftbschools.structure;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate.StructureBlockInfo;
import net.minecraftforge.common.util.Constants;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Predicate;

public class NbtFixerProcessor extends StructureProcessor {

    public static final List<NbtFixerRule> rules = new ArrayList<>();

    public static final Codec<NbtFixerProcessor> CODEC = Codec.unit(NbtFixerProcessor::new);

    private final StructureProcessorType<NbtFixerProcessor> TYPE = StructureProcessorType.register("ftbschools:nbt_fixer", CODEC);

    @Nullable
    @Override
    public StructureBlockInfo process(LevelReader lr,
                                      BlockPos pos, BlockPos pos2,
                                      StructureBlockInfo infoRel, StructureBlockInfo infoAbs,
                                      StructurePlaceSettings _settings, @Nullable StructureTemplate _template) {
        StructureBlockInfo fixed = infoAbs;
        for (NbtFixerRule rule : rules) {
            fixed = rule.apply(fixed, infoRel.pos);
        }
        return fixed;
    }

    @Override
    protected StructureProcessorType<?> getType() {
        return TYPE;
    }

    static {
        // Astral crystals
        rules.add(new NbtFixerRule(
                registryNameMatch(
                        "astralsorcery:collector_crystal",
                        "astralsorcery:lens"
                ),
                (info, rel) -> {
                    CompoundTag nbt = info.nbt.copy();

                    BlockPos oldPos = new BlockPos(nbt.getInt("x"), nbt.getInt("y"), nbt.getInt("z"));
                    BlockPos newPos = info.pos;

                    // Fix own position
                    nbt.putInt("x", newPos.getX());
                    nbt.putInt("y", newPos.getX());
                    nbt.putInt("z", newPos.getZ());

                    // Fix linked positions
                    nbt.getList("linked", Constants.NBT.TAG_COMPOUND).forEach(tag -> {
                        bPosFixAstral((CompoundTag) tag, newPos.subtract(oldPos));
                    });

                    // Fix other connections (on lenses)
                    nbt.getList("occupiedConnections", Constants.NBT.TAG_COMPOUND).forEach(tag -> {
                        bPosFixAstral((CompoundTag) tag, newPos.subtract(oldPos));
                    });

                    return new StructureBlockInfo(info.pos, info.state, nbt);
                }
        ));
    }

    private static void bPosFixAstral(CompoundTag pos, BlockPos offset) {
        BlockPos link = new BlockPos(pos.getInt("bposX"), pos.getInt("bposY"), pos.getInt("bposZ"));

        BlockPos newLink = link.offset(offset);
        pos.putInt("bposX", newLink.getX());
        pos.putInt("bposY", newLink.getY());
        pos.putInt("bposZ", newLink.getZ());
    }

    private static Predicate<StructureBlockInfo> registryNameMatch(String... acceptedNames) {
        return (info) -> {
            ResourceLocation id = info.state.getBlock().getRegistryName();
            for (String name : acceptedNames) {
                if (new ResourceLocation(name).equals(id)) return true;
            }
            return false;
        };
    }

    private static class NbtFixerRule {
        private final Predicate<StructureBlockInfo> predicate;
        private final BiFunction<StructureBlockInfo, BlockPos, StructureBlockInfo> transformer;

        private NbtFixerRule(Predicate<StructureBlockInfo> predicate, BiFunction<StructureBlockInfo, BlockPos, StructureBlockInfo> transformer) {
            this.predicate = predicate;
            this.transformer = transformer;
        }

        private StructureBlockInfo apply(StructureBlockInfo info, BlockPos rel) {
            if (info != null && predicate.test(info)) {
                return transformer.apply(info, rel);
            }
            return info;
        }
    }
}
