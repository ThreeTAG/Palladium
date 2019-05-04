package com.threetag.threecore.abilities;

import com.google.gson.JsonObject;

public class AbilityGenerator {

    public final String key;
    public final AbilityType abilityType;
    public final JsonObject jsonObject;

    public AbilityGenerator(String key, AbilityType abilityType, JsonObject jsonObject) {
        this.key = key;
        this.abilityType = abilityType;
        this.jsonObject = jsonObject;
    }

    public Ability create() {
        Ability ability = this.abilityType.create();
        ability.readFromJson(this.jsonObject);
        return ability;
    }

}
