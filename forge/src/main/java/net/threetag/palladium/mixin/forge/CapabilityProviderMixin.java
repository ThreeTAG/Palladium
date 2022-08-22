package net.threetag.palladium.mixin.forge;

import net.minecraft.core.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityProvider;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.threetag.palladium.energy.IBlockEntityEnergyContainer;
import net.threetag.palladium.energy.IEnergyStorage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CapabilityProvider.class)
public abstract class CapabilityProviderMixin {

    @Inject(at = @At("HEAD"), method = "getCapability", remap = false, cancellable = true)
    public <T> void getCapability(@NotNull Capability<T> cap, @Nullable Direction side, CallbackInfoReturnable<LazyOptional<T>> ci) {
        if (cap == ForgeCapabilities.ENERGY && this instanceof IBlockEntityEnergyContainer energy) {
            IEnergyStorage energyStorage = energy.getEnergyStorage(side);

            if (energyStorage != null) {
                ci.setReturnValue(LazyOptional.of(() -> (net.minecraftforge.energy.IEnergyStorage) energyStorage).cast());
            }
        }
    }
}
