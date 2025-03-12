package net.threetag.palladium.event.neoforge;

import com.mojang.blaze3d.shaders.FogShape;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ViewportEvent;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.power.ability.AbilitySerializers;
import net.threetag.palladium.power.ability.AbilityUtil;
import net.threetag.palladium.power.ability.IntangibilityAbility;

@EventBusSubscriber(modid = Palladium.MOD_ID, bus = EventBusSubscriber.Bus.GAME, value = Dist.CLIENT)
public class NeoForgeClientAbilityEventHandler {

    @SubscribeEvent
    public static void computeFogColors(ViewportEvent.ComputeFogColor e) {
        if (e.getCamera().getEntity() instanceof LivingEntity living
                && AbilityUtil.isTypeEnabled(living, AbilitySerializers.INTANGIBILITY.get())
                && IntangibilityAbility.getInWallBlockState(living) != null) {
            e.setRed(0F);
            e.setGreen(0F);
            e.setBlue(0F);
        }
    }

    @SubscribeEvent
    public static void renderFog(ViewportEvent.RenderFog e) {
        if (e.getCamera().getEntity() instanceof LivingEntity living
                && AbilityUtil.isTypeEnabled(living, AbilitySerializers.INTANGIBILITY.get())
                && IntangibilityAbility.getInWallBlockState(living) != null) {
            e.setFogShape(FogShape.SPHERE);
            e.setFarPlaneDistance(5F);
            e.setNearPlaneDistance(1F);
        }
    }

}
