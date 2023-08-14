package net.threetag.palladium.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ScreenEffectRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.power.ability.Abilities;
import net.threetag.palladium.power.ability.AbilityUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ScreenEffectRenderer.class)
public class ScreenEffectRendererMixin {

    @Inject(method = "renderTex", at = @At("HEAD"), cancellable = true)
    private static void preventInWallOverlayRendering(TextureAtlasSprite texture, PoseStack poseStack, CallbackInfo ci) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.cameraEntity instanceof LivingEntity living) {
            if (AbilityUtil.isTypeEnabled(living, Abilities.INTANGIBILITY.get())) {
                ci.cancel();
            }
        }
    }

}
