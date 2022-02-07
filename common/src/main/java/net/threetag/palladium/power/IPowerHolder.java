package net.threetag.palladium.power;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.power.ability.AbilityConfiguration;

import java.util.List;

public interface IPowerHolder {

    Power getPower();

    void setPower(Power power);

    List<AbilityConfiguration> getAbilities();

    void tick(LivingEntity entity);

    CompoundTag toNBT();

    void fromNBT(CompoundTag nbt);

}
