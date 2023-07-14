package dev.ftb.mods.ftbschools.data;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;

import java.util.HashSet;
import java.util.Set;

public class CommandBlacklist {
    private final Set<String> disabledCommands = new HashSet<>();

    public void addDisabledCommand(String command) {
        // both "cmd" or "/cmd" forms are accepted
        if (command.startsWith("/") && command.length() > 1) {
            command = command.substring(1);
        }
        disabledCommands.add(command);
    }

    public boolean isCommandDisabled(String command) {
        return disabledCommands.contains(command);
    }

    public void writeNBT(CompoundTag tag) {
        if (!disabledCommands.isEmpty()) {
            ListTag d = new ListTag();
            disabledCommands.forEach(c -> d.add(StringTag.valueOf(c)));
            tag.put("DisabledCommands", d);
        }
    }

    public void readNBT(CompoundTag tag) {
        if (tag.contains("DisabledCommands", Tag.TAG_LIST)) {
            tag.getList("DisabledCommands", Tag.TAG_STRING)
                    .forEach(el -> addDisabledCommand(el.getAsString()));
        }
    }
}
