package net.threetag.palladium.power.holderfactory;

import dev.architectury.core.RegistryEntry;
import dev.architectury.registry.registries.Registrar;
import dev.architectury.registry.registries.Registries;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.power.provider.IPowerProvider;

import java.util.function.Consumer;

public abstract class PowerProviderFactory extends RegistryEntry<PowerProviderFactory> {

    public static final ResourceKey<Registry<PowerProviderFactory>> RESOURCE_KEY = ResourceKey.createRegistryKey(new ResourceLocation(Palladium.MOD_ID, "power_provider_factory"));
    public static final Registrar<PowerProviderFactory> REGISTRY = Registries.get(Palladium.MOD_ID).builder(RESOURCE_KEY.location(), new PowerProviderFactory[0]).build();

    public abstract void create(Consumer<IPowerProvider> consumer);

}
