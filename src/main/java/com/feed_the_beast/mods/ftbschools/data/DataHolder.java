package com.feed_the_beast.mods.ftbschools.data;

import net.minecraft.nbt.CompoundTag;

public abstract class DataHolder {

    protected boolean dirty = false;

    public final void markDirty() {
        dirty = true;
    }

    protected abstract void load(CompoundTag tag);

    public final CompoundTag saveChanges() {
        CompoundTag tag = new CompoundTag();
        if (dirty) {
            tag = save();
        }
        dirty = false;
        return tag;
    }

    protected abstract CompoundTag save();

}
