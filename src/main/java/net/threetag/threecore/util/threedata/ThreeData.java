package net.threetag.threecore.util.threedata;

import com.google.common.reflect.TypeToken;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.nbt.CompoundNBT;

import java.lang.reflect.Type;

public abstract class ThreeData<T> {

    protected final String key;
    protected String jsonKey;
    protected String description;
    protected EnumSync syncType = EnumSync.EVERYONE;
    protected boolean write = true;
    private final TypeToken<T> typeToken = new TypeToken<T>(getClass()) {
    };
    private final Type type = typeToken.getType();

    public ThreeData(String key) {
        this.key = key;
    }

    public Type getType() {
        return type;
    }

    public String getKey() {
        return key;
    }

    public String getJsonKey() {
        return jsonKey;
    }

    public String getDescription() {
        return description;
    }

    public boolean isUserSetting() {
        return this.jsonKey != null && !this.jsonKey.isEmpty();
    }

    public ThreeData<T> enableSetting(String jsonKey, String desc) {
        this.jsonKey = jsonKey;
        this.description = desc;
        return this;
    }

    public ThreeData<T> enableSetting(String desc) {
        return this.enableSetting(this.key, desc);
    }

    public ThreeData<T> setSyncType(EnumSync syncType) {
        this.syncType = syncType;
        return this;
    }

    public ThreeData<T> disableSaving() {
        this.write = false;
        return this;
    }

    public boolean canBeSaved() {
        return this.write;
    }

    public abstract T parseValue(JsonObject jsonObject, T defaultValue);

    public abstract void writeToNBT(CompoundNBT nbt, T value);

    public abstract T readFromNBT(CompoundNBT nbt, T defaultValue);

    public abstract JsonElement serializeJson(T value);

    public final String getJsonString(T value) {
        JsonElement element = serializeJson(value);

        if (element.isJsonPrimitive()) {
            return serializeJson(value).toString().replaceAll("\\\\\"", "\"");
        } else if (element.isJsonObject()) {
            String s = serializeJson(value).toString().replaceAll("\\\\\"", "\"");
            return "{" + s.substring(1, s.length() - 1) + "}";
        }
        return serializeJson(value).toString().replaceAll("\\\\\"", "\"");
    }

    public EnumSync getSyncType() {
        return syncType;
    }
}
