package com.threetag.threecore.base.item;

import com.threetag.threecore.util.energy.EnergyStorageItem;
import com.threetag.threecore.util.energy.EnergyUtil;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class ItemCapacitor extends Item {

    int capacity, maxTransfer;

    public ItemCapacitor(Properties properties, int capacity, int maxTransfer) {
        super(properties);
        this.capacity = capacity;
        this.maxTransfer = maxTransfer;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void addInformation(ItemStack stack, @Nullable World world, List<ITextComponent> list, ITooltipFlag flag) {
        stack.getCapability(CapabilityEnergy.ENERGY).ifPresent((e) -> list.add(EnergyUtil.getFormattedEnergy(e)));
    }

    @Override
    public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> items) {
        if (this.isInGroup(group)) {
            items.add(new ItemStack(this));
            ItemStack stack = new ItemStack(this);
            stack.getCapability(CapabilityEnergy.ENERGY).ifPresent((e) -> stack.getOrCreateTag().putInt("Energy", e.getMaxEnergyStored()));
            items.add(stack);
        }
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable NBTTagCompound nbt) {
        return new EnergyItemCapabilityProvider(stack, this.capacity, this.maxTransfer);
    }

    @Override
    public double getDurabilityForDisplay(ItemStack stack) {
        AtomicReference<Double> d = new AtomicReference<>(1D);
        stack.getCapability(CapabilityEnergy.ENERGY).ifPresent((e) -> d.set(1D - ((double) e.getEnergyStored() / (double) e.getMaxEnergyStored())));
        return d.get();
    }

    @Override
    public boolean showDurabilityBar(ItemStack stack) {
        return true;
    }

    @Override
    public boolean isDamaged(ItemStack stack) {
        return true;
    }

    public static class EnergyItemCapabilityProvider implements ICapabilityProvider {

        final ItemStack stack;
        final IEnergyStorage energyStorage;

        public EnergyItemCapabilityProvider(ItemStack stack, int capacity, int maxReceive, int maxExtract) {
            this.stack = stack;
            this.energyStorage = new EnergyStorageItem(stack, capacity, maxReceive, maxExtract);
        }

        public EnergyItemCapabilityProvider(ItemStack stack, int capacity, int maxTransfer) {
            this(stack, capacity, maxTransfer, maxTransfer);
        }

        public EnergyItemCapabilityProvider(ItemStack stack, int capacity) {
            this(stack, capacity, capacity, capacity);
        }

        @Nonnull
        @Override
        public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable EnumFacing side) {
            return cap == CapabilityEnergy.ENERGY ? LazyOptional.of(() -> (T) this.energyStorage) : LazyOptional.empty();
        }
    }

}
