package com.threetag.threecore.sizechanging.capability;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class SizeChangingProvider implements ICapabilitySerializable<CompoundNBT> {

    public final ISizeChanging container;
    private LazyOptional<ISizeChanging> optional;

    public SizeChangingProvider(ISizeChanging container) {
        this.container = container;
        optional = LazyOptional.of(() -> container);
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == CapabilitySizeChanging.SIZE_CHANGING) {
            return (LazyOptional<T>) optional;
        }
        return LazyOptional.empty();
    }

    @Override
    public CompoundNBT serializeNBT() {
        if (this.container instanceof INBTSerializable)
            return ((INBTSerializable<CompoundNBT>) this.container).serializeNBT();
        return new CompoundNBT();
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        if (this.container instanceof INBTSerializable)
            ((INBTSerializable<CompoundNBT>) this.container).deserializeNBT(nbt);
    }

}
