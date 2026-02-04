package net.threetag.palladium.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.client.renderer.entity.HumanoidRendererModifications;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@SuppressWarnings({"rawtypes", "ConstantConditions"})
@Mixin(LivingEntityRenderer.class)
public abstract class LivingEntityRendererMixin {

    @Inject(at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/renderer/entity/LivingEntityRenderer;setupRotations(Lnet/minecraft/world/entity/LivingEntity;Lcom/mojang/blaze3d/vertex/PoseStack;FFF)V",
            shift = At.Shift.AFTER),
            method = "render(Lnet/minecraft/world/entity/LivingEntity;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V")
    private void preSetup(LivingEntity pEntity, float pEntityYaw, float pPartialTicks, PoseStack pMatrixStack, MultiBufferSource pBuffer, int pPackedLight, CallbackInfo ci) {
        if ((Object) this instanceof LivingEntityRenderer renderer && renderer.getModel() instanceof HumanoidModel model) {
            HumanoidRendererModifications.preSetup(renderer, pEntity, model, pMatrixStack, pPartialTicks);
        }
    }

    @Inject(at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/model/EntityModel;setupAnim(Lnet/minecraft/world/entity/Entity;FFFFF)V",
            shift = At.Shift.AFTER),
            method = "render(Lnet/minecraft/world/entity/LivingEntity;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V")
    private void preRender(LivingEntity pEntity, float pEntityYaw, float pPartialTicks, PoseStack pMatrixStack, MultiBufferSource pBuffer, int pPackedLight, CallbackInfo ci) {
        if ((Object) this instanceof LivingEntityRenderer renderer && renderer.getModel() instanceof HumanoidModel model) {
            HumanoidRendererModifications.preRender(renderer, pEntity, model, pMatrixStack, pPartialTicks);
        }
    }

    @Inject(at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/LivingEntity;isSpectator()Z"),
            method = "render(Lnet/minecraft/world/entity/LivingEntity;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V")
    private void preLayers(LivingEntity pEntity, float pEntityYaw, float pPartialTicks, PoseStack pMatrixStack, MultiBufferSource pBuffer, int pPackedLight, CallbackInfo ci) {
        if ((Object) this instanceof LivingEntityRenderer renderer && renderer.getModel() instanceof HumanoidModel model) {
            HumanoidRendererModifications.preLayers(renderer, pEntity, model, pMatrixStack);
        }
    }

    @Inject(at = @At(
            value = "INVOKE",
            target = "Lcom/mojang/blaze3d/vertex/PoseStack;popPose()V"),
            method = "render(Lnet/minecraft/world/entity/LivingEntity;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V")
    private void postLayers(LivingEntity pEntity, float pEntityYaw, float pPartialTicks, PoseStack pMatrixStack, MultiBufferSource pBuffer, int pPackedLight, CallbackInfo ci) {
        if ((Object) this instanceof LivingEntityRenderer renderer && renderer.getModel() instanceof HumanoidModel model) {
            HumanoidRendererModifications.postLayers(renderer, pEntity, model, pMatrixStack, pBuffer, pPackedLight, pPartialTicks);
        }
    }

    // Caching setupAnim arguments -------------------------------------------------------------------------------------

    @ModifyArgs(method = "render(Lnet/minecraft/world/entity/LivingEntity;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/model/EntityModel;setupAnim(Lnet/minecraft/world/entity/Entity;FFFFF)V"))
    private void setupAnimArgs(Args args) {
        int floatIndex = -1;
        for (int i = 0; i < args.size(); i++) {
            if (args.get(i) instanceof Float) {
                floatIndex = i;
                break;
            }
        }

        if (floatIndex != -1 && floatIndex + 4 < args.size()) {
            HumanoidRendererModifications.CACHED_LIMB_SWING = args.get(floatIndex);
            HumanoidRendererModifications.CACHED_LIMB_SWING_AMOUNT = args.get(floatIndex + 1);
            HumanoidRendererModifications.CACHED_AGE_IN_TICKS = args.get(floatIndex + 2);
            HumanoidRendererModifications.CACHED_NET_HEAD_YAW = args.get(floatIndex + 3);
            HumanoidRendererModifications.CACHED_HEAD_PITCH = args.get(floatIndex + 4);
        }
    }

}
