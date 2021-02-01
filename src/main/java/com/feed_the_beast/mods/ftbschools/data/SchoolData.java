package com.feed_the_beast.mods.ftbschools.data;

import com.feed_the_beast.mods.ftbschools.util.Util;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtIo;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.storage.LevelResource;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SchoolData extends DataHolder {

    public static final LevelResource FTBSCHOOLS_DATA = new LevelResource("ftbschools");

    public static SchoolData INSTANCE;

    public final MinecraftServer server;
    private Path filePath;

    private int nextId = 0;

    private Map<UUID, SchoolPlayerData> playerData = new HashMap<>();

    public SchoolData(MinecraftServer server) {
        this.server = server;
    }

    public void loadAll() {
        Path worldData = server.getWorldPath(FTBSCHOOLS_DATA);
        filePath = worldData.resolve("root.nbt");

        if (Files.exists(filePath)) {
            Util.tryIO(() -> load(NbtIo.readCompressed(filePath.toFile())));
        }

        Path playerPath = worldData.resolve("players/");
        if (Files.exists(playerPath)) {
            Util.tryIO(() -> Files.list(playerPath).forEach(path -> Util.tryIO(() -> {
                CompoundTag tag = NbtIo.readCompressed(path.toFile());
                if (tag.hasUUID("uuid")) {
                    UUID uuid = tag.getUUID("uuid");
                    SchoolPlayerData data = new SchoolPlayerData(this, uuid);
                    playerData.put(uuid, data);
                    data.load(tag);
                }
            })));
        }
    }

    @Override
    protected void load(CompoundTag tag) {
        nextId = tag.getInt("next_id");
    }

    public void saveAll() {
        CompoundTag rootTag = saveChanges();
        if (!rootTag.isEmpty()) {
            Util.getOrCreateDir(filePath.getParent());
            Util.tryIO(() -> NbtIo.writeCompressed(rootTag, filePath.toFile()));
        }

        Path playerPath = filePath.getParent().resolve("players/");
        playerData.forEach((uuid, data) -> {
            CompoundTag playerTag = data.saveChanges();
            if (!playerTag.isEmpty()) {
                Util.getOrCreateDir(playerPath);
                Util.tryIO(() -> NbtIo.writeCompressed(playerTag, playerPath.resolve(uuid + ".nbt").toFile()));
            }
        });
    }

    @Override
    protected CompoundTag save() {
        CompoundTag tag = new CompoundTag();
        tag.putInt("next_id", nextId);
        return tag;
    }

    public int nextId() {
        markDirty();
        return nextId++;
    }

    public SchoolPlayerData getPlayerData(UUID uuid) {
        return playerData.computeIfAbsent(uuid, id -> new SchoolPlayerData(this, id));
    }

}
