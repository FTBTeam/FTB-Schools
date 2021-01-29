package com.feed_the_beast.mods.ftbschools.block;

import com.feed_the_beast.mods.ftbschools.util.StructureExcluded;
import com.feed_the_beast.mods.ftbschools.world.SchoolManager;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;

public class SchoolTypeMarkerBlock extends Block implements StructureExcluded {

    public static final EnumProperty<SchoolManager.SchoolType> TYPE = EnumProperty.create("school_type", SchoolManager.SchoolType.class);

    public SchoolTypeMarkerBlock() {
        super(Properties.copy(Blocks.WHITE_WOOL));
        registerDefaultState(this.getStateDefinition().any().setValue(TYPE, SchoolManager.SchoolType.NIGHT));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(TYPE);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (!level.isClientSide) {
            level.setBlockAndUpdate(pos, state.cycle(TYPE));
        }
        player.swing(hand);
        return InteractionResult.sidedSuccess(level.isClientSide);
    }
}
