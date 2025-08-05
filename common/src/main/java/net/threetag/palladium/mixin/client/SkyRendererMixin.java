package net.threetag.palladium.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.SkyRenderer;
import net.threetag.palladium.client.renderer.WatcherRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SkyRenderer.class)
public class SkyRendererMixin {

    @Inject(method = "renderSunMoonAndStars", at = @At("RETURN"))
    public void renderSunMoonAndStars(
            PoseStack poseStack, MultiBufferSource.BufferSource bufferSource, float timeOfDay, int moonPhase, float rainLevel, float starBrightness, CallbackInfo ci
    ) {
        WatcherRenderer.INSTANCE.render(poseStack, bufferSource, WatcherRenderer.INSTANCE.getVisibility(Minecraft.getInstance().getDeltaTracker().getGameTimeDeltaTicks()));
    }

}
