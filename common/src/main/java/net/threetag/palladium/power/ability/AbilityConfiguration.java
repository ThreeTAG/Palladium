package net.threetag.palladium.power.ability;

import com.google.gson.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.threetag.palladium.power.ability.condition.Condition;
import net.threetag.palladium.power.ability.condition.ConditionSerializer;
import net.threetag.palladium.util.property.PalladiumProperty;
import net.threetag.palladium.util.property.PalladiumPropertyValue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AbilityConfiguration {

    private final String id;
    private final Ability ability;
    private final Map<PalladiumProperty<?>, PalladiumPropertyValue<?>> properties = new HashMap<>();
    private final List<Condition> unlockingConditions = new ArrayList<>();
    private final List<Condition> enablingConditions = new ArrayList<>();

    @SuppressWarnings({"unchecked", "rawtypes"})
    public AbilityConfiguration(String id, Ability ability) {
        this.id = id;
        this.ability = ability;
        ability.defaultProperties.forEach((d, v) -> this.properties.put(d, new PalladiumPropertyValue(d, v)));
    }

    public String getId() {
        return id;
    }

    public Ability getAbility() {
        return ability;
    }

    public <T> AbilityConfiguration set(PalladiumProperty<T> data, T value) {
        this.properties.put(data, new PalladiumPropertyValue<>(data, value));
        return this;
    }

    @SuppressWarnings("unchecked")
    public <T> T get(PalladiumProperty<T> property) {
        return (T) this.properties.get(property).getValue();
    }

    public List<Condition> getUnlockingConditions() {
        return unlockingConditions;
    }

    public List<Condition> getEnablingConditions() {
        return enablingConditions;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public static AbilityConfiguration fromJSON(String id, JsonObject json) {
        Ability ability = Ability.REGISTRY.get(new ResourceLocation(GsonHelper.getAsString(json, "ability")));

        if (ability == null) {
            throw new JsonParseException("Ability '" + GsonHelper.getAsString(json, "ability") + "' does not exist!");
        }

        AbilityConfiguration configuration = new AbilityConfiguration(id, ability);

        configuration.properties.forEach(((property, value) -> {
            if (json.has(property.getKey())) {
                configuration.properties.put(property, new PalladiumPropertyValue(property, property.fromJSON(json.get(property.getKey()))));
            }
        }));

        if (GsonHelper.isValidNode(json, "conditions")) {
            JsonObject conditions = GsonHelper.getAsJsonObject(json, "conditions");

            if (GsonHelper.isValidNode(conditions, "unlocking")) {
                JsonArray unlocking = GsonHelper.getAsJsonArray(conditions, "unlocking");

                for (JsonElement jsonElement : unlocking) {
                    JsonObject c = jsonElement.getAsJsonObject();
                    configuration.getUnlockingConditions().add(ConditionSerializer.fromJSON(c));
                }
            }

            if (GsonHelper.isValidNode(conditions, "enabling")) {
                JsonArray enabling = GsonHelper.getAsJsonArray(conditions, "enabling");

                for (JsonElement jsonElement : enabling) {
                    JsonObject c = jsonElement.getAsJsonObject();
                    configuration.getEnablingConditions().add(ConditionSerializer.fromJSON(c));
                }
            }
        }

        return configuration;
    }
}
