package net.threetag.palladium.condition;

import com.google.gson.JsonObject;
import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.util.context.DataContext;
import net.threetag.palladium.util.context.DataContextType;
import net.threetag.palladium.power.IPowerHolder;
import net.threetag.palladium.power.Power;
import net.threetag.palladium.power.ability.AbilityConfiguration;
import net.threetag.palladium.power.ability.AbilityEntry;
import net.threetag.palladium.util.property.PropertyManager;

public class ToggleCondition extends KeyCondition {

    public ToggleCondition(int cooldown, AbilityConfiguration.KeyType type, boolean needsEmptyHand, boolean allowScrollingWhenCrouching) {
        super(cooldown, type, needsEmptyHand, allowScrollingWhenCrouching);
    }

    @Override
    public void init(LivingEntity entity, AbilityEntry entry, PropertyManager manager) {
        entry.startCooldown(entity, this.cooldown);
    }

    @Override
    public boolean active(DataContext context) {
        var entity = context.get(DataContextType.ENTITY);
        var entry = context.get(DataContextType.ABILITY);

        if (entity == null || entry == null) {
            return false;
        }

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
    public AbilityConfiguration.KeyPressType getKeyPressType() {
        return AbilityConfiguration.KeyPressType.TOGGLE;
    }

    @Override
    public ConditionSerializer getSerializer() {
        return ConditionSerializers.TOGGLE.get();
    }

    public static class Serializer extends ConditionSerializer {

        public Serializer() {
            this.withProperty(HeldCondition.Serializer.COOLDOWN, 0);
            this.withProperty(KeyCondition.KEY_TYPE_WITH_SCROLLING, AbilityConfiguration.KeyType.KEY_BIND);
            this.withProperty(KeyCondition.NEEDS_EMPTY_HAND, false);
            this.withProperty(KeyCondition.ALLOW_SCROLLING_DURING_CROUCHING, true);
        }

        @Override
        public Condition make(JsonObject json) {
            return new ToggleCondition(this.getProperty(json, HeldCondition.Serializer.COOLDOWN), this.getProperty(json, KeyCondition.KEY_TYPE_WITH_SCROLLING), this.getProperty(json, KeyCondition.NEEDS_EMPTY_HAND), this.getProperty(json, KeyCondition.ALLOW_SCROLLING_DURING_CROUCHING));
        }

        @Override
        public ConditionEnvironment getContextEnvironment() {
            return ConditionEnvironment.DATA;
        }

        @Override
        public String getDocumentationDescription() {
            return "Toggles the ability on and off after a key press or mouse click.";
        }
    }
}
