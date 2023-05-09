package dev.ftb.mods.ftbschools.structure;

import com.mojang.serialization.Codec;
import dev.ftb.mods.ftbschools.kubejs.FTBSchoolsEvents;
import dev.ftb.mods.ftbschools.kubejs.ReplaceBlockEventJS;
import dev.ftb.mods.ftbschools.register.ModStructureProcessors;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate.StructureBlockInfo;
import org.jetbrains.annotations.Nullable;

public class StructureBlockReplacerProcessor extends StructureProcessor {
    public static final StructureBlockReplacerProcessor INSTANCE = new StructureBlockReplacerProcessor();

    public static final Codec<StructureBlockReplacerProcessor> CODEC = Codec.unit(() -> INSTANCE);

    @Nullable
    @Override
    public StructureBlockInfo process(LevelReader lr,
                                      BlockPos pos, BlockPos pos2,
                                      StructureBlockInfo infoRel, StructureBlockInfo infoAbs,
                                      StructurePlaceSettings _settings, @Nullable StructureTemplate _template) {
        if (infoAbs.state.is(Blocks.STRUCTURE_BLOCK)) {
            String meta = infoAbs.nbt.getString("metadata");
            if (meta.startsWith("ftbschools:")) {
                String meta1 = meta.split(":", 2)[1];
                if (!meta1.isEmpty()) {
                    ReplaceBlockEventJS event = new ReplaceBlockEventJS(infoAbs.pos, infoRel.pos, meta1);
                    FTBSchoolsEvents.REPLACE_BLOCK.post(event);
                    if (event.getNewState() != null) {
                        return new StructureBlockInfo(infoAbs.pos, event.getNewState(), event.getNewNbt());
                    }
                }
            }
        }

        return infoAbs;
    }

    @Override
    protected StructureProcessorType<?> getType() {
        return ModStructureProcessors.NBT_FIXER.get();
    }
}
