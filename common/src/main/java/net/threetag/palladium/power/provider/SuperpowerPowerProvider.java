package net.threetag.palladium.power.provider;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.power.EntityPowerHolder;
import net.threetag.palladium.power.IPowerHolder;
import net.threetag.palladium.power.Power;
import net.threetag.palladium.power.PowerManager;
import net.threetag.palladium.util.property.PalladiumProperty;
import net.threetag.palladium.util.property.ResourceLocationProperty;

public class SuperpowerPowerProvider implements IPowerProvider {

    public static final PalladiumProperty<ResourceLocation> SUPERPOWER_ID = new ResourceLocationProperty("superpower");

    @Override
    public IPowerHolder createHolder(LivingEntity entity) {
        Power power = PowerManager.getInstance(entity.level).getPower(SUPERPOWER_ID.get(entity));
        return power != null ? new EntityPowerHolder(entity, power) : null;
    }
}
