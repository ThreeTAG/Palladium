package net.threetag.palladium.entity;

import net.minecraft.core.Registry;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.threetag.palladium.Palladium;
import net.threetag.palladiumcore.registry.DeferredRegister;
import net.threetag.palladiumcore.registry.EntityAttributeRegistry;
import net.threetag.palladiumcore.registry.RegistrySupplier;

public class PalladiumAttributes {

    public static final DeferredRegister<Attribute> ATTRIBUTES = DeferredRegister.create(Palladium.MOD_ID, Registry.ATTRIBUTE_REGISTRY);

    public static final RegistrySupplier<Attribute> FLIGHT_SPEED = ATTRIBUTES.register("flight_speed", () -> new RangedAttribute("palladium.flight_speed", 0D, 0D, 32D).setSyncable(true));
    public static final RegistrySupplier<Attribute> LEVITATION_SPEED = ATTRIBUTES.register("levitation_speed", () -> new RangedAttribute("palladium.levitation_speed", 0D, 0D, 32D).setSyncable(true));
    public static final RegistrySupplier<Attribute> JETPACK_FLIGHT_SPEED = ATTRIBUTES.register("jetpack_flight_speed", () -> new RangedAttribute("palladium.jetpack_flight_speed", 0D, 0D, 32D).setSyncable(true));
    public static final RegistrySupplier<Attribute> HOVERING = ATTRIBUTES.register("hovering", () -> new RangedAttribute("palladium.hovering", 0D, 0D, 1D).setSyncable(true));

    public static void init() {
        EntityAttributeRegistry.registerModification(() -> EntityType.PLAYER, FLIGHT_SPEED);
        EntityAttributeRegistry.registerModification(() -> EntityType.PLAYER, LEVITATION_SPEED);
        EntityAttributeRegistry.registerModification(() -> EntityType.PLAYER, JETPACK_FLIGHT_SPEED);
        EntityAttributeRegistry.registerModification(() -> EntityType.PLAYER, HOVERING);
    }
}
