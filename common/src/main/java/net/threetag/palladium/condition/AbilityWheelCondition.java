package net.threetag.palladium.condition;

import com.google.gson.JsonObject;
import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.power.IPowerHolder;
import net.threetag.palladium.power.Power;
import net.threetag.palladium.power.ability.AbilityConfiguration;
import net.threetag.palladium.power.ability.AbilityInstance;
import net.threetag.palladium.util.context.DataContext;
import net.threetag.palladium.util.context.DataContextType;
import net.threetag.palladium.util.property.IntegerProperty;
import net.threetag.palladium.util.property.PalladiumProperty;

import java.util.Objects;

public class AbilityWheelCondition extends Condition {

    public final int cooldown;

    public AbilityWheelCondition(int cooldown) {
        this.cooldown = cooldown;
    }

    @Override
    public boolean active(DataContext context) {
        var entity = context.getLivingEntity();
        var entry = context.get(DataContextType.ABILITY);

        if (entity == null || entry == null) {
            return false;
        }

        if (Objects.requireNonNull(entry).keyPressed) {
            entry.keyPressed = false;
            if (entry.getEnabledTicks() == 0 && this.cooldown != 0) {
                entry.startCooldown(entity, this.cooldown);
            }
            return true;
        }

        return false;
    }

    @Override
    public void onKeyPressed(LivingEntity entity, AbilityInstance entry, Power power, IPowerHolder holder) {
        if (entry.cooldown == 0) {
            entry.keyPressed = true;
        }
    }

    @Override
    public ConditionSerializer getSerializer() {
        return ConditionSerializers.ABILITY_WHEEL.get();
    }

    @Override
    public boolean handlesCooldown() {
        return this.cooldown > 0;
    }

    public static class Serializer extends ConditionSerializer {

        public static final PalladiumProperty<Integer> COOLDOWN = new IntegerProperty("cooldown").configurable("Amount of ticks the ability wont be useable for after using it");

        public Serializer() {
            this.withProperty(COOLDOWN, 0);
        }

        @Override
        public Condition make(JsonObject json) {
            return new AbilityWheelCondition(this.getProperty(json, COOLDOWN));
        }

        @Override
        public ConditionEnvironment getContextEnvironment() {
            return ConditionEnvironment.DATA;
        }

        @Override
        public String getDocumentationDescription() {
            return "Allows ability to be used in an ability wheel.";
        }
    }
}
