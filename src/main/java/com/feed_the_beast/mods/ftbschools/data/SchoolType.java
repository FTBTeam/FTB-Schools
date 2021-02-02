package com.feed_the_beast.mods.ftbschools.data;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;

public class SchoolType {
    public final SchoolManager manager;
    public final ResourceLocation id;
    public final SchoolTypeProperties properties;

    public SchoolType(SchoolManager m, ResourceLocation id, SchoolTypeProperties properties) {
        this.manager = m;
        this.id = id;
        this.properties = properties;
    }

    public ServerLevel getDimension() {
        return properties.night ? manager.nightDim : manager.dayDim;
    }
}
