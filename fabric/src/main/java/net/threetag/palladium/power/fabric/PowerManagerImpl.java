package net.threetag.palladium.power.fabric;

import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.fabric.components.PalladiumComponents;
import net.threetag.palladium.power.IPowerHolder;

public class PowerManagerImpl {

    public static IPowerHolder getPowerHolder(LivingEntity entity) {
        return PalladiumComponents.POWER_HOLDER.get(entity);
    }

}
