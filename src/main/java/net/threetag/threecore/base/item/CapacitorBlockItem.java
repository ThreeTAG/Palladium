package net.threetag.threecore.base.item;

import net.minecraft.block.Block;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.*;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.energy.CapabilityEnergy;
import net.threetag.threecore.ThreeCore;
import net.threetag.threecore.util.energy.EnergyUtil;

import javax.annotation.Nullable;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class CapacitorBlockItem extends BlockItem {

    int capacity, maxTransfer;

    public CapacitorBlockItem(Block blockIn, Properties builder, int capacity, int maxTransfer) {
        super(blockIn, builder);
        this.capacity = capacity;
        this.maxTransfer = maxTransfer;
        this.addPropertyOverride(new ResourceLocation(ThreeCore.MODID, "energy"), new IItemPropertyGetter() {
            @OnlyIn(Dist.CLIENT)
            @Override
            public float call(ItemStack stack, @Nullable World world, @Nullable LivingEntity entity) {
                AtomicReference<Float> f = new AtomicReference<>((float) 0);
                stack.getCapability(CapabilityEnergy.ENERGY).ifPresent(energyStorage -> {
                    f.set((float) energyStorage.getEnergyStored() / (float) energyStorage.getMaxEnergyStored());
                });
                return f.get();
            }
        });
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void addInformation(ItemStack stack, @Nullable World world, List<ITextComponent> list, ITooltipFlag flag) {
        stack.getCapability(CapabilityEnergy.ENERGY).ifPresent((e) -> list.add(EnergyUtil.getFormattedEnergy(e).applyTextStyle(TextFormatting.GRAY)));
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
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundNBT nbt) {
        return new CapacitorItem.EnergyItemCapabilityProvider(stack, this.capacity, this.maxTransfer);
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
}
