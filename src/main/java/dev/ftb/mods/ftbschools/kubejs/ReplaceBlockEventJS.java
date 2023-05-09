package dev.ftb.mods.ftbschools.kubejs;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.ftb.mods.ftbschools.FTBSchools;
import dev.latvian.mods.kubejs.event.EventJS;
import net.minecraft.commands.arguments.blocks.BlockStateParser;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.ForgeRegistries;

public class ReplaceBlockEventJS extends EventJS {
    private final BlockPos absolutePos;
    private final BlockPos relativePos;
    private final String tag;
    private BlockState newState;
    private CompoundTag newNbt;

    public ReplaceBlockEventJS(BlockPos absolutePos, BlockPos relativePos, String tag) {
        this.absolutePos = absolutePos;
        this.relativePos = relativePos;
        this.tag = tag;
    }

    public BlockPos getAbsolutePos() {
        return absolutePos;
    }

    public BlockPos getRelativePos() {
        return relativePos;
    }

    public String getTag() {
        return tag;
    }

    public BlockState getNewState() {
        return newState;
    }

    public CompoundTag getNewNbt() {
        return newNbt;
    }

    public void replaceWith(String newState) {
        try {
            BlockStateParser.BlockResult blockResult = BlockStateParser.parseForBlock(Registry.BLOCK, newState, true);
            this.newState = blockResult.blockState();
            this.newNbt = blockResult.nbt();
        } catch (CommandSyntaxException e) {
            FTBSchools.LOGGER.error("caught error from ReplaceBlockEventJS.replaceWith({}): {}", newState, e.getMessage());
        }
    }
}
