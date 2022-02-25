package net.threetag.palladium.power.holderfactory;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.power.DefaultPowerHolder;
import net.threetag.palladium.power.IPowerHolder;
import net.threetag.palladium.power.Power;
import net.threetag.palladium.power.PowerManager;
import net.threetag.palladium.power.provider.IPowerProvider;
import net.threetag.palladium.power.provider.PowerProvider;
import net.threetag.palladium.util.property.PalladiumProperty;
import net.threetag.palladium.util.property.ResourceLocationProperty;

import java.util.function.BiFunction;
import java.util.function.Consumer;

public class SuperpowerPowerProviderFactory extends PowerProviderFactory implements BiFunction<LivingEntity, Power, IPowerHolder> {

    public static final PalladiumProperty<ResourceLocation> SUPERPOWER_ID = new ResourceLocationProperty("superpower");

    private PowerProvider provider = null;

    @Override
    public void create(Consumer<IPowerProvider> consumer) {
        consumer.accept(this.provider = new PowerProvider(new ResourceLocation(Palladium.MOD_ID, "superpower"), this));
    }

    @Override
    public IPowerHolder apply(LivingEntity entity, Power power) {
        power = power == null ? PowerManager.getInstance(entity.level).getPower(SUPERPOWER_ID.get(entity)) : power;
        return power != null ? new DefaultPowerHolder(entity, power, this.provider.getKey(), defaultPowerHolder -> {
            ResourceLocation powerId = SUPERPOWER_ID.get(defaultPowerHolder.entity);
            return defaultPowerHolder.getPower().isInvalid() || powerId == null || !powerId.equals(defaultPowerHolder.getPower().getId());
        }) : null;
    }
}
