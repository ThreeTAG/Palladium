package net.threetag.palladium.mixin.client.fabric;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.buffers.GpuBuffer;
import net.minecraft.client.Camera;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.fog.FogData;
import net.minecraft.client.renderer.fog.FogRenderer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.material.FogType;
import net.threetag.palladium.power.ability.AbilitySerializers;
import net.threetag.palladium.power.ability.AbilityUtil;
import net.threetag.palladium.power.ability.IntangibilityAbility;
import org.joml.Vector4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(FogRenderer.class)
public class FogRendererMixin {

    @Inject(method = "computeFogColor", at = @At("HEAD"), cancellable = true)
    private static void computeFogColor(Camera camera, float partialTick, ClientLevel level, int renderDistance, float darkenWorldAmount, boolean isFoggy, CallbackInfoReturnable<Vector4f> cir) {
        if (camera.getEntity() instanceof LivingEntity living
                && AbilityUtil.isTypeEnabled(living, AbilitySerializers.INTANGIBILITY.get())
                && IntangibilityAbility.getInWallBlockState(living) != null) {
            cir.setReturnValue(new Vector4f(0F, 0F, 0F, 1F));
        }
    }

    @Inject(method = "setupFog",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/fog/FogRenderer;updateBuffer(Ljava/nio/ByteBuffer;ILorg/joml/Vector4f;FFFFFF)V")
    )
    private static void setupFog(Camera camera, int renderDistance, boolean isFoggy, DeltaTracker deltaTracker, float darkenWorldAmount, ClientLevel level, CallbackInfoReturnable<Vector4f> cir, @Local FogData fogData) {
        if (camera.getEntity() instanceof LivingEntity living
                && AbilityUtil.isTypeEnabled(living, AbilitySerializers.INTANGIBILITY.get())
                && IntangibilityAbility.getInWallBlockState(living) != null) {
            fogData.environmentalStart = 1F;
            fogData.environmentalEnd = 5F;
        }
    }

}