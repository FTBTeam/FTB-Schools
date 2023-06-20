package dev.ftb.mods.ftbschools.data;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;

public class SchoolType {
    public final SchoolManager manager;
    public final ResourceLocation id;
    public final SchoolTypeProperties properties;

    public SchoolType(SchoolManager m, ResourceLocation id, SchoolTypeProperties properties) {
        this.manager = m;
        this.id = id;
        this.properties = properties;
    }

    public static SchoolType fromNetwork(FriendlyByteBuf buf) {
        return new SchoolType(null, buf.readResourceLocation(), SchoolTypeProperties.fromNetwork(buf));
    }

    public void toNetwork(FriendlyByteBuf buf) {
        buf.writeResourceLocation(id);
        properties.toNetwork(buf);
    }

    public JsonElement toJson() {
        JsonObject o = new JsonObject();
        o.addProperty("night", properties.night);
        return o;
    }

    public ServerLevel getDimension() {
        if (manager == null) {
            throw new IllegalStateException("null manager (trying to call client-side?)");
        }
        return properties.night ? manager.nightDim : manager.dayDim;
    }
}
