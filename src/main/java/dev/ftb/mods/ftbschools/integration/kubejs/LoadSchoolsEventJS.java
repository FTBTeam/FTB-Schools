package dev.ftb.mods.ftbschools.integration.kubejs;

import dev.ftb.mods.ftbschools.data.SchoolManager;
import dev.ftb.mods.ftbschools.data.SchoolTypeProperties;
import dev.latvian.mods.kubejs.event.EventJS;
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

    public void disableCommand(String command) {
        SchoolManager.INSTANCE.commandBlacklist.addDisabledCommand(command);
    }
}
