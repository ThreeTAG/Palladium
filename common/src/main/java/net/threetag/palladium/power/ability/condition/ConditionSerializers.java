package net.threetag.palladium.power.ability.condition;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.threetag.palladium.Palladium;

public class ConditionSerializers {

    public static final DeferredRegister<ConditionSerializer> CONDITION_SERIALIZERS = DeferredRegister.create(Palladium.MOD_ID, ConditionSerializer.RESOURCE_KEY);

    public static final RegistrySupplier<ConditionSerializer> NOT = CONDITION_SERIALIZERS.register("not", NotCondition.Serializer::new);
    public static final RegistrySupplier<ConditionSerializer> OR = CONDITION_SERIALIZERS.register("or", OrCondition.Serializer::new);
    public static final RegistrySupplier<ConditionSerializer> ABILITY_UNLOCKED = CONDITION_SERIALIZERS.register("ability_unlocked", AbilityUnlockedCondition.Serializer::new);
    public static final RegistrySupplier<ConditionSerializer> ABILITY_ENABLED = CONDITION_SERIALIZERS.register("ability_enabled", AbilityEnabledCondition.Serializer::new);
    public static final RegistrySupplier<ConditionSerializer> ACTION = CONDITION_SERIALIZERS.register("action", ActionCondition.Serializer::new);
    public static final RegistrySupplier<ConditionSerializer> TOGGLE = CONDITION_SERIALIZERS.register("toggle", ToggleCondition.Serializer::new);
    public static final RegistrySupplier<ConditionSerializer> HELD = CONDITION_SERIALIZERS.register("held", HeldCondition.Serializer::new);
    public static final RegistrySupplier<ConditionSerializer> HEALTH = CONDITION_SERIALIZERS.register("health", HealthCondition.Serializer::new);

}
