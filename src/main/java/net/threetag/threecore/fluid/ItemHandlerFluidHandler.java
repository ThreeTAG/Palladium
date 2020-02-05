package net.threetag.threecore.fluid;

import com.google.common.collect.Lists;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;
import java.util.List;

public class ItemHandlerFluidHandler implements IFluidHandler {

    private final IItemHandler itemHandler;

    public ItemHandlerFluidHandler(IItemHandler itemHandler) {
        this.itemHandler = itemHandler;
    }

    public List<IFluidHandlerItem> getFluidHandlers() {
        List<IFluidHandlerItem> fluidHandlerItems = Lists.newArrayList();
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            ItemStack stack = itemHandler.getStackInSlot(i);
            stack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY).ifPresent(fluidHandlerItems::add);
        }

        return fluidHandlerItems;
    }

    protected void validateTankIndex(int tank, List<IFluidHandlerItem> fluidHandlers) {
        if (tank < 0 || tank >= fluidHandlers.size())
            throw new RuntimeException("Tank " + tank + " not in valid range - [0," + fluidHandlers.size() + ")");
    }

    @Override
    public int getTanks() {
        return getFluidHandlers().size();
    }

    @Nonnull
    @Override
    public FluidStack getFluidInTank(int tank) {
        List<IFluidHandlerItem> fluidHandlerItems = getFluidHandlers();
        this.validateTankIndex(tank, fluidHandlerItems);
        // TODO find a way to not use tank 0
        return fluidHandlerItems.get(tank).getFluidInTank(0);
    }

    @Override
    public int getTankCapacity(int tank) {
        List<IFluidHandlerItem> fluidHandlerItems = getFluidHandlers();
        this.validateTankIndex(tank, fluidHandlerItems);
        return fluidHandlerItems.get(tank).getTankCapacity(0);
    }

    @Override
    public boolean isFluidValid(int tank, @Nonnull FluidStack stack) {
        List<IFluidHandlerItem> fluidHandlerItems = getFluidHandlers();
        this.validateTankIndex(tank, fluidHandlerItems);
        return fluidHandlerItems.get(tank).isFluidValid(tank, stack);
    }

    @Override
    public int fill(FluidStack resource, FluidAction action) {
        List<IFluidHandlerItem> fluidHandlerItems = getFluidHandlers();
        FluidStack fs = resource.copy();
        for (IFluidHandlerItem fluidHandler : fluidHandlerItems) {
            if (fs.getAmount() > 0)
                fs.setAmount(fs.getAmount() - fluidHandler.fill(fs, action));
        }
        return resource.getAmount() - fs.getAmount();
    }

    @Nonnull
    @Override
    public FluidStack drain(FluidStack resource, FluidAction action) {
        List<IFluidHandlerItem> fluidHandlerItems = getFluidHandlers();
        FluidStack fs = resource.copy();
        for (IFluidHandlerItem fluidHandler : fluidHandlerItems) {
            if (fs.getAmount() > 0 && fluidHandler.getFluidInTank(0).getFluid().isEquivalentTo(resource.getFluid()))
                fs.setAmount(fs.getAmount() - fluidHandler.drain(fs, action).getAmount());
        }
        FluidStack result = resource.copy();
        result.setAmount(resource.getAmount() - fs.getAmount());
        return result;
    }

    @Nonnull
    @Override
    public FluidStack drain(int maxDrain, FluidAction action) {
        for (IFluidHandlerItem item : getFluidHandlers()) {
            FluidStack drain = item.drain(maxDrain, action);
            if (!drain.isEmpty())
                return drain;
        }
        return FluidStack.EMPTY;
    }
}
