package com.threetag.threecore.abilities.capability;

import com.threetag.threecore.abilities.IAbilityContainer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ItemAbilityContainerProvider implements ICapabilityProvider {

    public final ItemStack stack;
    public final IAbilityContainer abilityContainer;

    public ItemAbilityContainerProvider(ItemStack stack) {
        this.stack = stack;
        this.abilityContainer = new ItemAbilityContainer(stack);
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        return cap == CapabilityAbilityContainer.ABILITY_CONTAINER ? LazyOptional.of(() -> (T) this.abilityContainer) : LazyOptional.empty();
    }

}
