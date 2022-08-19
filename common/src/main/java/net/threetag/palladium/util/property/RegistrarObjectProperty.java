package net.threetag.palladium.util.property;

import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import dev.architectury.registry.registries.Registrar;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

import java.util.Objects;

public class RegistrarObjectProperty<T> extends PalladiumProperty<T> {

    private final Registrar<T> registry;

    public RegistrarObjectProperty(String key, Registrar<T> registry) {
        super(key);
        this.registry = registry;
    }

    @Override
    public T fromJSON(JsonElement jsonElement) {
        ResourceLocation id = new ResourceLocation(jsonElement.getAsString());

        if (this.registry.contains(id)) {
            return this.registry.get(id);
        } else {
            throw new JsonParseException("Unknown " + this.registry.key().toString() + " '" + id + "'");
        }
    }

    @Override
    public JsonElement toJSON(T value) {
        return new JsonPrimitive(Objects.requireNonNull(this.registry.getKey(value)).toString());
    }

    @Override
    public T fromNBT(Tag tag, T defaultValue) {
        if (tag instanceof StringTag stringTag) {
            ResourceLocation id = new ResourceLocation(stringTag.getAsString());

            if (this.registry.contains(id)) {
                return this.registry.get(id);
            }
        }
        return defaultValue;
    }

    @Override
    public Tag toNBT(T value) {
        return StringTag.valueOf(Objects.requireNonNull(this.registry.getKey(value)).toString());
    }

    @Override
    public T fromBuffer(FriendlyByteBuf buf) {
        return this.registry.get(buf.readResourceLocation());
    }

    @SuppressWarnings("unchecked")
    @Override
    public void toBuffer(FriendlyByteBuf buf, Object value) {
        buf.writeResourceLocation(Objects.requireNonNull(this.registry.getId((T) value)));
    }

    @Override
    public String getString(T value) {
        return value != null ? Objects.requireNonNull(this.registry.getKey(value)).toString() : null;
    }
}
