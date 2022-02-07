package net.threetag.palladium.power;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.power.ability.AbilityConfiguration;
import net.threetag.palladium.power.ability.AbilityEntry;

import java.util.List;
import java.util.Map;

public interface IPowerHolder {

    Power getPower();

    void setPower(Power power);

    Map<String, AbilityEntry> getAbilities();

    void tick(LivingEntity entity);

    CompoundTag toNBT();

    void fromNBT(CompoundTag nbt);

}
