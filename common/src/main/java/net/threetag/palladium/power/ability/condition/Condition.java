package net.threetag.palladium.power.ability.condition;

import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.power.IPowerHolder;
import net.threetag.palladium.power.Power;
import net.threetag.palladium.power.ability.AbilityEntry;

public abstract class Condition {

    public abstract boolean active(LivingEntity entity, AbilityEntry entry, Power power, IPowerHolder holder);

    public boolean needsKey() {
        return false;
    }

    public void onKeyPressed(LivingEntity entity, AbilityEntry entry, Power power, IPowerHolder holder) {
        entry.getConfiguration().getAbility().tick(entity, entry, holder, true);
    }

}
