package net.threetag.palladium.addonpack.parser.forge;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.addonpack.builder.AddonBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Mod.EventBusSubscriber(modid = Palladium.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class AddonParserImpl {

    private static final Map<Object, List<AddonBuilder<?>>> BUILDERS = new HashMap<>();

    public static <T> void register(ResourceKey<Registry<T>> resourceKey, AddonBuilder<T> builder) {
        List<AddonBuilder<?>> builders = BUILDERS.computeIfAbsent(resourceKey, (k) -> new ArrayList<>());
        builders.add(builder);
    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> e) {
        for (AddonBuilder<?> builder : BUILDERS.get(Registry.ITEM_REGISTRY)) {
            e.getRegistry().register(((Item) builder.get()).setRegistryName(builder.getId()));
        }
        BUILDERS.remove(Registry.ITEM_REGISTRY);
    }

}
