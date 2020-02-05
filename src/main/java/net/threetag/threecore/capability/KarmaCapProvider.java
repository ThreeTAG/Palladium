package net.threetag.threecore.capability;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.threetag.threecore.karma.IKarma;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class KarmaCapProvider implements ICapabilitySerializable<CompoundNBT> {

    private CapabilityKarma karma = new CapabilityKarma();
    private LazyOptional<IKarma> optional = LazyOptional.of(() -> karma);

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == CapabilityKarma.KARMA) {
            return (LazyOptional<T>) optional;
        }
        return LazyOptional.empty();
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
