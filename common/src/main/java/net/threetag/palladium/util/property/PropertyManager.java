package net.threetag.palladium.util.property;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.GsonHelper;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

public class PropertyManager {

    final Map<PalladiumProperty<?>, Object> defaultProperties = new LinkedHashMap<>();
    final Map<PalladiumProperty<?>, Object> values = new LinkedHashMap<>();
    Listener listener;

    public PropertyManager setListener(Listener listener) {
        this.listener = listener;
        return this;
    }

    public <T> PropertyManager register(PalladiumProperty<T> property, T value) {
        this.defaultProperties.put(property, value);
        this.values.put(property, value);
        return this;
    }

    public boolean isRegistered(PalladiumProperty<?> property) {
        return this.defaultProperties.containsKey(property);
    }

    @SuppressWarnings("unchecked")
    public <T> PropertyManager set(PalladiumProperty<T> property, @Nullable T value) {
        if (!this.defaultProperties.containsKey(property)) {
            throw new RuntimeException("Property " + property.getKey() + " was not registered!");
        }
        T oldValue = (T) this.values.get(property);
        this.values.put(property, value);
        if (this.listener != null) {
            this.listener.onChanged(property, oldValue, value);
        }
        return this;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public PropertyManager setRaw(PalladiumProperty property, @Nullable Object value) {
        if (!this.defaultProperties.containsKey(property)) {
            throw new RuntimeException("Property " + property.getKey() + " was not registered!");
        }
        Object oldValue = this.values.get(property);
        this.values.put(property, value);
        if (this.listener != null) {
            this.listener.onChanged(property, oldValue, value);
        }
        return this;
    }

    @Nullable
    @SuppressWarnings("unchecked")
    public <T> T get(PalladiumProperty<T> property) {
        return (T) this.values.get(property);
    }

    @Nullable
    @SuppressWarnings("unchecked")
    public <T> T getDefault(PalladiumProperty<T> property) {
        return (T) this.defaultProperties.get(property);
    }

    public <T> Optional<T> optional(PalladiumProperty<T> property) {
        return Optional.ofNullable(this.get(property));
    }

    @SuppressWarnings("unchecked")
    public <T> T reset(PalladiumProperty<T> property) {
        T oldValue = (T) this.values.get(property);
        T defVal = (T) this.defaultProperties.get(property);
        this.values.put(property, defVal);
        if (this.listener != null) {
            this.listener.onChanged(property, oldValue, defVal);
        }
        return (T) this.defaultProperties.get(property);
    }

    public PalladiumProperty<?> getPropertyByName(String name) {
        for (PalladiumProperty<?> property : this.values.keySet()) {
            if (property.getKey().equals(name)) {
                return property;
            }
        }
        return null;
    }

    public Map<PalladiumProperty<?>, Object> values() {
        return this.values;
    }

    public PropertyManager copy() {
        PropertyManager propertyManager = new PropertyManager();
        propertyManager.defaultProperties.putAll(this.defaultProperties);
        propertyManager.values.putAll(this.values);
        return propertyManager;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public void fromNBT(CompoundTag nbt) {
        this.values.clear();
        for (PalladiumProperty property : this.defaultProperties.keySet()) {
            if (nbt.contains(property.getKey())) {
                Tag tag = nbt.get(property.getKey());

                if (tag instanceof StringTag stringTag && stringTag.getAsString().equals("null")) {
                    this.values.put(property, null);
                } else {
                    this.values.put(property, property.fromNBT(nbt.get(property.getKey()), this.defaultProperties.get(property)));
                }
            } else {
                this.values.put(property, property.fromNBT(nbt.get(property.getKey()), this.defaultProperties.get(property)));
            }
        }
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public CompoundTag toNBT() {
        CompoundTag nbt = new CompoundTag();
        for (PalladiumProperty property : this.values.keySet()) {
            if (this.values.get(property) == null) {
                nbt.put(property.getKey(), StringTag.valueOf("null"));
            } else {
                nbt.put(property.getKey(), property.toNBT(this.values.get(property)));
            }
        }
        return nbt;
    }

    public void toBuffer(FriendlyByteBuf buf) {
        buf.writeInt(this.values.size());
        this.values.forEach((property, value) -> {
            buf.writeUtf(property.getKey());
            buf.writeBoolean(value != null);
            if (value != null) {
                property.toBuffer(buf, value);
            }
        });
    }

    public void fromBuffer(FriendlyByteBuf buf) {
        int amount = buf.readInt();

        for (int i = 0; i < amount; i++) {
            PalladiumProperty<?> property = getPropertyByName(buf.readUtf());
            if (buf.readBoolean() && property != null) {
                this.values.put(property, property.fromBuffer(buf));
            }
        }
    }

    public void fromJSON(JsonObject json) {
        for (PalladiumProperty<?> property : this.defaultProperties.keySet()) {
            if (GsonHelper.isValidNode(json, property.getKey())) {
                JsonElement jsonElement = json.get(property.getKey());
                if (jsonElement.isJsonPrimitive() && jsonElement.getAsString().equals("null")) {
                    this.values.put(property, null);
                } else {
                    this.values.put(property, property.fromJSON(json.get(property.getKey())));
                }
            }
        }
    }

    public interface Listener {

        <T> void onChanged(PalladiumProperty<T> property, T oldValue, T newValue);

    }

}
