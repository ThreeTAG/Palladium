package net.threetag.palladium.condition;

import com.google.gson.JsonObject;
import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.util.context.DataContext;
import net.threetag.palladium.util.context.DataContextType;
import net.threetag.palladium.power.IPowerHolder;
import net.threetag.palladium.power.Power;
import net.threetag.palladium.power.ability.AbilityConfiguration;
import net.threetag.palladium.power.ability.AbilityEntry;
import net.threetag.palladium.util.property.IntegerProperty;
import net.threetag.palladium.util.property.PalladiumProperty;
import net.threetag.palladium.util.property.PropertyManager;

public class HeldCondition extends KeyCondition {

    public HeldCondition(int cooldown, AbilityConfiguration.KeyType type, boolean needsEmptyHand) {
        super(cooldown, type, needsEmptyHand, true);
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

        if (this.cooldown != 0 && entry.cooldown == 0 && entry.keyPressed) {
            entry.keyPressed = false;
        }
        return entry.keyPressed;
    }

    @Override
    public void onKeyPressed(LivingEntity entity, AbilityEntry entry, Power power, IPowerHolder holder) {
        entry.keyPressed = true;
    }

    @Override
    public void onKeyReleased(LivingEntity entity, AbilityEntry entry, Power power, IPowerHolder holder) {
        entry.keyPressed = false;
    }

    @Override
    public AbilityConfiguration.KeyPressType getKeyPressType() {
        return AbilityConfiguration.KeyPressType.HOLD;
    }

    @Override
    public CooldownType getCooldownType() {
        return CooldownType.DYNAMIC;
    }

    @Override
    public ConditionSerializer getSerializer() {
        return ConditionSerializers.HELD.get();
    }

    public static class Serializer extends ConditionSerializer {

        public static final PalladiumProperty<Integer> COOLDOWN = new IntegerProperty("cooldown").configurable("Amount of ticks the ability can be used for");

        public Serializer() {
            this.withProperty(COOLDOWN, 0);
            this.withProperty(KeyCondition.KEY_TYPE_WITHOUT_SCROLLING, AbilityConfiguration.KeyType.KEY_BIND);
            this.withProperty(KeyCondition.NEEDS_EMPTY_HAND, false);
        }

        @Override
        public Condition make(JsonObject json) {
            return new HeldCondition(this.getProperty(json, COOLDOWN), this.getProperty(json, KeyCondition.KEY_TYPE_WITHOUT_SCROLLING), this.getProperty(json, KeyCondition.NEEDS_EMPTY_HAND));
        }

        @Override
        public ConditionEnvironment getContextEnvironment() {
            return ConditionEnvironment.DATA;
        }

        @Override
        public String getDocumentationDescription() {
            return "Allows the ability to be used while holding a key bind.";
        }
    }
}
