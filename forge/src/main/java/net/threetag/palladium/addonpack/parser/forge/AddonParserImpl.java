package net.threetag.palladium.addonpack.parser.forge;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryManager;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.addonpack.builder.AddonBuilder;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings({"unchecked", "rawtypes", "UnnecessaryLocalVariable"})
@Mod.EventBusSubscriber(modid = Palladium.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class AddonParserImpl {

    public static IEventBus EVENT_BUS;
    public static final Map<String, Map<ResourceKey<?>, DeferredRegister>> DEFERRED_REGISTERS = new HashMap<>();

    public static <T> void register(ResourceKey<Registry<T>> resourceKey, AddonBuilder<T> builder) {
        Map<ResourceKey<?>, DeferredRegister> map1 = DEFERRED_REGISTERS.computeIfAbsent(builder.getId().getNamespace(), (ns) -> new HashMap<>());
        DeferredRegister register = map1.computeIfAbsent(resourceKey, key -> {
            ResourceKey key1 = key;
            DeferredRegister r = DeferredRegister.create(RegistryManager.ACTIVE.getRegistry(key1), builder.getId().getNamespace());
            r.register(EVENT_BUS);
            return r;
        });
        register.register(builder.getId().getPath(), builder);
    }

}
