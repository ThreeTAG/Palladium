package net.threetag.palladium.power.ability;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
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
    private boolean needsKey = false;

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

    public Component getDisplayName() {
        Component title = this.propertyManager.get(Ability.TITLE);
        ResourceLocation id = Ability.REGISTRY.getId(this.getAbility());
        return title != null ? title : new TranslatableComponent("ability." + Objects.requireNonNull(id).getNamespace() + "." + id.getPath());
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

    public boolean needsKey() {
        return needsKey;
    }

    public void toBuffer(FriendlyByteBuf buf) {
        buf.writeUtf(this.id);
        buf.writeResourceLocation(Ability.REGISTRY.getId(this.ability));
        this.propertyManager.toBuffer(buf);
        buf.writeBoolean(this.needsKey);
    }

    public static AbilityConfiguration fromBuffer(FriendlyByteBuf buf) {
        String id = buf.readUtf();
        Ability ability = Ability.REGISTRY.get(buf.readResourceLocation());
        AbilityConfiguration configuration = new AbilityConfiguration(id, Objects.requireNonNull(ability));
        configuration.propertyManager.fromBuffer(buf);
        configuration.needsKey = buf.readBoolean();
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
            boolean withKey = false;

            if (GsonHelper.isValidNode(conditions, "unlocking")) {
                JsonArray unlocking = GsonHelper.getAsJsonArray(conditions, "unlocking");

                for (JsonElement jsonElement : unlocking) {
                    JsonObject c = jsonElement.getAsJsonObject();
                    Condition condition = ConditionSerializer.fromJSON(c);

                    if (condition.needsKey()) {
                        if (withKey) {
                            throw new JsonParseException("Can't have two key binding conditions on one ability!");
                        }
                        withKey = true;
                    }

                    configuration.getUnlockingConditions().add(condition);
                }
            }

            if (GsonHelper.isValidNode(conditions, "enabling")) {
                JsonArray enabling = GsonHelper.getAsJsonArray(conditions, "enabling");

                for (JsonElement jsonElement : enabling) {
                    JsonObject c = jsonElement.getAsJsonObject();
                    Condition condition = ConditionSerializer.fromJSON(c);

                    if (condition.needsKey()) {
                        if (withKey) {
                            throw new JsonParseException("Can't have two key binding conditions on one ability!");
                        }
                        withKey = true;
                    }

                    configuration.getEnablingConditions().add(condition);
                }
            }

            if (withKey) {
                configuration.needsKey = true;
            }
        }

        return configuration;
    }
}
