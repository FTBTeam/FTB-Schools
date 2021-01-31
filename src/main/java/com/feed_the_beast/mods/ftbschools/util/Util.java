package com.feed_the_beast.mods.ftbschools.util;

import com.feed_the_beast.mods.ftbschools.FTBSchools;
import net.minecraft.core.BlockPos;
import net.minecraftforge.fml.loading.FMLPaths;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Util {

    private static final int SCHOOLS_PER_LINE = 256; // 65536 should be enough right?

    public static final Path CONFIG = Util.getOrCreateDir(FMLPaths.CONFIGDIR.get().resolve(FTBSchools.MOD_ID)).normalize();

    public static Path getOrCreateDir(Path dir) {
        if (Files.notExists(dir)) {
            tryIO(() -> Files.createDirectories(dir));
        }
        return dir.normalize();
    }

    @FunctionalInterface
    public interface TryIO {
        void run() throws IOException;
    }

    public static void tryIO(TryIO tryIO) {
        try {
            tryIO.run();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    public static <X> X cast(Object o) {
        try {
            return (X) o;
        } catch (ClassCastException e) {
            FTBSchools.LOGGER.fatal("Class cast using Util.cast FAILED! ", e);
            throw e;
        }
    }

    public static BlockPos getCenterOfRegion(int id) {
        return new BlockPos((id % SCHOOLS_PER_LINE) << 9, 69, (id / SCHOOLS_PER_LINE) << 9);
    }
}
