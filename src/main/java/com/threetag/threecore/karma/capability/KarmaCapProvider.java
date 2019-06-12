package com.threetag.threecore.karma.capability;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class KarmaCapProvider implements ICapabilitySerializable<CompoundNBT> {

    private CapabilityKarma karma = new CapabilityKarma();

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == CapabilityKarma.KARMA) {
            return LazyOptional.of(() -> (T) karma);
        }
        return null;
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = new CompoundNBT();
        nbt.putInt("karma", this.karma.getKarma());
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        this.karma.setKarma(nbt.getInt("karma"));
    }
}
