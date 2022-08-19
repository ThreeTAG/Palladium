package net.threetag.palladium.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.threetag.palladium.client.model.animation.HumanoidAnimationsManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@SuppressWarnings("ConstantConditions")
@Mixin(PlayerRenderer.class)
public class PlayerRendererMixin {

    @Inject(at = @At("RETURN"), method = "setupRotations(Lnet/minecraft/client/player/AbstractClientPlayer;Lcom/mojang/blaze3d/vertex/PoseStack;FFF)V")
    public void setupRotations(AbstractClientPlayer player, PoseStack poseStack, float ageInTicks, float rotationYaw, float partialTicks, CallbackInfo ci) {
        PlayerRenderer playerRenderer = (PlayerRenderer) (Object) this;
        HumanoidAnimationsManager.setupRotations(playerRenderer, player, poseStack, ageInTicks, rotationYaw, partialTicks);
    }

    @Inject(at = @At("HEAD"), method = "renderHand")
    public void renderHand(PoseStack matrixStack, MultiBufferSource buffer, int combinedLight, AbstractClientPlayer player, ModelPart rendererArm, ModelPart rendererArmwear, CallbackInfo ci) {
        PlayerRenderer playerRenderer = (PlayerRenderer) (Object) this;

        if (playerRenderer.getModel() instanceof AgeableListModelInvoker invoker) {
            HumanoidAnimationsManager.resetModelParts(invoker.invokeHeadParts(), invoker.invokeBodyParts());
        }
    }

}
