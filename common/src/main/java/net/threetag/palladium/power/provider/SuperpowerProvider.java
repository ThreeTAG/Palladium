package net.threetag.palladium.power.provider;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.power.DefaultPowerHolder;
import net.threetag.palladium.power.IPowerHandler;
import net.threetag.palladium.power.Power;
import net.threetag.palladium.power.PowerManager;
import net.threetag.palladium.util.property.PalladiumProperty;
import net.threetag.palladium.util.property.ResourceLocationProperty;

public class SuperpowerProvider extends PowerProvider {

    public static final PalladiumProperty<ResourceLocation> SUPERPOWER_ID = new ResourceLocationProperty("superpower");

    @Override
    public void providePowers(LivingEntity entity, IPowerHandler handler) {
        Power power = PowerManager.getInstance(entity.level).getPower(SUPERPOWER_ID.get(entity));

        if (power != null && !handler.hasPower(power)) {
            handler.setPowerHolder(power, new DefaultPowerHolder(entity, power, defaultPowerHolder -> {
                ResourceLocation powerId = SUPERPOWER_ID.get(defaultPowerHolder.entity);
                return powerId == null || !powerId.equals(defaultPowerHolder.getPower().getId());
            }));
        }
    }
}
