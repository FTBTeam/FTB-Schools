package dev.ftb.mods.ftbschools.block;

import dev.ftb.mods.ftbschools.FTBSchools;
import dev.ftb.mods.ftbschools.register.ModBlocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BarrierBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class SchoolBarrierBlock extends BarrierBlock {

    public SchoolBarrierBlock() {
        super(Properties.copy(Blocks.BARRIER));
    }

    @Override
    public RenderShape getRenderShape(BlockState arg) {
        return RenderShape.INVISIBLE;
    }

    @Override
    public void animateTick(BlockState state, Level worldIn, BlockPos pos, RandomSource rand) {
        if (FTBSchools.PROXY.shouldBarrierRender()) {
            double d = (double) pos.getX() + 0.5 - (rand.nextDouble() * 0.1);
            double e = (double) pos.getY() + 0.5 - (rand.nextDouble() * 0.1);
            double f = (double) pos.getZ() + 0.5 - (rand.nextDouble() * 0.1);
            double g = 0.4F - rand.nextDouble() * 0.8F;

            LocalPlayer player = Minecraft.getInstance().player;
            if (player != null) {
                if ((rand.nextDouble() < 0.5 && player.isHolding(ModBlocks.BARRIER.get().asItem()))
                        || rand.nextDouble() < (1 - (pos.distSqr(player.blockPosition()) / 32)) * 0.5) {
                    //worldIn.addParticle(new DustParticleOptions(0x00 / 255f, 0x6d / 255f, 0x72 / 255f, rand.nextFloat() * 2), d + g, e + g, f + g, rand.nextGaussian() * 0.1, rand.nextGaussian() * 0.1, rand.nextGaussian() * 0.1);
                    worldIn.addParticle(ParticleTypes.END_ROD, d + g, e + g, f + g, rand.nextGaussian() * 0.005, rand.nextGaussian() * 0.005, rand.nextGaussian() * 0.005);
                }
            }

        }
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return FTBSchools.PROXY.shouldBarrierRender() ? super.getShape(state, worldIn, pos, context) : Shapes.empty();
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return Shapes.block();
    }

    public float getShadeBrightness(BlockState arg, BlockGetter arg2, BlockPos arg3) {
        return 1.0F;
    }
}
