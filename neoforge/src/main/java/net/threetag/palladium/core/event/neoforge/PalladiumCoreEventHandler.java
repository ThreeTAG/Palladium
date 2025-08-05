package net.threetag.palladium.core.event.neoforge;

import net.minecraft.network.chat.Component;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.OnDatapackSyncEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.core.event.PalladiumEntityEvents;
import net.threetag.palladium.core.event.PalladiumLifecycleEvents;
import net.threetag.palladium.core.event.PalladiumPlayerEvents;

import java.util.concurrent.atomic.AtomicReference;

@EventBusSubscriber(modid = Palladium.MOD_ID)
public class PalladiumCoreEventHandler {

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void entityTickPre(EntityTickEvent.Pre e) {
        PalladiumEntityEvents.TICK_PRE.invoker().entityTick(e.getEntity());
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void entityTickPost(EntityTickEvent.Post e) {
        PalladiumEntityEvents.TICK_POST.invoker().entityTick(e.getEntity());
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void onDataPackSync(OnDatapackSyncEvent e) {
        PalladiumLifecycleEvents.DATA_PACK_SYNC.invoker().onDataPackSync(e.getPlayerList(), e.getPlayer());
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void startTracking(PlayerEvent.StartTracking e) {
        PalladiumPlayerEvents.START_TRACKING.invoker().playerTracking(e.getEntity(), e.getTarget());
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void stopTracking(PlayerEvent.StopTracking e) {
        PalladiumPlayerEvents.STOP_TRACKING.invoker().playerTracking(e.getEntity(), e.getTarget());
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void playerChangedDimension(PlayerEvent.NameFormat e) {
        AtomicReference<Component> name = new AtomicReference<>(e.getDisplayname());
        PalladiumPlayerEvents.NAME_FORMAT.invoker().playerNameFormat(e.getEntity(), e.getUsername(), name);
        e.setDisplayname(name.get());
    }

}
