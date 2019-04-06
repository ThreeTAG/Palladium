package com.threetag.threecore.abilities.capability;

import com.threetag.threecore.abilities.IAbilityContainer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class AbilityContainerProvider implements ICapabilitySerializable<NBTTagCompound> {

    public final IAbilityContainer container;

    public AbilityContainerProvider(IAbilityContainer container) {
        this.container = container;
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable EnumFacing side) {
        return cap == CapabilityAbilityContainer.ABILITY_CONTAINER ? LazyOptional.of(() -> (T) this.container) : LazyOptional.empty();
    }

    @Override
    public NBTTagCompound serializeNBT() {
        if (this.container instanceof INBTSerializable)
            return ((INBTSerializable<NBTTagCompound>) this.container).serializeNBT();
        return new NBTTagCompound();
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {
        if (this.container instanceof INBTSerializable)
            ((INBTSerializable<NBTTagCompound>) this.container).deserializeNBT(nbt);
    }
}
