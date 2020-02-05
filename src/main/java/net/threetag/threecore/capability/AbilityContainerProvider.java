package net.threetag.threecore.capability;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.threetag.threecore.ability.IAbilityContainer;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class AbilityContainerProvider implements ICapabilitySerializable<CompoundNBT> {

    public final IAbilityContainer container;
    public final LazyOptional<IAbilityContainer> lazyOptional;

    public AbilityContainerProvider(IAbilityContainer container) {
        this.container = container;
        this.lazyOptional = LazyOptional.of(() -> container);
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        return cap == CapabilityAbilityContainer.ABILITY_CONTAINER ? (LazyOptional<T>) this.lazyOptional : LazyOptional.empty();
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
