package net.threetag.palladium.power.ability.condition;

import com.google.gson.JsonObject;
import dev.architectury.registry.registries.Registrar;
import dev.architectury.registry.registries.Registries;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.threetag.palladium.Palladium;

public abstract class ConditionSerializer {

    public static final ResourceKey<Registry<ConditionSerializer>> RESOURCE_KEY = ResourceKey.createRegistryKey(new ResourceLocation(Palladium.MOD_ID, "condition_serializers"));
    public static final Registrar<ConditionSerializer> REGISTRY = Registries.get(Palladium.MOD_ID).builder(RESOURCE_KEY.location(), new ConditionSerializer[0]).build();

    // TODO make with threedata for documentation later on
    public abstract Condition fromJSON(JsonObject json);

}
