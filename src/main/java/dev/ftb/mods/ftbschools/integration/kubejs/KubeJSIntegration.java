package dev.ftb.mods.ftbschools.integration.kubejs;

import dev.latvian.mods.kubejs.KubeJSPlugin;

public class KubeJSIntegration extends KubeJSPlugin {
    @Override
    public void registerEvents() {
        FTBSchoolsEvents.EVENT_GROUP.register();
    }
}
