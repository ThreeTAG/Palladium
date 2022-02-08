package net.threetag.palladium.util.property;

import com.google.gson.JsonElement;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;

public abstract class PalladiumProperty<T> {

    private final String key;
    private String description;

    public PalladiumProperty(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public String getDescription() {
        return description;
    }

    public PalladiumProperty<T> configurable(String description) {
        this.description = description;
        return this;
    }

    public abstract T fromJSON(JsonElement jsonElement);

    public abstract JsonElement toJSON(T value);

    public abstract T fromNBT(Tag tag, T defaultValue);

    public abstract Tag toNBT(T value);

    public abstract T fromBuffer(FriendlyByteBuf buf);

    public abstract void toBuffer(FriendlyByteBuf buf, Object value);
}
