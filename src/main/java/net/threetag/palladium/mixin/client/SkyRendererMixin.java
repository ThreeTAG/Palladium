package net.threetag.palladium.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.SkyRenderer;
import net.minecraft.world.level.MoonPhase;
import net.threetag.palladium.client.renderer.WatcherRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SkyRenderer.class)
public class SkyRendererMixin {

    @Inject(method = "<init>", at = @At("RETURN"))
    public void init(CallbackInfo ci) {
        WatcherRenderer.INSTANCE.init();
    }

    @Inject(method = "renderSunMoonAndStars", at = @At("HEAD"))
    public void renderSunMoonAndStars(
            PoseStack poseStack, float p_362201_, float p_362569_, float p_363542_, MoonPhase p_455415_, float p_468909_, float p_467714_, CallbackInfo ci
    ) {
        WatcherRenderer.INSTANCE.render(poseStack, WatcherRenderer.INSTANCE.getVisibility(Minecraft.getInstance().getDeltaTracker().getGameTimeDeltaTicks()));
    }

}
