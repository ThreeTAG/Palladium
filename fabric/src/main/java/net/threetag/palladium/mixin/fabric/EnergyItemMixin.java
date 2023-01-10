package net.threetag.palladium.mixin.fabric;

import net.minecraft.world.item.ItemStack;
import net.threetag.palladium.item.EnergyItem;
import org.spongepowered.asm.mixin.Mixin;
import team.reborn.energy.api.base.SimpleEnergyItem;

@SuppressWarnings("DataFlowIssue")
@Mixin(EnergyItem.class)
public class EnergyItemMixin implements SimpleEnergyItem {

    @Override
    public long getEnergyCapacity(ItemStack stack) {
        return ((EnergyItem) (Object) this).getEnergyCapacity(stack);
    }

    @Override
    public long getEnergyMaxInput(ItemStack stack) {
        return ((EnergyItem) (Object) this).getEnergyMaxInput(stack);
    }

    @Override
    public long getEnergyMaxOutput(ItemStack stack) {
        return ((EnergyItem) (Object) this).getEnergyMaxOutput(stack);
    }
}
