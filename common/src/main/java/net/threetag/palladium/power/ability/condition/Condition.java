package net.threetag.palladium.power.ability.condition;

import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.power.IPowerHolder;
import net.threetag.palladium.power.Power;
import net.threetag.palladium.power.ability.AbilityConfiguration;

public abstract class Condition {

    public abstract boolean active(LivingEntity entity, AbilityConfiguration entry, Power power, IPowerHolder holder);

}
