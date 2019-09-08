package net.threetag.threecore.abilities.superpower;

import net.threetag.threecore.abilities.Ability;
import net.threetag.threecore.abilities.AbilityGenerator;
import net.threetag.threecore.abilities.AbilityMap;
import net.threetag.threecore.abilities.IAbilityProvider;
import net.threetag.threecore.util.render.IIcon;
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
            ability.getAdditionalData().putBoolean("IsFromSuperpower", true);
            abilityMap.put(a.key, ability);
        });
        return abilityMap;
    }
}
