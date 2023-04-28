package dev.ftb.mods.ftbschools.structure;

import com.mojang.serialization.Codec;
import dev.ftb.mods.ftbschools.FTBSchools;
import dev.ftb.mods.ftbschools.register.ModStructureProcessors;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate.StructureBlockInfo;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Predicate;

public class NbtFixerProcessor extends StructureProcessor {
    public static final List<NbtFixerRule> RULES = new ArrayList<>();

    public static final Codec<NbtFixerProcessor> CODEC = Codec.unit(NbtFixerProcessor::new);

    @Nullable
    @Override
    public StructureBlockInfo process(LevelReader lr,
                                      BlockPos pos, BlockPos pos2,
                                      StructureBlockInfo infoRel, StructureBlockInfo infoAbs,
                                      StructurePlaceSettings _settings, @Nullable StructureTemplate _template) {
        StructureBlockInfo fixed = infoAbs;
        for (NbtFixerRule rule : RULES) {
            fixed = rule.apply(fixed, infoRel.pos);
        }
        return fixed;
    }

    @Override
    protected StructureProcessorType<?> getType() {
        return ModStructureProcessors.NBT_FIXER.get();
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
            ResourceLocation id = ForgeRegistries.BLOCKS.getKey(info.state.getBlock());
            for (String name : acceptedNames) {
                if (new ResourceLocation(name).equals(id)) return true;
            }
            return false;
        };
    }

    static {
        RULES.add(
                new NbtFixerRule(registryNameMatch(
                        "astralsorcery:rock_collector_crystal",
                        "astralsorcery:celestial_collector_crystal",
                        "astralsorcery:lens",
                        "astralsorcery:prism"
                ), (info, relativePos) -> {
                    CompoundTag nbt = info.nbt.copy();
                    CompoundTag savedData = nbt.getCompound("ftbschools.saved");

                    BlockPos oldPos = new BlockPos(savedData.getInt("x"), savedData.getInt("y"), savedData.getInt("z"));
                    BlockPos newPos = info.pos;

                    // Fix own position

                    ResourceLocation id = ForgeRegistries.BLOCKS.getKey(info.state.getBlock());
                    FTBSchools.LOGGER.info("Fixing nbt for object {} with pos {} -> {}", id, oldPos.toShortString(), newPos.toShortString());
                    FTBSchools.LOGGER.info("Offset: {}", newPos.subtract(oldPos).toShortString());

                    // Fix linked positions
                    nbt.getList("linked", Tag.TAG_COMPOUND).forEach(tag -> {
                        FTBSchools.LOGGER.info("Fixing link: {}", tag);
                        bPosFixAstral((CompoundTag) tag, newPos.subtract(oldPos));
                        FTBSchools.LOGGER.info("Fixed link: {}", tag);
                    });

                    // Fix other connections (on lenses)
                    nbt.getList("occupiedConnections", Tag.TAG_COMPOUND).forEach(tag -> {
                        FTBSchools.LOGGER.info("Fixing connection: {}", tag);
                        bPosFixAstral((CompoundTag) tag, newPos.subtract(oldPos));
                        FTBSchools.LOGGER.info("Fixed connection: {}", tag);
                    });

                    return new StructureBlockInfo(info.pos, info.state, nbt);
                }
                )
        );
    }

    record NbtFixerRule(Predicate<StructureBlockInfo> predicate,
                               BiFunction<StructureBlockInfo, BlockPos, StructureBlockInfo> transformer) {
        StructureBlockInfo apply(StructureBlockInfo info, BlockPos rel) {
            if (info != null && predicate.test(info)) {
                return transformer.apply(info, rel);
            }
            return info;
        }
    }
}
