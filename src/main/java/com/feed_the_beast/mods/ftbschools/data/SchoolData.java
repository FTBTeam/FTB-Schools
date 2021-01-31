package com.feed_the_beast.mods.ftbschools.data;

import com.feed_the_beast.mods.ftbschools.util.Util;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtIo;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.storage.LevelResource;

import java.nio.file.Files;
import java.nio.file.Path;

public class SchoolData {

    public static final LevelResource FTBSCHOOLS_DATA = new LevelResource("ftbschools");

    public static SchoolData INSTANCE;

    public final MinecraftServer server;
    private Path filePath;

    private int nextIdDay = 0;
    private int nextIdNight = 0;

    private boolean dirty = false;

    public SchoolData(MinecraftServer server) {
        this.server = server;
    }

    public void load() {
        filePath = server.getWorldPath(FTBSCHOOLS_DATA).resolve("root.nbt");

        if (Files.exists(filePath)) {
            Util.tryIO(() -> {
                CompoundTag tag = NbtIo.readCompressed(filePath.toFile());
                nextIdDay = tag.getInt("next_id_day");
                nextIdNight = tag.getInt("next_id_night");
            });
        }
    }

    public void setDirty() {
        dirty = true;
    }

    public void save() {
        if (dirty) {
            Util.getOrCreateDir(filePath.getParent());
            CompoundTag tag = new CompoundTag();
            tag.putInt("next_id_day", nextIdDay);
            tag.putInt("next_id_night", nextIdNight);
            Util.tryIO(() -> NbtIo.writeCompressed(tag, filePath.toFile()));
            dirty = false;
        }
    }

    public int nextId(boolean night) {
        int r = night ? nextIdNight++ : nextIdDay++;
        setDirty();
        return r;
    }

}
