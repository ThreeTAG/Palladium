package net.threetag.threecore.capability;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class AccessoireCapProvider implements ICapabilitySerializable<CompoundNBT> {

    private CapabilityAccessoires accessoires = new CapabilityAccessoires();
    private LazyOptional<IAccessoireHolder> optional = LazyOptional.of(() -> accessoires);

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == CapabilityAccessoires.ACCESSOIRES) {
            return (LazyOptional<T>) optional;
        }
        return LazyOptional.empty();
    }

    @Override
    public CompoundNBT serializeNBT() {
        return this.accessoires.serializeNBT();
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        this.accessoires.deserializeNBT(nbt);
    }
}
