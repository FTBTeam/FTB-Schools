package com.feed_the_beast.mods.ftbschools;

import com.feed_the_beast.ftbquests.client.ClientQuestFile;
import com.feed_the_beast.mods.ftbschools.block.FTBSchoolsBlocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.level.GameType;

public abstract class FTBSchoolsProxy {

    public abstract boolean shouldBarrierRender();

    public static class Client extends FTBSchoolsProxy {
        @Override
        public boolean shouldBarrierRender() {
            LocalPlayer player = Minecraft.getInstance().player;
            return Minecraft.getInstance().gameMode.getPlayerMode() == GameType.CREATIVE
                    && player.isHolding(FTBSchoolsBlocks.BARRIER.get().asItem())
                    && ClientQuestFile.INSTANCE.canEdit();
        }
    }

    public static class Common extends FTBSchoolsProxy {
        @Override
        public boolean shouldBarrierRender() {
            return false;
        }
    }
}
