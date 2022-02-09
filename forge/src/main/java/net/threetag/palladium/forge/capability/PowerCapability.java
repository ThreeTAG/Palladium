package net.threetag.palladium.forge.capability;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.util.INBTSerializable;
import net.threetag.palladium.power.EntityPowerHolder;
import net.threetag.palladium.power.IPowerHolder;

public class PowerCapability extends EntityPowerHolder implements INBTSerializable<CompoundTag> {

    public static Capability<IPowerHolder> POWER = CapabilityManager.get(new CapabilityToken<>() {
    });

    public PowerCapability(LivingEntity entity) {
        super(entity);
    }

    @Override
    public CompoundTag serializeNBT() {
        return this.toNBT();
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        this.fromNBT(nbt);
    }
}
