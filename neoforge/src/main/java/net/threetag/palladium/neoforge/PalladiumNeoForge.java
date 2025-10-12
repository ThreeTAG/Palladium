package net.threetag.palladium.neoforge;

import dev.architectury.platform.Platform;
import dev.architectury.utils.Env;
import net.minecraft.core.registries.Registries;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.AddPackFindersEvent;
import net.neoforged.neoforge.registries.RegisterEvent;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.addonpack.AddonPackManager;
import net.threetag.palladium.client.PalladiumClient;

import java.util.Optional;
import java.util.function.Consumer;

@Mod(Palladium.MOD_ID)
@EventBusSubscriber(modid = Palladium.MOD_ID)
public final class PalladiumNeoForge {

    public PalladiumNeoForge() {
        Palladium.init();

        if (Platform.getEnvironment() == Env.CLIENT) {
            PalladiumClient.init();
        }
    }

    @SubscribeEvent
    public static void packFinder(AddPackFindersEvent e) {
        if (e.getPackType() != AddonPackManager.getPackType()) {
            e.addRepositorySource(AddonPackManager.getWrappedPackFinder(e.getPackType()));
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onRegister(RegisterEvent e) {
        if (e.getRegistryKey() == Registries.ATTRIBUTE) {
            AddonPackManager.initiateBasicLoaders();
        }

        AddonPackManager.initiateFor(e.getRegistryKey(), (registry, id, object) -> e.register(registry, id, () -> object));
    }

    public static Optional<IEventBus> getModEventBus(String modId) {
        return ModList.get().getModContainerById(modId)
                .map(ModContainer::getEventBus);
    }

    public static void whenModBusAvailable(String modId, Consumer<IEventBus> busConsumer) {
        IEventBus bus = getModEventBus(modId).orElseThrow(() -> new IllegalStateException("Mod '" + modId + "' is not available!"));
        busConsumer.accept(bus);
    }
}
