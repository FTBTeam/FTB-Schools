package dev.ftb.mods.ftbschools.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Iterator;
import java.util.List;

@Mixin(StructureTemplate.class)
public class StructureTemplateMixin {
//    @Inject(
//            method = "fillFromWorld",
//            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureTemplate;addToLists(Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureTemplate$StructureBlockInfo;Ljava/util/List;Ljava/util/List;Ljava/util/List;)V", ordinal = 0, shift = At.Shift.AFTER),
//            locals = LocalCapture.CAPTURE_FAILHARD
//    )
//    public void savePos(Level level, BlockPos pos, Vec3i size, boolean withEntities, Block blockToIgnore, CallbackInfo ci,
//                        BlockPos l0, List l1, List l2, List l3, BlockPos l4, BlockPos l5, Iterator l6,
//                        BlockPos l7, BlockPos l8, BlockState l9, BlockEntity l10, StructureTemplate.StructureBlockInfo l11) {
//        if (l10 != null && !l2.isEmpty()) {
//            // Save a copy of the block entity's position (vanilla doesn't do this)
//            // Block info for the block entity will be the object just appended to blocksWithNBT
//            // (this data is needed by the NBTFixerProcessor)
//            CompoundTag schoolsSaved = new CompoundTag();
//            schoolsSaved.putInt("x", l10.getBlockPos().getX());
//            schoolsSaved.putInt("y", l10.getBlockPos().getY());
//            schoolsSaved.putInt("z", l10.getBlockPos().getZ());
//            @SuppressWarnings("unchecked") List<StructureTemplate.StructureBlockInfo> blocksWithNBT = l2;
//            blocksWithNBT.get(blocksWithNBT.size() - 1).nbt.put("ftbschools.saved", schoolsSaved);
//        }
//    }

    @Inject(
            method = "placeInWorld(Lnet/minecraft/world/level/ServerLevelAccessor;Lnet/minecraft/core/BlockPos;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructurePlaceSettings;Lnet/minecraft/util/RandomSource;I)Z",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/block/entity/BlockEntity;load(Lnet/minecraft/nbt/CompoundTag;)V",
                    ordinal = 0
            ),
            locals = LocalCapture.CAPTURE_FAILHARD
    )
    public void clean(ServerLevelAccessor p0, BlockPos p1, BlockPos p2, StructurePlaceSettings p3, RandomSource p4, int p5,
                      CallbackInfoReturnable<Boolean> cir,
                      List l0, BoundingBox l1, List l2, List l3, List l3b, int l4, int l5, int l6, int l7, int l8, int l9, Iterator l10,
                      StructureTemplate.StructureBlockInfo info) {
        // clean up any saved block entity position data added by the above mixin
        info.nbt.remove("ftbschools.saved");
    }
}
