package net.threetag.threecore.item;

import com.google.common.collect.Lists;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.HoeItem;
import net.minecraft.item.IItemTier;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fml.DistExecutor;
import net.threetag.threecore.ability.Ability;
import net.threetag.threecore.ability.AbilityMap;
import net.threetag.threecore.ability.IAbilityProvider;
import net.threetag.threecore.capability.ItemAbilityContainerProvider;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Supplier;

public class HoeAbilityItem extends HoeItem implements IAbilityProvider {

    private List<Supplier<Ability>> abilityGenerators;
    private List<ITextComponent> description;

    public HoeAbilityItem(IItemTier itemTier, float attackSpeed, Properties properties) {
        super(itemTier, attackSpeed, properties);
    }

    public HoeAbilityItem setAbilities(List<Supplier<Ability>> abilities) {
        this.abilityGenerators = abilities;
        return this;
    }

    public HoeAbilityItem addAbility(Supplier<Ability> abilityGenerator) {
        if (this.abilityGenerators == null)
            this.abilityGenerators = Lists.newArrayList();
        this.abilityGenerators.add(abilityGenerator);
        return this;
    }

    public HoeAbilityItem setDescription(List<ITextComponent> description) {
        DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> this.description = description);
        return this;
    }

    public HoeAbilityItem addDescriptionLine(ITextComponent line) {
        DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> {
            if (this.description == null)
                this.description = Lists.newArrayList();
            this.description.add(line);
        });
        return this;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        if (this.description != null)
            tooltip.addAll(this.description);
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundNBT nbt) {
        return new ItemAbilityContainerProvider(stack);
    }

    @Override
    public AbilityMap getAbilities() {
        return new AbilityMap(this.abilityGenerators);
    }

}
