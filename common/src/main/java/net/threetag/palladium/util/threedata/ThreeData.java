package net.threetag.palladium.util.threedata;

import com.google.gson.JsonElement;
import net.minecraft.nbt.Tag;

public abstract class ThreeData<T> {

    private final String key;
    private String description;

    public ThreeData(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public String getDescription() {
        return description;
    }

    public ThreeData<T> configurable(String description) {
        this.description = description;
        return this;
    }

    public abstract T fromJSON(JsonElement jsonElement);

    public abstract JsonElement toJSON(T value);

    public abstract T fromNBT(Tag tag, T defaultValue);

    public abstract Tag toNBT(T value);
}
