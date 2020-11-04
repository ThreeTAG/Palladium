package net.threetag.threecore.capability;

import net.threetag.threecore.ability.AbilityHelper;
import net.threetag.threecore.ability.container.IAbilityContainer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ItemAbilityContainerProvider implements ICapabilitySerializable<CompoundNBT> {

    public final ItemStack stack;
    public final IAbilityContainer abilityContainer;
    public LazyOptional<IAbilityContainer> lazyOptional;

    public ItemAbilityContainerProvider(ItemStack stack) {
        this.stack = stack;
        this.abilityContainer = new ItemAbilityContainer(stack);
        this.lazyOptional = LazyOptional.of(() -> this.abilityContainer);
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        return cap == CapabilityAbilityContainer.ABILITY_CONTAINER ? (LazyOptional<T>) this.lazyOptional : LazyOptional.empty();
    }

    @Override
    public CompoundNBT serializeNBT() {
        return AbilityHelper.saveToNBT(this.abilityContainer.getAbilityMap());
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        AbilityHelper.loadFromNBT(nbt, this.abilityContainer.getAbilityMap());
    }
}
