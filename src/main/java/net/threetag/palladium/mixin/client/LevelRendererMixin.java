package net.threetag.palladium.mixin.client;

import com.mojang.blaze3d.buffers.GpuBufferSlice;
import com.mojang.blaze3d.framegraph.FrameGraphBuilder;
import net.minecraft.client.Camera;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.power.ability.AbilitySerializers;
import net.threetag.palladium.power.ability.AbilityUtil;
import net.threetag.palladium.power.ability.IntangibilityAbility;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LevelRenderer.class)
public class LevelRendererMixin {

    @Inject(method = "addSkyPass", at = @At("HEAD"), cancellable = true)
    private void skipSkyRenderingForPhasingBlindness(FrameGraphBuilder frameGraphBuilder, Camera camera, GpuBufferSlice shaderFog, CallbackInfo ci) {
        if (camera.getEntity() instanceof LivingEntity living) {
            if (AbilityUtil.isTypeEnabled(living, AbilitySerializers.INTANGIBILITY.get())) {
                if (IntangibilityAbility.getInWallBlockState(living) != null) {
                    ci.cancel();
                }
            }
        }
    }
}
