package net.threetag.palladium.power;

import net.minecraft.world.entity.LivingEntity;

public interface IPowerProvider {

    Power getPower(LivingEntity entity);

}