package net.threetag.palladium.condition;

import com.google.gson.JsonObject;
import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.power.IPowerHolder;
import net.threetag.palladium.power.Power;
import net.threetag.palladium.power.ability.AbilityConfiguration;
import net.threetag.palladium.power.ability.AbilityEntry;
import net.threetag.palladium.util.property.PropertyManager;

public class ToggleCondition extends KeyCondition {

    public ToggleCondition(int cooldown, AbilityConfiguration.KeyType type, boolean needsEmptyHand) {
        super(cooldown, type, needsEmptyHand);
    }

    @Override
    public void init(LivingEntity entity, AbilityEntry entry, PropertyManager manager) {
        entry.startCooldown(entity, this.cooldown);
    }

    @Override
    public boolean active(LivingEntity entity, AbilityEntry entry, Power power, IPowerHolder holder) {
        if (this.cooldown != 0 && entry.cooldown == 0) {
            entry.keyPressed = false;
        }
        return entry.keyPressed;
    }

    @Override
    public void onKeyPressed(LivingEntity entity, AbilityEntry entry, Power power, IPowerHolder holder) {
        entry.keyPressed = !entry.keyPressed;
    }

    @Override
    public CooldownType getCooldownType() {
        return CooldownType.DYNAMIC;
    }

    @Override
    public ConditionSerializer getSerializer() {
        return ConditionSerializers.TOGGLE.get();
    }

    public static class Serializer extends ConditionSerializer {

        public Serializer() {
            this.withProperty(HeldCondition.Serializer.COOLDOWN, 0);
            this.withProperty(KeyCondition.KEY_TYPE, AbilityConfiguration.KeyType.KEY_BIND);
            this.withProperty(KeyCondition.NEEDS_EMPTY_HAND, false);
        }

        @Override
        public Condition make(JsonObject json) {
            return new ToggleCondition(this.getProperty(json, HeldCondition.Serializer.COOLDOWN), this.getProperty(json, KeyCondition.KEY_TYPE), this.getProperty(json, KeyCondition.NEEDS_EMPTY_HAND));
        }

        @Override
        public ConditionContextType getContextType() {
            return ConditionContextType.ABILITIES;
        }

        @Override
        public String getDocumentationDescription() {
            return "Toggles the ability on and off after a key press or mouse click.";
        }
    }
}
