package net.threetag.palladium.power.provider;

import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.power.IPowerHolder;
import net.threetag.palladium.power.PowerManager;

import java.util.Optional;

public interface IPowerProvider {

    IPowerHolder createHolder(LivingEntity entity);

    default Optional<IPowerHolder> get(LivingEntity entity) {
        return Optional.ofNullable(PowerManager.getPowerHandler(entity).getPowerHolder(this));
    }

}