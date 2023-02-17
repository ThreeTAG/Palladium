package net.threetag.palladium.power.ability;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.addonpack.log.AddonPackLog;
import net.threetag.palladium.condition.*;
import net.threetag.palladium.util.icon.IIcon;
import net.threetag.palladium.util.icon.IconSerializer;
import net.threetag.palladium.util.json.GsonUtil;
import net.threetag.palladium.util.property.PalladiumProperty;
import net.threetag.palladium.util.property.PropertyManager;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AbilityConfiguration {

    private final String id;
    private final Ability ability;
    private final PropertyManager propertyManager;
    private final List<Condition> unlockingConditions = new ArrayList<>();
    private final List<Condition> enablingConditions = new ArrayList<>();
    private boolean buyable = false;
    private CooldownType cooldownType = CooldownType.STATIC;
    private boolean needsKey = false;
    private KeyType keyType = KeyType.KEY_BIND;
    private KeyPressType keyPressType = KeyPressType.ONCE;
    public List<String> dependencies = new ArrayList<>();

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
        ResourceLocation id = Ability.REGISTRY.getKey(this.getAbility());
        return title != null ? title : Component.translatable("ability." + Objects.requireNonNull(id).getNamespace() + "." + id.getPath());
    }

    public <T> AbilityConfiguration set(PalladiumProperty<T> data, T value) {
        this.propertyManager.set(data, value);
        return this;
    }

    public <T> T get(PalladiumProperty<T> property) {
        return this.propertyManager.get(property);
    }

    public PropertyManager getPropertyManager() {
        return propertyManager;
    }

    public List<Condition> getUnlockingConditions() {
        return this.unlockingConditions;
    }

    public List<Condition> getEnablingConditions() {
        return this.enablingConditions;
    }

    public CooldownType getCooldownType() {
        return this.cooldownType;
    }

    public List<String> getDependencies() {
        return this.dependencies;
    }

    public boolean needsKey() {
        return this.needsKey;
    }

    public KeyType getKeyType() {
        return this.keyType;
    }

    public KeyPressType getKeyPressType() {
        return this.keyPressType;
    }

    public boolean isBuyable() {
        return buyable;
    }

    @Nullable
    public BuyableCondition findBuyCondition() {
        for (Condition condition : this.getUnlockingConditions()) {
            if (condition instanceof BuyableCondition buyableCondition) {
                return buyableCondition;
            }
        }
        return null;
    }

    public void toBuffer(FriendlyByteBuf buf) {
        buf.writeUtf(this.id);
        buf.writeResourceLocation(Objects.requireNonNull(Ability.REGISTRY.getKey(this.ability)));
        this.propertyManager.toBuffer(buf);
        buf.writeBoolean(this.needsKey);
        buf.writeBoolean(this.buyable);
        buf.writeInt(this.keyType.ordinal());
        buf.writeInt(this.keyPressType.ordinal());
        buf.writeInt(this.cooldownType.ordinal());
        buf.writeInt(this.dependencies.size());

        for (String s : this.dependencies) {
            buf.writeUtf(s);
        }
    }


    public static AbilityConfiguration fromBuffer(FriendlyByteBuf buf) {
        String id = buf.readUtf();
        Ability ability = Ability.REGISTRY.get(buf.readResourceLocation());
        AbilityConfiguration configuration = new AbilityConfiguration(id, Objects.requireNonNull(ability));
        configuration.propertyManager.fromBuffer(buf);
        configuration.needsKey = buf.readBoolean();
        configuration.buyable = buf.readBoolean();
        configuration.keyType = KeyType.values()[buf.readInt()];
        configuration.keyPressType = KeyPressType.values()[buf.readInt()];
        configuration.cooldownType = CooldownType.values()[buf.readInt()];
        int keys = buf.readInt();
        for (int i = 0; i < keys; i++) {
            configuration.dependencies.add(buf.readUtf());
        }

        return configuration;
    }

    public static AbilityConfiguration fromJSON(String id, JsonObject json) {
        var abilityId = GsonUtil.getAsResourceLocation(json, "type");

        if(abilityId.equals(Palladium.id("interpolated_integer"))) {
            abilityId = Abilities.ANIMATION_TIMER.getId();
            AddonPackLog.warning("'interpolated_integer' ability found in power, please use 'animation_timer' instead!");
        }

        Ability ability = Ability.REGISTRY.get(abilityId);

        if (ability == null) {
            if (GsonHelper.isValidNode(json, "ability")) {
                ability = Ability.REGISTRY.get(new ResourceLocation(GsonHelper.getAsString(json, "ability")));
                AddonPackLog.warning("Usage of 'ability' in ability declarations is deprecated!");

                if (ability == null) {
                    throw new JsonParseException("Ability '" + GsonHelper.getAsString(json, "ability") + "' does not exist!");
                }
            } else {
                throw new JsonParseException("Ability '" + GsonHelper.getAsString(json, "type") + "' does not exist!");
            }
        }

        AbilityConfiguration configuration = new AbilityConfiguration(id, ability);
        configuration.propertyManager.fromJSON(json);

        if (GsonHelper.isValidNode(json, "conditions")) {
            ConditionSerializer.CURRENT_CONTEXT = ConditionContextType.ABILITIES;
            JsonObject conditions = GsonHelper.getAsJsonObject(json, "conditions");
            boolean withKey = false;
            CooldownType cooldownType = null;

            if (GsonHelper.isValidNode(conditions, "unlocking")) {
                JsonElement condJson = conditions.get("unlocking");
                var condList = ConditionSerializer.listFromJSON(condJson, ConditionContextType.ABILITIES);

                for (Condition condition : condList) {
                    if (condition instanceof BuyableCondition buyableCondition) {
                        if (configuration.buyable) {
                            throw new JsonParseException("Can't have more than one buyable unlock condition!");
                        } else {
                            configuration.buyable = true;
                        }
                    }

                    if (condition.needsKey()) {
                        throw new JsonParseException("Can't have key binding conditions for unlocking!");
                    }

                    if (condition.handlesCooldown()) {
                        if (cooldownType != null) {
                            throw new JsonParseException("Can't have two abilities handling the cooldown!");
                        } else {
                            cooldownType = condition.getCooldownType();
                        }
                    }

                    configuration.getUnlockingConditions().add(condition);
                    configuration.dependencies.addAll(condition.getDependentAbilities());
                }
            }

            if (GsonHelper.isValidNode(conditions, "enabling")) {
                JsonElement condJson = conditions.get("enabling");
                var condList = ConditionSerializer.listFromJSON(condJson, ConditionContextType.ABILITIES);

                for (Condition condition : condList) {
                    if (condition instanceof BuyableCondition) {
                        throw new JsonParseException("Can't have a buyable unlock condition for enabling!");
                    }

                    if (condition.needsKey()) {
                        if (withKey) {
                            throw new JsonParseException("Can't have two key binding conditions on one ability!");
                        }
                        withKey = true;
                        configuration.keyType = condition.getKeyType();
                        configuration.keyPressType = condition.getKeyPressType();
                    }

                    if (condition.handlesCooldown()) {
                        if (cooldownType != null) {
                            throw new JsonParseException("Can't have two abilities handling the cooldown!");
                        } else {
                            cooldownType = condition.getCooldownType();
                        }
                    }

                    configuration.getEnablingConditions().add(condition);
                    configuration.dependencies.addAll(condition.getDependentAbilities());
                }
            }

            if (withKey) {
                configuration.needsKey = true;
            }

            configuration.cooldownType = cooldownType == null ? CooldownType.STATIC : cooldownType;
        }

        ability.postParsing(configuration);

        return configuration;
    }

    public enum KeyType {

        KEY_BIND, LEFT_CLICK, RIGHT_CLICK, SPACE_BAR;

    }

    public enum KeyPressType {

        ONCE, HOLD

    }

    public static class UnlockData {

        public final IIcon icon;
        public final int amount;
        public final Component description;

        public UnlockData(IIcon icon, int amount, Component description) {
            this.icon = icon;
            this.amount = amount;
            this.description = description;
        }

        public UnlockData(FriendlyByteBuf buf) {
            this.icon = IconSerializer.parseNBT(Objects.requireNonNull(buf.readAnySizeNbt()));
            this.amount = buf.readInt();
            this.description = buf.readComponent();
        }

        public void toBuffer(FriendlyByteBuf buf) {
            buf.writeNbt(IconSerializer.serializeNBT(this.icon));
            buf.writeInt(this.amount);
            buf.writeComponent(this.description);
        }
    }
}
