package net.threetag.threecore.addonpacks.item;

import com.google.common.collect.Lists;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShieldItem;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.LazyLoadBase;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.threetag.threecore.abilities.AbilityGenerator;
import net.threetag.threecore.abilities.AbilityMap;
import net.threetag.threecore.abilities.IAbilityProvider;
import net.threetag.threecore.abilities.capability.ItemAbilityContainerProvider;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Supplier;

public class ShieldAbilityItem extends ShieldItem implements IAbilityProvider {

    private List<AbilityGenerator> abilityGenerators;
    private final int useDuration;
    private final LazyLoadBase<Ingredient> repairMaterial;

    public ShieldAbilityItem(Properties properties, int useDuration, Supplier<Ingredient> repairMaterial) {
        super(properties);
        this.useDuration = useDuration;
        this.repairMaterial = new LazyLoadBase<>(repairMaterial);
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return this.useDuration;
    }

    @Override
    public boolean getIsRepairable(ItemStack toRepair, ItemStack repairMaterial) {
        return this.repairMaterial.getValue().test(repairMaterial);
    }

    public ShieldAbilityItem setAbilities(List<AbilityGenerator> abilities) {
        this.abilityGenerators = abilities;
        return this;
    }

    public ShieldAbilityItem addAbility(AbilityGenerator abilityGenerator) {
        if (this.abilityGenerators == null)
            this.abilityGenerators = Lists.newArrayList();
        this.abilityGenerators.add(abilityGenerator);
        return this;
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundNBT nbt) {
        if (this.abilityGenerators != null && !this.abilityGenerators.isEmpty())
            return new ItemAbilityContainerProvider(stack);
        else
            return super.initCapabilities(stack, nbt);
    }

    @Override
    public AbilityMap getAbilities() {
        return new AbilityMap(this.abilityGenerators);
    }
}
