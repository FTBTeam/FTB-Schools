package com.feed_the_beast.mods.ftbschools.kubejs;

import com.feed_the_beast.mods.ftbschools.world.SchoolManager;
import com.feed_the_beast.mods.ftbschools.data.SchoolType;
import com.google.common.collect.ImmutableMap;
import dev.latvian.kubejs.event.EventJS;

import java.util.function.Consumer;

public class LoadSchoolsEventJS extends EventJS {

    private final ImmutableMap.Builder<String, SchoolType> schools;

    public LoadSchoolsEventJS(ImmutableMap.Builder<String, SchoolType> schools) {
        this.schools = schools;
    }

    public LoadSchoolsEventJS addSchool(String id) {
        return addSchool(id, (b) -> {});
    }

    public LoadSchoolsEventJS addSchool(String id, Consumer<SchoolType.Builder> props) {
        schools.put(id, SchoolManager.makeSchool(id, props));
        return this;
    }
}
