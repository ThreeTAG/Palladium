package net.threetag.palladium.power.provider;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.power.IPowerHolder;
import net.threetag.palladium.power.Power;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiFunction;

public class PowerProvider implements IPowerProvider {

    private final ResourceLocation key;
    private final BiFunction<LivingEntity, Power, IPowerHolder> supplier;

    public PowerProvider(ResourceLocation key, BiFunction<LivingEntity, Power, IPowerHolder> supplier) {
        this.key = key;
        this.supplier = supplier;
    }

    @Override
    public ResourceLocation getKey() {
        return key;
    }

    @Override
    public IPowerHolder createPower(LivingEntity entity, @Nullable Power power) {
        return this.supplier.apply(entity, power);
    }

}