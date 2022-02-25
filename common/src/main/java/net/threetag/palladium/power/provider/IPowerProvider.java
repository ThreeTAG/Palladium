package net.threetag.palladium.power.provider;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.power.IPowerHolder;
import net.threetag.palladium.power.Power;
import org.jetbrains.annotations.Nullable;

public interface IPowerProvider {

    ResourceLocation getKey();

    IPowerHolder createPower(LivingEntity entity, @Nullable Power power);

}
