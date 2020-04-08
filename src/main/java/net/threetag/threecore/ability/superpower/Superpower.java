package net.threetag.threecore.ability.superpower;

import net.threetag.threecore.ability.Ability;
import net.threetag.threecore.ability.AbilityGenerator;
import net.threetag.threecore.ability.AbilityMap;
import net.threetag.threecore.ability.IAbilityProvider;
import net.threetag.threecore.util.icon.IIcon;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

import java.util.Arrays;
import java.util.List;

public class Superpower implements IAbilityProvider {

    protected final ResourceLocation id;
    protected ITextComponent name;
    protected IIcon icon;
    protected List<AbilityGenerator> abilityGenerators;

    public Superpower(ResourceLocation id, ITextComponent name, IIcon icon, AbilityGenerator... abilityGenerators) {
        this.id = id;
        this.name = name;
        this.icon = icon;
        this.abilityGenerators = Arrays.asList(abilityGenerators);
    }

    public Superpower(ResourceLocation id, ITextComponent name, IIcon icon, List<AbilityGenerator> abilityGenerators) {
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
            Ability ability = a.create();
            ability.getAdditionalData().putString("Superpower", this.getId().toString());
            abilityMap.put(a.key, ability);
        });
        return abilityMap;
    }
}
