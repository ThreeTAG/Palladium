package net.threetag.threecore.base.item;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.fluid.FlowingFluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.FluidContainerColorer;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidHandlerItemStack;
import net.minecraftforge.registries.ForgeRegistries;
import net.threetag.threecore.ThreeCore;
import net.threetag.threecore.util.fluid.TCFluidUtil;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class VialItem extends Item {

    public VialItem(Properties properties) {
        super(properties);

        this.addPropertyOverride(new ResourceLocation(ThreeCore.MODID, "empty"), new IItemPropertyGetter() {
            @OnlyIn(Dist.CLIENT)
            @Override
            public float call(ItemStack stack, @Nullable World world, @Nullable LivingEntity entity) {
                AtomicReference<Float> f = new AtomicReference<>((float) 0);
                stack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY).ifPresent(fluid -> {
                    if (fluid.getFluidInTank(0).isEmpty())
                        f.set(1F);
                });
                return f.get();
            }
        });
    }

    @Override
    public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> items) {
        if (CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY != null && this.isInGroup(group)) {
            ForgeRegistries.FLUIDS.getEntries().forEach(entry -> {
                ItemStack stack = new ItemStack(this);
                if (entry.getValue() instanceof FlowingFluid) {
                    if (((FlowingFluid) entry.getValue()).getStillFluid() == entry.getValue()) {
                        stack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY).ifPresent(fluidHandler -> {
                            fluidHandler.fill(new FluidStack(((FlowingFluid) entry.getValue()).getStillFluid(), 500), IFluidHandler.FluidAction.EXECUTE);
                        });
                        items.add(stack);
                    }
                } else {
                    stack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY).ifPresent(fluidHandler -> {
                        fluidHandler.fill(new FluidStack(entry.getValue(), 500), IFluidHandler.FluidAction.EXECUTE);
                    });
                    items.add(stack);
                }
            });
        }
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        if (CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY != null)
            stack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY).ifPresent(fluidHandler -> {
                FluidStack fluidStack = fluidHandler.getFluidInTank(0);
                if (!fluidStack.isEmpty()) {
                    tooltip.add(fluidStack.getDisplayName().deepCopy().applyTextStyle(TextFormatting.GRAY));
                    tooltip.add(new StringTextComponent(TCFluidUtil.getFormattedFluidInfo(fluidStack.getAmount(), fluidHandler.getTankCapacity(0))).applyTextStyle(TextFormatting.GRAY));
                }
            });
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundNBT nbt) {
        return new FluidHandlerItemStack(stack, 500);
    }

    @OnlyIn(Dist.CLIENT)
    public static class ItemColor extends FluidContainerColorer {

        @Override
        public int getColor(@Nonnull ItemStack stack, int tintIndex) {
            FluidStack fluidStack = FluidUtil.getFluidContained(stack).orElse(FluidStack.EMPTY);
            if (!fluidStack.isEmpty() && fluidStack.getFluid().isEquivalentTo(Fluids.LAVA) && tintIndex == 1)
                return 0xff6700;
            else
                return super.getColor(stack, tintIndex);
        }
    }

}
