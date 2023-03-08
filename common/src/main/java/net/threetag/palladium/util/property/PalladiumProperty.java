package net.threetag.palladium.util.property;

import com.google.common.reflect.TypeToken;
import com.google.gson.JsonElement;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

import java.lang.reflect.Type;

@SuppressWarnings("UnstableApiUsage")
public abstract class PalladiumProperty<T> {

    private final String key;
    private String description;
    private SyncType syncType = SyncType.EVERYONE;
    public final TypeToken<T> typeToken = new TypeToken<T>(getClass()) {
    };
    private final Type type = typeToken.getType();
    private boolean persistent = true;

    public PalladiumProperty(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public String getDescription() {
        return description;
    }

    public SyncType getSyncType() {
        return syncType;
    }

    public Type getType() {
        return type;
    }

    public PalladiumProperty<T> disablePersistence() {
        this.persistent = false;
        return this;
    }

    public boolean isPersistent() {
        return this.persistent;
    }

    public PalladiumProperty<T> configurable(String description) {
        this.description = description;
        return this;
    }

    public PalladiumProperty<T> sync(SyncType syncType) {
        this.syncType = syncType;
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

    public boolean isRegistered(Entity entity) {
        return EntityPropertyHandler.getHandler(entity).isRegistered(this);
    }

    public String getString(T value) {
        return value == null ? null : value.toString();
    }

    public static Object fixValues(PalladiumProperty<?> property, Object value) {
        if (property instanceof IntegerProperty && value instanceof Number number) {
            value = number.intValue();
        } else if (property instanceof FloatProperty && value instanceof Number number) {
            value = number.floatValue();
        } else if (property instanceof DoubleProperty && value instanceof Number number) {
            value = number.doubleValue();
        } else if (property instanceof ResourceLocationProperty && value instanceof String string) {
            value = new ResourceLocation(string);
        }

        return value;
    }
}
