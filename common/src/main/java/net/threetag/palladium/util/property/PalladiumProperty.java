package net.threetag.palladium.util.property;

import com.google.common.reflect.TypeToken;
import com.google.gson.JsonElement;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;

import java.lang.reflect.Type;

@SuppressWarnings("UnstableApiUsage")
public abstract class PalladiumProperty<T> {

    private final String key;
    private String description;
    private final TypeToken<T> typeToken = new TypeToken<T>(getClass()) {
    };
    private final Type type = typeToken.getType();

    public PalladiumProperty(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public String getDescription() {
        return description;
    }

    public Type getType() {
        return type;
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

    public void set(Entity entity, T value) {
        EntityPropertyHandler.getHandler(entity).set(this, value);
    }

    public T get(Entity entity) {
        return EntityPropertyHandler.getHandler(entity).get(this);
    }
}
