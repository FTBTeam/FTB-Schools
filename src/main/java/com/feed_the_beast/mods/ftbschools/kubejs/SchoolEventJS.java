package com.feed_the_beast.mods.ftbschools.kubejs;

import com.feed_the_beast.mods.ftbschools.data.SchoolData;
import com.feed_the_beast.mods.ftbschools.data.SchoolType;
import dev.latvian.kubejs.entity.EntityJS;
import dev.latvian.kubejs.player.PlayerEventJS;
import dev.latvian.kubejs.player.ServerPlayerJS;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

public abstract class SchoolEventJS extends PlayerEventJS {

    private final SchoolData school;
    private final ServerPlayer player;

    protected SchoolEventJS(SchoolData school, ServerPlayer player) {
        this.school = school;
        this.player = player;
    }

    public final SchoolData getData() {
        return school;
    }

    public final SchoolType getType() {
        return getData().type;
    }

    public final ResourceLocation getId() {
        return getType().id;
    }

    public final int getIndex() {
        return getData().index;
    }

    @Override
    public EntityJS getEntity() {
        return this.entityOf(player);
    }

    public static class Enter extends SchoolEventJS {
        public Enter(SchoolData school, ServerPlayer player) {
            super(school, player);
        }
    }

    public static class Leave extends SchoolEventJS {
        private final boolean droppedOut;

        public Leave(SchoolData school, ServerPlayer player, boolean droppedOut) {
            super(school, player);
            this.droppedOut = droppedOut;
        }
    }
}
