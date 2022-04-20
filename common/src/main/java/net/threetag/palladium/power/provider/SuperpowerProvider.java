package net.threetag.palladium.power.provider;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.power.DefaultPowerHolder;
import net.threetag.palladium.power.IPowerHandler;
import net.threetag.palladium.power.Power;
import net.threetag.palladium.power.PowerManager;
import net.threetag.palladium.util.property.PalladiumProperties;

public class SuperpowerProvider extends PowerProvider {

    @Override
    public void providePowers(LivingEntity entity, IPowerHandler handler) {
        Power power = PowerManager.getInstance(entity.level).getPower(PalladiumProperties.SUPERPOWER_ID.get(entity));

        if (power != null && !handler.hasPower(power)) {
            handler.setPowerHolder(power, new DefaultPowerHolder(entity, power, defaultPowerHolder -> {
                ResourceLocation powerId = PalladiumProperties.SUPERPOWER_ID.get(defaultPowerHolder.entity);
                return powerId == null || !powerId.equals(defaultPowerHolder.getPower().getId());
            }));
        }
    }
}
