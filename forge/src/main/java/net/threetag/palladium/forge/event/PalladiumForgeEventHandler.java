package net.threetag.palladium.forge.event;

import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.event.PalladiumEvents;

@Mod.EventBusSubscriber(modid = Palladium.MOD_ID)
public class PalladiumForgeEventHandler {

    @SubscribeEvent
    public static void onLivingUpdate(LivingEvent.LivingUpdateEvent e) {
        PalladiumEvents.LIVING_UPDATE.invoker().tick(e.getEntityLiving());
    }

    @SubscribeEvent
    public static void onStartTracking(PlayerEvent.StartTracking e) {
        PalladiumEvents.START_TRACKING.invoker().startTracking(e.getPlayer(), e.getTarget());
    }

}
