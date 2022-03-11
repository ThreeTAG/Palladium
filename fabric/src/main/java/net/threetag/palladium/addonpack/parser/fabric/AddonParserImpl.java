package net.threetag.palladium.addonpack.parser.fabric;

import dev.architectury.registry.registries.DeferredRegister;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.threetag.palladium.addonpack.builder.AddonBuilder;

import java.util.HashMap;
import java.util.Map;

public class AddonParserImpl {

    public static final Map<String, Map<ResourceKey<?>, DeferredRegister<?>>> DEFERRED_REGISTERS = new HashMap<>();

    @SuppressWarnings("unchecked")
    public static <T> void register(ResourceKey<Registry<T>> resourceKey, AddonBuilder<T> builder) {
        Map<ResourceKey<?>, DeferredRegister<?>> map1 = DEFERRED_REGISTERS.computeIfAbsent(builder.getId().getNamespace(), (ns) -> new HashMap<>());
        DeferredRegister<T> register = (DeferredRegister<T>) map1.computeIfAbsent(resourceKey, key -> {
            DeferredRegister<T> r = DeferredRegister.create(builder.getId().getNamespace(), resourceKey);
            r.register();
            return r;
        });
        register.register(builder.getId().getPath(), builder);
    }
}
