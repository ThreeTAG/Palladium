package net.threetag.threecore.capability;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class MultiAbilityContainerProvider implements ICapabilitySerializable<CompoundNBT> {

    public final IMultiAbilityContainer container;
    public final LazyOptional<IMultiAbilityContainer> lazyOptional;

    public MultiAbilityContainerProvider(IMultiAbilityContainer container) {
        this.container = container;
        this.lazyOptional = LazyOptional.of(() -> container);
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        return cap == CapabilityAbilityContainer.MULTI_ABILITY_CONTAINER? (LazyOptional<T>) this.lazyOptional : LazyOptional.empty();
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
