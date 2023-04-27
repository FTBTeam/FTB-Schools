package dev.ftb.mods.ftbschools.register;

import dev.ftb.mods.ftbschools.FTBSchools;
import dev.ftb.mods.ftbschools.block.SchoolBarrierBlock;
import dev.ftb.mods.ftbschools.block.SpawnMarkerBlock;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;

public class FTBSchoolsBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, FTBSchools.MOD_ID);

    public static final RegistryObject<SpawnMarkerBlock> SPAWN_MARKER = simpleBlock("spawn_marker", SpawnMarkerBlock::new);
    public static final RegistryObject<SchoolBarrierBlock> BARRIER = simpleBlock("barrier", SchoolBarrierBlock::new);

    private static <T extends Block> RegistryObject<T> register(String name, Supplier<? extends T> block, Function<RegistryObject<T>, Supplier<? extends Item>> item) {
        RegistryObject<T> register = BLOCKS.register(name, block);
        FTBSchoolsItems.ITEMS.register(name, item.apply(register));
        return register;
    }

    private static <B extends Block> RegistryObject<B> simpleBlock(String name, Supplier<? extends B> block) {
        return register(name, block, FTBSchoolsBlocks::blockItem);
    }

    private static <T extends Block> Supplier<BlockItem> blockItem(final RegistryObject<T> block) {
        return () -> new BlockItem(Objects.requireNonNull(block.get()), new Item.Properties().tab(CreativeModeTab.TAB_MISC));
    }
}
