package net.threetag.palladium.forge;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.client.renderer.entity.EffectEntityRenderer;
import net.threetag.palladium.entity.PalladiumEntityTypes;
import net.threetag.palladium.event.PalladiumEvents;

@Mod.EventBusSubscriber(modid = Palladium.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class PalladiumForgeEventHandler {

    @SubscribeEvent
    public static void onLivingUpdate(LivingEvent.LivingUpdateEvent e) {
        PalladiumEvents.LIVING_UPDATE.invoker().tick(e.getEntityLiving());
    }

    @SubscribeEvent
    public static void onStartTracking(PlayerEvent.StartTracking e) {
        PalladiumEvents.START_TRACKING.invoker().startTracking(e.getPlayer(), e.getTarget());
    }

    @Mod.EventBusSubscriber(modid = Palladium.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ModBus {

        @OnlyIn(Dist.CLIENT)
        @SubscribeEvent
        public static void onEntityRendererRegister(EntityRenderersEvent.RegisterRenderers e) {
            e.registerEntityRenderer(PalladiumEntityTypes.EFFECT.get(), EffectEntityRenderer::new);
        }

    }

}
