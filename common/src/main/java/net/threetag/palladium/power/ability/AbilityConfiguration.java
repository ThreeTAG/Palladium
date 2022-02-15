package net.threetag.palladium.power.ability;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.threetag.palladium.power.ability.condition.Condition;
import net.threetag.palladium.power.ability.condition.ConditionSerializer;
import net.threetag.palladium.util.property.PalladiumProperty;
import net.threetag.palladium.util.property.PropertyManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AbilityConfiguration {

    private final String id;
    private final Ability ability;
    private final PropertyManager propertyManager;
    private final List<Condition> unlockingConditions = new ArrayList<>();
    private final List<Condition> enablingConditions = new ArrayList<>();

    public AbilityConfiguration(String id, Ability ability) {
        this.id = id;
        this.ability = ability;
        this.propertyManager = ability.propertyManager.copy();
    }

    public String getId() {
        return id;
    }

    public Ability getAbility() {
        return ability;
    }

    public <T> AbilityConfiguration set(PalladiumProperty<T> data, T value) {
        this.propertyManager.set(data, value);
        return this;
    }

    public <T> T get(PalladiumProperty<T> property) {
        return this.propertyManager.get(property);
    }

    public List<Condition> getUnlockingConditions() {
        return unlockingConditions;
    }

    public List<Condition> getEnablingConditions() {
        return enablingConditions;
    }

    public void toBuffer(FriendlyByteBuf buf) {
        buf.writeUtf(this.id);
        buf.writeResourceLocation(Ability.REGISTRY.getId(this.ability));
        this.propertyManager.toBuffer(buf);
    }

    public static AbilityConfiguration fromBuffer(FriendlyByteBuf buf) {
        String id = buf.readUtf();
        Ability ability = Ability.REGISTRY.get(buf.readResourceLocation());
        AbilityConfiguration configuration = new AbilityConfiguration(id, Objects.requireNonNull(ability));
        configuration.propertyManager.fromBuffer(buf);
        return configuration;
    }

    public static AbilityConfiguration fromJSON(String id, JsonObject json) {
        Ability ability = Ability.REGISTRY.get(new ResourceLocation(GsonHelper.getAsString(json, "ability")));

        if (ability == null) {
            throw new JsonParseException("Ability '" + GsonHelper.getAsString(json, "ability") + "' does not exist!");
        }

        AbilityConfiguration configuration = new AbilityConfiguration(id, ability);
        configuration.propertyManager.fromJSON(json);

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
