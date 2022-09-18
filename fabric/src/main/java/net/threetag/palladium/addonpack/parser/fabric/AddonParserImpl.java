package net.threetag.palladium.addonpack.parser.fabric;

import com.google.common.base.MoreObjects;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.threetag.palladium.addonpack.builder.AddonBuilder;

public class AddonParserImpl {

    @SuppressWarnings({"unchecked", "ConstantConditions"})
    public static <T> void register(ResourceKey<? extends Registry<T>> key, AddonBuilder<T> builder) {
        System.out.println("HALLO " + key.location().toString());

        for (ResourceLocation objects : Registry.REGISTRY.keySet()) {
            System.out.println(" - " + objects.toString());
        }

        var registry = (Registry<T>) MoreObjects.firstNonNull(Registry.REGISTRY.get(key.location()), BuiltinRegistries.REGISTRY.get(key.location()));
        Registry.register(registry, builder.getId(), builder.get());
    }

}
