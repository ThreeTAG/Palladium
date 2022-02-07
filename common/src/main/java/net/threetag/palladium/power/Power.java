package net.threetag.palladium.power;

import com.google.gson.JsonObject;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.threetag.palladium.power.ability.Abilities;
import net.threetag.palladium.power.ability.AbilityConfiguration;

import java.util.ArrayList;
import java.util.List;

public class Power {

    private final ResourceLocation id;
    private final Component name;
    private final List<AbilityConfiguration> abilities = new ArrayList<>();

    public Power(ResourceLocation id, Component name) {
        this.id = id;
        this.name = name;
        this.abilities.add(new AbilityConfiguration("test_1", Abilities.HEALING.get()));
    }

    public Power addAbility(AbilityConfiguration configuration) {
        this.abilities.add(configuration);
        return this;
    }

    public ResourceLocation getId() {
        return id;
    }

    public List<AbilityConfiguration> getAbilities() {
        return abilities;
    }

    public static Power fromJSON(ResourceLocation id, JsonObject json) {
        Component name = Component.Serializer.fromJson(json.get("name"));
        Power power = new Power(id, name);

        if (GsonHelper.isValidNode(json, "abilities")) {
            JsonObject abilities = GsonHelper.getAsJsonObject(json, "abilities");

            for (String key : abilities.keySet()) {
                power.addAbility(AbilityConfiguration.fromJSON(key, GsonHelper.getAsJsonObject(abilities, key)));
            }
        }

        return power;
    }

}
