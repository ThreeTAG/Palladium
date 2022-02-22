package net.threetag.palladium.power.ability.condition;

import com.google.gson.JsonElement;
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
import net.threetag.palladium.documentation.DocumentationBuilder;
import net.threetag.palladium.documentation.IDefaultDocumentedConfigurable;
import net.threetag.palladium.util.property.PalladiumProperty;
import net.threetag.palladium.util.property.PropertyManager;

import java.util.Map;
import java.util.stream.Collectors;

public abstract class ConditionSerializer extends RegistryEntry<ConditionSerializer> implements IDefaultDocumentedConfigurable {

    public static final ResourceKey<Registry<ConditionSerializer>> RESOURCE_KEY = ResourceKey.createRegistryKey(new ResourceLocation(Palladium.MOD_ID, "condition_serializers"));
    public static final Registrar<ConditionSerializer> REGISTRY = Registries.get(Palladium.MOD_ID).builder(RESOURCE_KEY.location(), new ConditionSerializer[0]).build();

    final PropertyManager propertyManager = new PropertyManager();

    public <T> ConditionSerializer withProperty(PalladiumProperty<T> data, T value) {
        this.propertyManager.register(data, value);
        return this;
    }

    public <T> T getProperty(JsonObject json, PalladiumProperty<T> data) {
        if (this.propertyManager.isRegistered(data)) {
            if (json.has(data.getKey())) {
                JsonElement jsonElement = json.get(data.getKey());
                if (jsonElement.isJsonPrimitive() && jsonElement.getAsString().equals("null")) {
                    return null;
                } else {
                    return data.fromJSON(json.get(data.getKey()));
                }
            } else {
                this.propertyManager.get(data);
            }
        } else {
            throw new RuntimeException("Condition Serializer does not have " + data.getKey() + " data!");
        }
        return null;
    }

    public abstract Condition make(JsonObject json);

    public static Condition fromJSON(JsonObject json) {
        ConditionSerializer conditionSerializer = ConditionSerializer.REGISTRY.get(new ResourceLocation(GsonHelper.getAsString(json, "type")));

        if (conditionSerializer == null) {
            throw new JsonParseException("Condition Serializer '" + GsonHelper.getAsString(json, "type") + "' does not exist!");
        }

        return conditionSerializer.make(json);
    }

    public static DocumentationBuilder documentationBuilder() {
        return new DocumentationBuilder(new ResourceLocation(Palladium.MOD_ID, "ability_conditions"), "Ability Conditions")
                .add(DocumentationBuilder.heading("Ability Conditions"))
                .addDocumentationSettings(REGISTRY.entrySet().stream().map(Map.Entry::getValue).collect(Collectors.toList()));
    }

    @Override
    public PropertyManager getPropertyManager() {
        return this.propertyManager;
    }

    @Override
    public ResourceLocation getId() {
        return REGISTRY.getId(this);
    }
}
