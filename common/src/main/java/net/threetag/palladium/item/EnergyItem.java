package net.threetag.palladium.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.energy.EnergyHelper;
import net.threetag.palladiumcore.registry.client.ItemPropertyRegistry;

public abstract class EnergyItem extends Item {

    private final int capacity, maxInput, maxOutput;

    public EnergyItem(Properties properties, int capacity, int maxInput, int maxOutput) {
        super(properties);
        this.capacity = capacity;
        this.maxInput = maxInput;
        this.maxOutput = maxOutput;
        ItemPropertyRegistry.register(this, Palladium.id("energy"), (itemStack, clientLevel, livingEntity, i) -> {
            var storage = EnergyHelper.getFromItemStack(itemStack);
            return storage.map(energyStorage -> Math.round(13F * energyStorage.getEnergyAmount() / (float) energyStorage.getEnergyCapacity())).orElse(0);
        });
        ItemPropertyRegistry.register(this, Palladium.id("charged"), (itemStack, clientLevel, livingEntity, i) -> {
            return itemStack.getOrCreateTag().getInt("energy") > 0 ? 1F : 0F;
        });
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
