package net.threetag.palladium.addonpack.parser.forge;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.RegisterEvent;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.addonpack.builder.AddonBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Mod.EventBusSubscriber(modid = Palladium.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class AddonParserImpl {

    private static final Map<ResourceKey<? extends Registry<?>>, RegistryEntries<?>> OBJECTS = new HashMap<>();

    @SuppressWarnings("unchecked")
    public static <T> void register(ResourceKey<? extends Registry<T>> key, AddonBuilder<T> builder) {
        RegistryEntries<T> entries = OBJECTS.containsKey(key) ? (RegistryEntries<T>) OBJECTS.get(key) : new RegistryEntries<>(key);
        entries.add(builder);
        OBJECTS.put(key, entries);
    }

    @SubscribeEvent
    public static void register(RegisterEvent e) {
        OBJECTS.forEach((resourceKey, entries) -> {
            entries.register(e);
        });
    }

    public static class RegistryEntries<T> {

        private final ResourceKey<? extends Registry<T>> key;
        private final List<AddonBuilder<T>> addonBuilders = new ArrayList<>();

        public RegistryEntries(ResourceKey<? extends Registry<T>> key) {
            this.key = key;
        }

        public void add(AddonBuilder<T> builder) {
            this.addonBuilders.add(builder);
        }

        public void register(RegisterEvent e) {
            for (AddonBuilder<T> addonBuilder : this.addonBuilders) {
                e.register(this.key, addonBuilder.getId(), addonBuilder);
            }
        }
    }

}
