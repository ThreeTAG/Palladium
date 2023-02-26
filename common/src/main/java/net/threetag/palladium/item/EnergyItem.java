package net.threetag.palladium.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public abstract class EnergyItem extends Item {

    private final int capacity, maxInput, maxOutput;

    public EnergyItem(Properties properties, int capacity, int maxInput, int maxOutput) {
        super(properties);
        this.capacity = capacity;
        this.maxInput = maxInput;
        this.maxOutput = maxOutput;
    }

    public int getEnergyCapacity(ItemStack stack) {
        return capacity;
    }

    public int getEnergyMaxInput(ItemStack stack) {
        return maxInput;
    }

    public int getEnergyMaxOutput(ItemStack stack) {
        return maxOutput;
    }

}
