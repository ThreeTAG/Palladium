package net.threetag.palladium.power;

import net.minecraft.resources.ResourceLocation;
import net.threetag.palladium.power.ability.Abilities;
import net.threetag.palladium.power.ability.AbilityConfiguration;

import java.util.ArrayList;
import java.util.List;

public class Power {

    private final ResourceLocation id;
    private final List<AbilityConfiguration> abilities = new ArrayList<>();

    public Power(ResourceLocation id) {
        this.id = id;
        this.abilities.add(new AbilityConfiguration("test_1", Abilities.HEALING.get()));
    }

    public ResourceLocation getId() {
        return id;
    }

    public List<AbilityConfiguration> getAbilities() {
        return abilities;
    }
}
