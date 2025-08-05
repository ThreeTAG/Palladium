package net.threetag.palladium.core.registry.neoforge;

import net.minecraft.core.Registry;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.registries.NewRegistryEvent;
import net.neoforged.neoforge.registries.RegistryBuilder;
import net.threetag.palladium.Palladium;

import java.util.ArrayList;
import java.util.List;

@EventBusSubscriber(modid = Palladium.MOD_ID)
public class RegistryBuilderImpl {

    private static final List<Registry<?>> REGISTRIES = new ArrayList<>();

    public static <T> Registry<T> createRegistry(net.threetag.palladium.core.registry.RegistryBuilder<T> registryBuilder) {
        RegistryBuilder<T> neoBuilder = new RegistryBuilder<>(registryBuilder.getResourceKey()).sync(registryBuilder.isSynced());

        if (registryBuilder.getDefaultKey() != null) {
            neoBuilder.defaultKey(registryBuilder.getDefaultKey());
        }

        var registry = neoBuilder.create();
        REGISTRIES.add(registry);
        return registry;
    }

    @SubscribeEvent
    public static void registerRegistries(NewRegistryEvent event) {
        for (Registry<?> registry : REGISTRIES) {
            event.register(registry);
        }
    }

}
