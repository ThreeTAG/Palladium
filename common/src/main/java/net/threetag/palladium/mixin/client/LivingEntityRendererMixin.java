package net.threetag.palladium.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.client.animation.HideBodyPartsAnimation;
import net.threetag.palladium.client.renderer.entity.layer.PackRenderLayerRenderer;
import net.threetag.palladium.entity.BodyPart;
import net.threetag.palladium.entity.PlayerModelCacheExtension;
import net.threetag.palladium.util.RenderUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@SuppressWarnings({"rawtypes", "unchecked"})
@Mixin(LivingEntityRenderer.class)
public abstract class LivingEntityRendererMixin {

    @Shadow
    protected abstract boolean addLayer(RenderLayer layer);

    @Shadow
    public abstract EntityModel getModel();

    @Inject(method = "<init>", at = @At("RETURN"))
    private void addLayerInInit(EntityRendererProvider.Context context, EntityModel<?> model, float shadowRadius, CallbackInfo ci) {
        this.addLayer(new PackRenderLayerRenderer<>((LivingEntityRenderer) (Object) this));
    }

    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/LivingEntityRenderer;shouldRenderLayers(Lnet/minecraft/client/renderer/entity/state/LivingEntityRenderState;)Z"),
            method = "render(Lnet/minecraft/client/renderer/entity/state/LivingEntityRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V")
    private void preLayers(LivingEntityRenderState livingEntityRenderState, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, CallbackInfo ci) {
        if (RenderUtil.getCurrentlyRenderedEntity() instanceof LivingEntity living && this.getModel() instanceof HumanoidModel model) {
            for (BodyPart bodyPart : BodyPart.values()) {
                var visible = HideBodyPartsAnimation.CACHED_VISIBILITIES.getOrDefault(bodyPart, true);
                bodyPart.setVisibility(model, visible);
            }

            for (BodyPart bodyPart : BodyPart.getRemovedBodyParts(living)) {
                bodyPart.setVisibility(model, false);
            }
        }
    }

    @Inject(at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/PoseStack;popPose()V"),
            method = "render(Lnet/minecraft/client/renderer/entity/state/LivingEntityRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V")
    private void postLayers(LivingEntityRenderState livingEntityRenderState, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, CallbackInfo ci) {
        if (RenderUtil.getCurrentlyRenderedEntity() instanceof PlayerModelCacheExtension ext && this.getModel() instanceof HumanoidModel model) {
            model.copyPropertiesTo(ext.palladium$getCachedModel());
        }
    }

}
