package net.threetag.threecore.util.threedata;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.JSONUtils;
import net.minecraftforge.client.event.RenderGameOverlayEvent;

public class HotbarElementThreeData extends ThreeData<RenderGameOverlayEvent.ElementType> {

    public HotbarElementThreeData(String key) {
        super(key);
    }

    @Override
    public RenderGameOverlayEvent.ElementType parseValue(JsonObject jsonObject, RenderGameOverlayEvent.ElementType defaultValue) {
        if (!JSONUtils.hasField(jsonObject, this.jsonKey))
            return defaultValue;
        RenderGameOverlayEvent.ElementType type = getType(JSONUtils.getString(jsonObject, this.jsonKey));
        return type != null ? type : defaultValue;
    }

    @Override
    public void writeToNBT(CompoundNBT nbt, RenderGameOverlayEvent.ElementType value) {
        nbt.putString(this.key, value.name());
    }

    @Override
    public RenderGameOverlayEvent.ElementType readFromNBT(CompoundNBT nbt, RenderGameOverlayEvent.ElementType defaultValue) {
        if (!nbt.contains(this.key))
            return defaultValue;
        RenderGameOverlayEvent.ElementType type = getType(nbt.getString(this.key));
        return type != null ? type : defaultValue;
    }

    @Override
    public JsonElement serializeJson(RenderGameOverlayEvent.ElementType value) {
        return new JsonPrimitive(value.name().toLowerCase());
    }

    public static RenderGameOverlayEvent.ElementType getType(String name) {
        for (RenderGameOverlayEvent.ElementType type : RenderGameOverlayEvent.ElementType.values()) {
            if (type.name().equalsIgnoreCase(name)) {
                return type;
            }
        }
        return null;
    }

    public static String getElementList() {
        StringBuilder s = new StringBuilder();

        for (RenderGameOverlayEvent.ElementType type : RenderGameOverlayEvent.ElementType.values()) {
            s.append(", \"").append(type.name().toLowerCase()).append("\"");
        }

        return s.substring(2);
    }

}
