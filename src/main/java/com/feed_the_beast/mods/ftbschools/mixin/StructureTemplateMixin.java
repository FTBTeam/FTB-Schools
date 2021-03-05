package com.feed_the_beast.mods.ftbschools.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Iterator;
import java.util.List;
import java.util.Random;

@Mixin(StructureTemplate.class)
public class StructureTemplateMixin {

    @Inject(
            method = "fillFromWorld",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/nbt/CompoundTag;remove(Ljava/lang/String;)V",
                    ordinal = 0
            ),
            locals = LocalCapture.CAPTURE_FAILHARD
    )
    // save nbt
    public void savePos(Level p0, BlockPos p1, BlockPos p2, boolean p3, Block p4, CallbackInfo ci,
                        BlockPos l0, List l1, List l2, List l3, BlockPos l4, BlockPos l5, Iterator l6,
                        BlockPos l7, BlockPos l8, BlockState l9, BlockEntity l10,
                        CompoundTag nbt) {
        CompoundTag schoolsSaved = new CompoundTag();
        schoolsSaved.putInt("x", nbt.getInt("x"));
        schoolsSaved.putInt("y", nbt.getInt("y"));
        schoolsSaved.putInt("z", nbt.getInt("z"));
        nbt.put("ftbschools.saved", schoolsSaved);
    }

    @Inject(
            method = "placeInWorld(Lnet/minecraft/world/level/ServerLevelAccessor;Lnet/minecraft/core/BlockPos;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructurePlaceSettings;Ljava/util/Random;I)Z",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/nbt/CompoundTag;putInt(Ljava/lang/String;I)V",
                    ordinal = 0
            ),
            locals = LocalCapture.CAPTURE_FAILHARD
    )
    public void clean(ServerLevelAccessor p0, BlockPos p1, BlockPos p2, StructurePlaceSettings p3, Random p4, int p5, CallbackInfoReturnable<Boolean> cir,
                      List l0, BoundingBox l1, List l2, List l3, int l4, int l5, int l6, int l7, int l8, int l9, Iterator l10,
                      StructureTemplate.StructureBlockInfo info) {
        info.nbt.remove("ftbschools.saved");
    }
}
