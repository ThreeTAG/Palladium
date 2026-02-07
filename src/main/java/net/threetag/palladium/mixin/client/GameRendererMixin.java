package net.threetag.palladium.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.threetag.palladium.config.PalladiumClientConfig;
import net.threetag.palladium.power.ability.ShaderEffectAbility;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public abstract class GameRendererMixin {

    @Shadow
    @Final
    private Minecraft minecraft;

    @Shadow
    public abstract void setPostEffect(Identifier postEffectId);

    @Shadow
    private @Nullable Identifier postEffectId;

    @Inject(method = "checkEntityPostEffect", at = @At("RETURN"))
    private void checkEntityPostEffect(Entity entity, CallbackInfo ci) {
        if (this.postEffectId == null && this.minecraft.player != null) {
            var shader = ShaderEffectAbility.get(this.minecraft.player);
            if (shader != null) {
                this.setPostEffect(shader);
            }
        }
    }

    @ModifyConstant(method = "getProjectionMatrix", constant = @Constant(floatValue = 0.05F))
    public float getProjectionMatrix(float original) {
        if (!PalladiumClientConfig.SCALE_CAMERA_FIX.getAsBoolean()) {
            return original;
        }

        LivingEntity entity = (LivingEntity) ((GameRenderer) (Object) this).getMainCamera().entity();
        float scale = (float) entity.getAttributeValue(Attributes.SCALE);

        return Math.clamp(original * scale, 0.005F, 0.05F);
    }

    @Inject(method = "bobView", at = @At("HEAD"), cancellable = true)
    private void bobView(PoseStack poseStack, float partialTicks, CallbackInfo ci) {
        if (PalladiumClientConfig.SCALE_CAMERA_FIX.getAsBoolean() && this.minecraft.getCameraEntity() instanceof AbstractClientPlayer player) {
            float scale = (float) player.getAttributeValue(Attributes.SCALE) * 2F;
            float scaleMultiplier = Math.min(scale, 1);
            var avatarState = player.avatarState();
            float walkDist = avatarState.getBackwardsInterpolatedWalkDistance(partialTicks);
            float bob = avatarState.getInterpolatedBob(partialTicks);

            poseStack.translate(Mth.sin(walkDist * (float) Math.PI) * scaleMultiplier * bob * 0.5F,
                    -Math.abs(Mth.cos(walkDist * (float) Math.PI) * scaleMultiplier * bob), 0.0F);
            poseStack.mulPose(Axis.ZP.rotationDegrees(Mth.sin(walkDist * (float) Math.PI) * bob * 3.0F));
            poseStack.mulPose(Axis.XP.rotationDegrees(Math.abs(Mth.cos(walkDist * (float) Math.PI - 0.2F) * bob) * 5.0F));

            ci.cancel();
        }
    }

}
