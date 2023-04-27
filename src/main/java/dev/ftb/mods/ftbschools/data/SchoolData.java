package dev.ftb.mods.ftbschools.data;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;

import java.util.UUID;

/**
 * @author LatvianModder
 */
public class SchoolData {
    public static final int SCHOOLS_PER_LINE = 1024;

    public final SchoolManager manager;
    public final int index;
    public UUID owner;
    public SchoolType type;
    public CompoundTag playerData;

    public SchoolData(SchoolManager sd, int i) {
        manager = sd;
        index = i;
    }

    public SchoolData(SchoolManager sd, int i, CompoundTag tag) {
        this(sd, i);
        owner = tag.getUUID("Owner");
        type = manager.schoolTypes.get(new ResourceLocation(tag.getString("Type")));
        playerData = tag.contains("PlayerData") ? tag.getCompound("PlayerData") : null;
    }

    public CompoundTag write() {
        CompoundTag tag = new CompoundTag();
        tag.putUUID("Owner", owner);
        tag.putString("Type", type == null ? "" : type.id.toString());

        if (playerData != null) {
            tag.put("PlayerData", playerData);
        }

        return tag;
    }

    public int getRegionX() {
        return (index % 1024);
    }

    public int getRegionZ() {
        return (index / 1024);
    }

    public boolean hasPlayer() {
        return playerData != null;
    }

    public BlockPos getLocation() {
        return new BlockPos(((index % SCHOOLS_PER_LINE) << 9) + 256, 69, ((index / SCHOOLS_PER_LINE) << 9) + 256);
    }
}
