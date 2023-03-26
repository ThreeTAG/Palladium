package net.threetag.palladium.util.property;

import com.google.common.reflect.TypeToken;
import com.google.gson.JsonElement;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

import java.lang.reflect.Type;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

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
        EntityPropertyHandler.getHandler(entity).ifPresent(handler -> handler.set(this, value));
    }

    @SuppressWarnings("unchecked")
    public T get(Entity entity) {
        AtomicReference<T> result = new AtomicReference<>();

        if(this instanceof BooleanProperty) {
            result.set((T) Boolean.valueOf(false));
        } else if(this instanceof IntegerProperty) {
            result.set((T) Integer.valueOf(0));
        } else if(this instanceof FloatProperty) {
            result.set((T) Float.valueOf(0F));
        } else if(this instanceof DoubleProperty) {
            result.set((T) Double.valueOf(0D));
        }

        EntityPropertyHandler.getHandler(entity).ifPresent(handler -> result.set(handler.get(this)));
        return result.get();
    }

    public boolean isRegistered(Entity entity) {
        AtomicBoolean result = new AtomicBoolean(false);
        EntityPropertyHandler.getHandler(entity).ifPresent(handler -> result.set(handler.isRegistered(this)));
        return result.get();
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
