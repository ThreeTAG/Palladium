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
            locals = LocalCapture.CAPTURE_FAILHARD,
            method = "render(Lnet/minecraft/world/entity/LivingEntity;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V")
    private void preRender(LivingEntity pEntity, float pEntityYaw, float pPartialTicks, PoseStack pMatrixStack, MultiBufferSource pBuffer, int pPackedLight, CallbackInfo ci) {
        if ((Object) this instanceof LivingEntityRenderer renderer && renderer.getModel() instanceof HumanoidModel model) {
            HumanoidRendererModifications.preRender(renderer, pEntity, model, pMatrixStack);
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

    // Caching setupAnim arguments -------------------------------------------------------------------------------------

    @ModifyArg(method = "render(Lnet/minecraft/world/entity/LivingEntity;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/model/EntityModel;setupAnim(Lnet/minecraft/world/entity/Entity;FFFFF)V"), index = 1)
    private float setupAnimArg1(float f) {
        HumanoidRendererModifications.CACHED_LIMB_SWING = f;
        return f;
    }

    @ModifyArg(method = "render(Lnet/minecraft/world/entity/LivingEntity;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/model/EntityModel;setupAnim(Lnet/minecraft/world/entity/Entity;FFFFF)V"), index = 2)
    private float setupAnimArg2(float f) {
        HumanoidRendererModifications.CACHED_LIMB_SWING_AMOUNT = f;
        return f;
    }

    @ModifyArg(method = "render(Lnet/minecraft/world/entity/LivingEntity;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/model/EntityModel;setupAnim(Lnet/minecraft/world/entity/Entity;FFFFF)V"), index = 3)
    private float setupAnimArg3(float f) {
        HumanoidRendererModifications.CACHED_AGE_IN_TICKS = f;
        return f;
    }

    @ModifyArg(method = "render(Lnet/minecraft/world/entity/LivingEntity;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/model/EntityModel;setupAnim(Lnet/minecraft/world/entity/Entity;FFFFF)V"), index = 4)
    private float setupAnimArg4(float f) {
        HumanoidRendererModifications.CACHED_NET_HEAD_YAW = f;
        return f;
    }

    @ModifyArg(method = "render(Lnet/minecraft/world/entity/LivingEntity;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/model/EntityModel;setupAnim(Lnet/minecraft/world/entity/Entity;FFFFF)V"), index = 5)
    private float setupAnimArg5(float f) {
        HumanoidRendererModifications.CACHED_HEAD_PITCH = f;
        return f;
    }

}
