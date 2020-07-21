package net.threetag.threecore.ability.superpower;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.threetag.threecore.ability.Ability;
import net.threetag.threecore.ability.AbilityMap;
import net.threetag.threecore.ability.IAbilityProvider;
import net.threetag.threecore.util.icon.IIcon;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

public class Superpower implements IAbilityProvider {

    protected final ResourceLocation id;
    protected ITextComponent name;
    protected IIcon icon;
    protected List<Supplier<Ability>> abilityGenerators;

    public Superpower(ResourceLocation id, ITextComponent name, IIcon icon, Supplier<Ability>... abilityGenerators) {
        this.id = id;
        this.name = name;
        this.icon = icon;
        this.abilityGenerators = Arrays.asList(abilityGenerators);
    }

    public Superpower(ResourceLocation id, ITextComponent name, IIcon icon, List<Supplier<Ability>> abilityGenerators) {
        this.id = id;
        this.name = name;
        this.icon = icon;
        this.abilityGenerators = abilityGenerators;
    }

    public ResourceLocation getId() {
        return id;
    }

    public ITextComponent getName() {
        return name;
    }

    public IIcon getIcon() {
        return icon;
    }

    @Override
    public AbilityMap getAbilities() {
        AbilityMap abilityMap = new AbilityMap();
        this.abilityGenerators.forEach(a -> {
            Ability ability = a.get();
            CompoundNBT nbt = ability.getAdditionalData();
            nbt.putString("Superpower", this.getId().toString());
            ability.setAdditionalData(nbt);
            abilityMap.put(ability.getId(), ability);
        });
        return abilityMap;
    }
}
