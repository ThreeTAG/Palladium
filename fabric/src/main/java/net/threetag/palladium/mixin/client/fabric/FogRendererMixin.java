package net.threetag.palladium.mixin.client.fabric;

import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.FogParameters;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.power.ability.AbilitySerializers;
import net.threetag.palladium.power.ability.AbilityUtil;
import net.threetag.palladium.power.ability.IntangibilityAbility;
import org.joml.Vector4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FogRenderer.class)
public class FogRendererMixin {

    @Inject(method = "computeFogColor", at = @At("HEAD"), cancellable = true)
    private static void computeFogColor(Camera camera, float partialTick, ClientLevel level, int renderDistance, float darkenWorldAmount, CallbackInfoReturnable<Vector4f> cir) {
        if (camera.getEntity() instanceof LivingEntity living
                && AbilityUtil.isTypeEnabled(living, AbilitySerializers.INTANGIBILITY.get())
                && IntangibilityAbility.getInWallBlockState(living) != null) {
            cir.setReturnValue(new Vector4f(0F, 0F, 0F, 1F));
        }
    }

    @Inject(method = "setupFog", at = @At("RETURN"), cancellable = true)
    private static void setupFog(Camera camera, FogRenderer.FogMode fogMode, Vector4f fogColor, float renderDistance, boolean isFoggy, float partialTick, CallbackInfoReturnable<FogParameters> cir) {
        if (camera.getEntity() instanceof LivingEntity living
                && AbilityUtil.isTypeEnabled(living, AbilitySerializers.INTANGIBILITY.get())
                && IntangibilityAbility.getInWallBlockState(living) != null) {
            var fog = cir.getReturnValue();
            cir.setReturnValue(new FogParameters(1F, 5F, fog.shape(), fog.red(), fog.green(), fog.blue(), fog.alpha()));
        }
    }

}