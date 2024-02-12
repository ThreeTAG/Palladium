package net.threetag.palladium.addonpack.parser.fabric;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.threetag.palladium.addonpack.builder.AddonBuilder;

public class AddonParserImpl {

    @SuppressWarnings({"ConstantConditions", "unchecked"})
    public static <T> void register(ResourceKey<? extends Registry<T>> key, AddonBuilder<T, ?> builder) {
        var registry = (Registry<T>) BuiltInRegistries.REGISTRY.get(key.location());
        Registry.register(registry, builder.getId(), builder.get());
    }

}
