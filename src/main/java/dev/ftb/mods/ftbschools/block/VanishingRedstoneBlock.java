package dev.ftb.mods.ftbschools.block;

import net.minecraft.world.level.block.PoweredBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;

public class VanishingRedstoneBlock extends PoweredBlock {
    public VanishingRedstoneBlock() {
        super(BlockBehaviour.Properties.of(Material.METAL, MaterialColor.FIRE)
                .strength(5.0F, 6.0F)
                .sound(SoundType.METAL)
                .isRedstoneConductor((state, blockGetter, pos) -> false)
        );
    }
}
