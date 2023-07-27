package net.threetag.palladium.condition.context;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.power.IPowerHolder;
import net.threetag.palladium.power.Power;
import net.threetag.palladium.power.ability.AbilityEntry;

import java.util.HashMap;
import java.util.Map;

public class ConditionContext {

    private final Map<ConditionContextType<?>, Object> values = new HashMap<>();

    private ConditionContext() {
    }

    public static ConditionContext create() {
        return new ConditionContext();
    }

    public static ConditionContext forEntity(LivingEntity entity) {
        return create().with(ConditionContextType.ENTITY, entity).with(ConditionContextType.LEVEL, entity.level);
    }

    public static ConditionContext forArmorInSlot(LivingEntity entity, EquipmentSlot slot) {
        return forEntity(entity).with(ConditionContextType.SLOT, slot).with(ConditionContextType.ITEM, entity.getItemBySlot(slot));
    }

    public static ConditionContext forAbility(LivingEntity entity, AbilityEntry abilityEntry, Power power, IPowerHolder powerHolder) {
        var context = create();

        if (entity != null) {
            context.with(ConditionContextType.ENTITY, entity).with(ConditionContextType.LEVEL, entity.level);
        }

        if (abilityEntry != null) {
            context.with(ConditionContextType.ABILITY, abilityEntry);
        }

        if (power != null) {
            context.with(ConditionContextType.POWER, power);
        }

        if (powerHolder != null) {
            context.with(ConditionContextType.POWER_HOLDER, powerHolder);
        }

        return context;
    }

    public <T> ConditionContext with(ConditionContextType<T> type, T value) {
        this.values.put(type, value);
        return this;
    }

    @SuppressWarnings("unchecked")
    public <T> T get(ConditionContextType<T> type) {
        return (T) this.values.get(type);
    }

    public boolean has(ConditionContextType<?> type) {
        return this.values.containsKey(type);
    }

    public ConditionContext copy() {
        var context = new ConditionContext();
        context.values.putAll(this.values);
        return context;
    }

}
