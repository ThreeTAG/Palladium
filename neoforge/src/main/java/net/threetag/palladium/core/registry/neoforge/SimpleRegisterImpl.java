package net.threetag.palladium.core.registry.neoforge;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.registries.RegisterEvent;
import net.threetag.palladium.Palladium;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

@EventBusSubscriber(modid = Palladium.MOD_ID)
public class SimpleRegisterImpl {

    private static final List<Entry<?>> ENTRIES = new ArrayList<>();

    public static <T> void register(ResourceKey<Registry<T>> registryResourceKey, ResourceLocation id, T object) {
        ENTRIES.add(new Entry<>(registryResourceKey, id, object));
    }

    @SubscribeEvent
    public static void register(RegisterEvent event) {
        ENTRIES.forEach(entry -> entry.register(event));
    }

    public record Entry<T>(ResourceKey<Registry<T>> registryResourceKey, ResourceLocation id, T object) implements Supplier<T> {

        public void register(RegisterEvent event) {
            event.register(this.registryResourceKey, id, this);
        }

        @Override
        public T get() {
            return this.object;
        }
    }

}
