package net.threetag.palladium.power.ability;

import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.power.IPowerHolder;

public class SlowfallAbility extends Ability {

    @Override
    public void tick(LivingEntity entity, AbilityEntry entry, IPowerHolder holder, boolean enabled) {
        if(enabled && !entity.isOnGround() && entity.getDeltaMovement().y() < 0D) {
            entity.setDeltaMovement(entity.getDeltaMovement().x, entity.getDeltaMovement().y * 0.6D, entity.getDeltaMovement().z);
            entity.fallDistance = 0F;
        }
    }
}
