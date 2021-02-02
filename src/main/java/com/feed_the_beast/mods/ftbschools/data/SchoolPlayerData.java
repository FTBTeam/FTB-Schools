package com.feed_the_beast.mods.ftbschools.data;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;

import javax.annotation.Nullable;
import java.util.UUID;

import static com.feed_the_beast.mods.ftbschools.world.SchoolManager.schoolTypes;

public class SchoolPlayerData extends DataHolder {

    public final UUID uuid;

    private final SchoolServerData root;

    /**
     * A map of ACTIVE schools per type to their IDs in the school manager.
     * ACTIVE means that this is the last school of that type the player was in.
     */
    private final Object2IntMap<SchoolType> activeSchools = new Object2IntOpenHashMap<>();

    /**
     * The player's CURRENT school type. Useful for quests, KubeJS integration, etc.
     */
    @Nullable
    private SchoolType currentSchoolType = null;

    public SchoolPlayerData(SchoolServerData root, UUID uuid) {
        this.root = root;
        this.uuid = uuid;
    }

    @Override
    public void load(CompoundTag tag) {
        CompoundTag schools = tag.getCompound("schools");
        schools.getAllKeys().forEach(key -> {
            SchoolType type = schoolTypes.get(key);
            if (type != null) {
                activeSchools.put(type, schools.getInt(key));
            }
        });

        String current = tag.getString("current_school");
        currentSchoolType = current.isEmpty() ? null : schoolTypes.get(current);
    }

    @Override
    protected CompoundTag save() {
        CompoundTag tag = new CompoundTag();

        CompoundTag schools = new CompoundTag();
        activeSchools.forEach((type, id) -> schools.putInt(type.id, id));
        tag.put("schools", schools);

        if (currentSchoolType != null) {
            tag.putString("current_school", currentSchoolType.id);
        }

        return tag;
    }

    @Nullable
    public SchoolType getSchool() {
        return currentSchoolType;
    }

    public int getSchoolId() {
        return activeSchools.getOrDefault(getSchool(), -1);
    }

    public int setSchool(SchoolType type, boolean update) {
        currentSchoolType = type;
        if (type == null) return -1;
        if (update) {
            int id = root.nextId(type.properties.night);
            activeSchools.put(type, id);
            markDirty();
            return id;
        }
        return getSchoolId();
    }

    public boolean hasStarted(SchoolType type) {
        return activeSchools.containsKey(type);
    }

    public static SchoolPlayerData get(ServerPlayer player) {
        return SchoolServerData.INSTANCE.getPlayerData(player.getUUID());
    }
}
