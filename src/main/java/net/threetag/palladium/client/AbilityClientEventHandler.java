package net.threetag.palladium.client;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderHandEvent;
import net.neoforged.neoforge.client.event.RenderLivingEvent;
import net.neoforged.neoforge.client.event.ViewportEvent;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.client.renderer.entity.state.PalladiumRenderStateKeys;
import net.threetag.palladium.power.ability.AbilitySerializers;
import net.threetag.palladium.power.ability.AbilityUtil;
import net.threetag.palladium.power.ability.IntangibilityAbility;

@EventBusSubscriber(modid = Palladium.MOD_ID, value = Dist.CLIENT)
public class AbilityClientEventHandler {

    public static float OVERRIDDEN_OPACITY = 1F;

    @SubscribeEvent
    static void preRenderLiving(RenderLivingEvent.Pre<?, ?, ?> e) {
        float opacity = e.getRenderState().getRenderDataOrDefault(PalladiumRenderStateKeys.OPACITY, 1F);

        if (opacity <= 0F) {
            e.setCanceled(true);
            resetOpacityOverride();
        } else {
            OVERRIDDEN_OPACITY = opacity;
        }
    }

    @SubscribeEvent
    static void postRenderLiving(RenderLivingEvent.Post<?, ?, ?> e) {
        resetOpacityOverride();
    }

    @SubscribeEvent
    static void renderHand(RenderHandEvent e) {
        float opacity = 1F - AbilityUtil.getHighestAnimationTimerProgress(Minecraft.getInstance().player, AbilitySerializers.INVISIBILITY.get(), e.getPartialTick());

        if (opacity <= 0F) {
            e.setCanceled(true);
            resetOpacityOverride();
        } else {
            OVERRIDDEN_OPACITY = opacity;
        }
    }

    public static void resetOpacityOverride() {
        OVERRIDDEN_OPACITY = 1F;
    }

    @SubscribeEvent
    static void renderFog(ViewportEvent.RenderFog e) {
        if (e.getCamera().getEntity() instanceof LivingEntity living) {
            if (AbilityUtil.isTypeEnabled(living, AbilitySerializers.INTANGIBILITY.get())) {
                if (IntangibilityAbility.getInWallBlockState(living) != null) {
                    e.setFarPlaneDistance(5F);
                    e.setNearPlaneDistance(1F);
                }
            }
        }
    }

    @SubscribeEvent
    static void fogColor(ViewportEvent.ComputeFogColor e) {
        if (e.getCamera().getEntity() instanceof LivingEntity living) {
            if (AbilityUtil.isTypeEnabled(living, AbilitySerializers.INTANGIBILITY.get())) {
                if (IntangibilityAbility.getInWallBlockState(living) != null) {
                    e.setRed(0F);
                    e.setGreen(0F);
                    e.setBlue(0F);
                }
            }
        }
    }

}
