package dev.ftb.mods.ftbschools.data;

import net.minecraft.network.FriendlyByteBuf;

/**
 * @author LatvianModder
 */
public class SchoolTypeProperties {
    public boolean night = false;

    public SchoolTypeProperties() {
    }

    public SchoolTypeProperties(boolean night) {
        this.night = night;
    }

    public static SchoolTypeProperties fromNetwork(FriendlyByteBuf buf) {
        return new SchoolTypeProperties(buf.readBoolean());
    }

    public void toNetwork(FriendlyByteBuf buf) {
        buf.writeBoolean(night);
    }

    // TODO: Block Interaction callbacks
}
