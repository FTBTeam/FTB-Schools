package com.feed_the_beast.mods.ftbschools.kubejs;

import com.feed_the_beast.mods.ftbschools.data.SchoolTypeProperties;
import dev.latvian.kubejs.event.EventJS;
import net.minecraft.resources.ResourceLocation;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class LoadSchoolsEventJS extends EventJS {

    private final BiConsumer<ResourceLocation, SchoolTypeProperties> schools;

    public LoadSchoolsEventJS(BiConsumer<ResourceLocation, SchoolTypeProperties> schools) {
        this.schools = schools;
    }

    public void add(ResourceLocation id, Consumer<SchoolTypeProperties> p) {
        SchoolTypeProperties properties = new SchoolTypeProperties();
        p.accept(properties);
        schools.accept(id, properties);
    }
}
