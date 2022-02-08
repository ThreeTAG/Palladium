package net.threetag.palladium.power.ability.condition;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import dev.architectury.core.RegistryEntry;
import dev.architectury.registry.registries.Registrar;
import dev.architectury.registry.registries.Registries;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.util.property.PalladiumProperty;

import java.util.HashMap;
import java.util.Map;

public abstract class ConditionSerializer extends RegistryEntry<ConditionSerializer> {

    public static final ResourceKey<Registry<ConditionSerializer>> RESOURCE_KEY = ResourceKey.createRegistryKey(new ResourceLocation(Palladium.MOD_ID, "condition_serializers"));
    public static final Registrar<ConditionSerializer> REGISTRY = Registries.get(Palladium.MOD_ID).builder(RESOURCE_KEY.location(), new ConditionSerializer[0]).build();

    final Map<PalladiumProperty<?>, Object> defaultProperties = new HashMap<>();

    public <T> ConditionSerializer withProperty(PalladiumProperty<T> data, T value) {
        this.defaultProperties.put(data, value);
        return this;
    }

    @SuppressWarnings("unchecked")
    public <T> T getProperty(JsonObject json, PalladiumProperty<T> data) {
        if (this.defaultProperties.containsKey(data)) {
            return json.has(data.getKey()) ? data.fromJSON(json.get(data.getKey())) : (T) this.defaultProperties.get(data);
        } else {
            throw new RuntimeException("Condition Serializer does not have " + data.getKey() + " data!");
        }
    }

    public abstract Condition make(JsonObject json);

    public static Condition fromJSON(JsonObject json) {
        ConditionSerializer conditionSerializer = ConditionSerializer.REGISTRY.get(new ResourceLocation(GsonHelper.getAsString(json, "type")));

        if (conditionSerializer == null) {
            throw new JsonParseException("Condition Serializer '" + GsonHelper.getAsString(json, "type") + "' does not exist!");
        }

        return conditionSerializer.make(json);
    }
}
