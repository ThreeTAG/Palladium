package net.threetag.palladium.power.provider;

import dev.architectury.core.RegistryEntry;
import dev.architectury.registry.registries.Registrar;
import dev.architectury.registry.registries.Registries;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.power.IPowerHandler;

public abstract class PowerProvider extends RegistryEntry<PowerProvider> {

    public static final ResourceKey<Registry<PowerProvider>> RESOURCE_KEY = ResourceKey.createRegistryKey(new ResourceLocation(Palladium.MOD_ID, "power_providers"));
    public static final Registrar<PowerProvider> REGISTRY = Registries.get(Palladium.MOD_ID).builder(RESOURCE_KEY.location(), new PowerProvider[0]).build();

    public abstract void providePowers(LivingEntity entity, IPowerHandler handler);

}
