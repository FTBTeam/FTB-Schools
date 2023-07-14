package dev.ftb.mods.ftbschools.kubejs;

import dev.ftb.mods.ftbschools.data.SchoolData;
import dev.ftb.mods.ftbschools.data.SchoolType;
import dev.latvian.mods.kubejs.player.PlayerEventJS;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

public abstract class SchoolEventJS extends PlayerEventJS {
    public final SchoolData school;
    private final ServerPlayer _player;

    protected SchoolEventJS(SchoolData school, ServerPlayer player) {
        this.school = school;
        this._player = player;
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
    public Player getEntity() {
        return _player;
    }

    public static class Enter extends SchoolEventJS {
        public Enter(SchoolData school, ServerPlayer player) {
            super(school, player);
        }

        public void disableCommand(String command) {
            school.commandBlacklist.addDisabledCommand(command);
        }
    }

    public static class Leave extends SchoolEventJS {
        public final boolean droppedOut;

        public Leave(SchoolData school, ServerPlayer player, boolean droppedOut) {
            super(school, player);
            this.droppedOut = droppedOut;
        }
    }
}
