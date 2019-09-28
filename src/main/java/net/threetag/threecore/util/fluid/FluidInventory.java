package net.threetag.threecore.util.fluid;

import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.wrapper.RecipeWrapper;

public class FluidInventory extends RecipeWrapper {

    private final IFluidTank fluidTank;

    public FluidInventory(IItemHandlerModifiable inv, IFluidTank fluidTank) {
        super(inv);
        this.fluidTank = fluidTank;
    }

    public IFluidTank getFluidTank() {
        return fluidTank;
    }
}
