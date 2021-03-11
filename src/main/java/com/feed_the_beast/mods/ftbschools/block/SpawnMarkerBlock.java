package com.feed_the_beast.mods.ftbschools.block;

import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import org.jetbrains.annotations.Nullable;

public class SpawnMarkerBlock extends Block {

    public static final DirectionProperty SPAWN_FACING = BlockStateProperties.HORIZONTAL_FACING;

    public SpawnMarkerBlock() {
        super(Properties.copy(Blocks.WHITE_WOOL).noOcclusion());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(SPAWN_FACING);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(SPAWN_FACING, context.getHorizontalDirection().getOpposite());
    }


}
