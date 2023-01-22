package net.threetag.palladium.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.world.entity.HumanoidArm;
import net.threetag.palladium.accessory.Accessory;
import net.threetag.palladium.client.model.animation.PalladiumAnimationRegistry;
import net.threetag.palladium.client.renderer.renderlayer.PackRenderLayerManager;
import net.threetag.palladium.entity.BodyPart;
import net.threetag.palladium.power.ability.ShrinkBodyOverlayAbility;
import net.threetag.palladium.util.RenderUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@SuppressWarnings("ConstantConditions")
@Mixin(PlayerRenderer.class)
public class PlayerRendererMixin {

    private float cachedHandShrink = 0F;

    @Inject(at = @At("RETURN"), method = "setupRotations(Lnet/minecraft/client/player/AbstractClientPlayer;Lcom/mojang/blaze3d/vertex/PoseStack;FFF)V")
    public void setupRotations(AbstractClientPlayer player, PoseStack poseStack, float ageInTicks, float rotationYaw, float partialTicks, CallbackInfo ci) {
        PlayerRenderer playerRenderer = (PlayerRenderer) (Object) this;
        PalladiumAnimationRegistry.setupRotations(playerRenderer, player, poseStack, ageInTicks, rotationYaw, partialTicks);
    }

    @Inject(at = @At("HEAD"), method = "renderHand")
    public void renderHandPre(PoseStack poseStack, MultiBufferSource buffer, int combinedLight, AbstractClientPlayer player, ModelPart rendererArm, ModelPart rendererArmwear, CallbackInfo ci) {
        PlayerRenderer playerRenderer = (PlayerRenderer) (Object) this;
        RenderUtil.REDIRECT_GET_BUFFER = true;
        PalladiumAnimationRegistry.SKIP_ANIMATIONS = true;

        PalladiumAnimationRegistry.applyFirstPersonAnimations(poseStack, player, playerRenderer.getModel(), rendererArm == playerRenderer.getModel().rightArm);

        if (playerRenderer.getModel() instanceof AgeableListModelInvoker invoker) {
            PalladiumAnimationRegistry.resetPoses(invoker.invokeHeadParts(), invoker.invokeBodyParts());
        }
    }

    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/model/PlayerModel;setupAnim(Lnet/minecraft/world/entity/LivingEntity;FFFFF)V", shift = At.Shift.AFTER), method = "renderHand")
    public void renderHandPreRender(PoseStack poseStack, MultiBufferSource buffer, int combinedLight, AbstractClientPlayer player, ModelPart rendererArm, ModelPart rendererArmwear, CallbackInfo ci) {
        PlayerRenderer playerRenderer = (PlayerRenderer) (Object) this;
        PalladiumAnimationRegistry.SKIP_ANIMATIONS = false;

        // Reset all, make them visible
        BodyPart.resetBodyParts(player, playerRenderer.getModel());

        // Make them invisible if specified
        for (BodyPart part : BodyPart.getHiddenBodyParts(player, true)) {
            part.setVisibility(playerRenderer.getModel(), false);
        }

        // Shrink Overlay
        float scale = ShrinkBodyOverlayAbility.getValue(player);

        if (scale != 0F) {
            float f = -0.1F * scale;
            this.cachedHandShrink = f;
            Vector3f vec = new Vector3f(f, f, f);
            rendererArmwear.offsetScale(vec);
        }
    }

    @Inject(at = @At("RETURN"), method = "renderHand")
    public void renderHandPost(PoseStack poseStack, MultiBufferSource buffer, int combinedLight, AbstractClientPlayer player, ModelPart rendererArm, ModelPart rendererArmwear, CallbackInfo ci) {
        PlayerRenderer playerRenderer = (PlayerRenderer) (Object) this;

        Accessory.getPlayerData(player).ifPresent(data -> data.getSlots().forEach((slot, accessories) -> {
            for (Accessory accessory : accessories) {
                if (accessory.isVisible(slot, player, true)) {
                    accessory.renderArm(rendererArm == playerRenderer.getModel().rightArm ? HumanoidArm.RIGHT : HumanoidArm.LEFT, player, playerRenderer, rendererArm, rendererArmwear, slot, poseStack, buffer, combinedLight);
                }
            }
        }));

        PackRenderLayerManager.forEachLayer(player, (context, layer) -> {
            layer.renderArm(context, rendererArm == playerRenderer.getModel().rightArm ? HumanoidArm.RIGHT : HumanoidArm.LEFT, playerRenderer, poseStack, buffer, combinedLight);
        });

        // Reset all, make them visible
        BodyPart.resetBodyParts(player, playerRenderer.getModel());

        RenderUtil.REDIRECT_GET_BUFFER = false;

        // Reset overlay shrink
        if (this.cachedHandShrink != 0F) {
            float f = -this.cachedHandShrink;
            this.cachedHandShrink = 0F;
            Vector3f vec = new Vector3f(f, f, f);
            rendererArmwear.offsetScale(vec);
        }
    }

}
