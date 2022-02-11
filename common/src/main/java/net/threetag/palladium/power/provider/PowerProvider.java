package net.threetag.palladium.power.provider;

import dev.architectury.core.RegistryEntry;
import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.power.IPowerHolder;
import net.threetag.palladium.power.Power;
import net.threetag.palladium.power.PowerManager;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public abstract class PowerProvider extends RegistryEntry<PowerProvider> {

    public abstract IPowerHolder createHolder(LivingEntity entity, @Nullable Power power);

    public Optional<IPowerHolder> get(LivingEntity entity) {
        return Optional.ofNullable(PowerManager.getPowerHandler(entity).getPowerHolder(this));
    }

}