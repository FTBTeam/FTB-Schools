package dev.ftb.mods.ftbschools.kubejs;

import dev.latvian.mods.kubejs.event.EventGroup;
import dev.latvian.mods.kubejs.event.EventHandler;
import dev.latvian.mods.kubejs.event.Extra;

public class FTBSchoolsEvents {
    public static final EventGroup EVENT_GROUP = EventGroup.of("FTBSchools");

    public static final EventHandler LOAD_SCHOOLS = EVENT_GROUP.server("loadSchools", () -> LoadSchoolsEventJS.class);
    public static final EventHandler ENTER_SCHOOL = EVENT_GROUP.server("enterSchool", () -> SchoolEventJS.Enter.class).extra(Extra.STRING);
    public static final EventHandler LEAVE_SCHOOL = EVENT_GROUP.server("leaveSchool", () -> SchoolEventJS.Leave.class).extra(Extra.STRING);
    public static final EventHandler REPLACE_BLOCK = EVENT_GROUP.server("replaceBlock", () -> ReplaceBlockEventJS.class);
}
