package com.feed_the_beast.mods.ftbschools;

import net.minecraft.client.Minecraft;
import net.minecraft.world.level.GameType;

public abstract class FTBSchoolsProxy {

    public abstract boolean shouldBarrierRender();

    public static class Client extends FTBSchoolsProxy {
        @Override
        public boolean shouldBarrierRender() {
            return Minecraft.getInstance().gameMode.getPlayerMode() == GameType.CREATIVE;
        }
    }

    public static class Common extends FTBSchoolsProxy {
        @Override
        public boolean shouldBarrierRender() {
            return false;
        }
    }
}
