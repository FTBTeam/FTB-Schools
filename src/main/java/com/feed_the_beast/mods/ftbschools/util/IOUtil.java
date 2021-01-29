package com.feed_the_beast.mods.ftbschools.util;

import com.feed_the_beast.mods.ftbschools.FTBSchools;
import net.minecraftforge.fml.loading.FMLPaths;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class IOUtil {

    public static final Path CONFIG = IOUtil.getOrCreateDir(FMLPaths.CONFIGDIR.get().resolve(FTBSchools.MOD_ID)).normalize();

    public static Path getOrCreateDir(Path dir) {
        if (Files.notExists(dir)) {
            tryIO(() -> Files.createDirectory(dir));
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
}
