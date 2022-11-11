package net.threetag.palladium.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.entity.BodyPart;
import net.threetag.palladium.power.ability.ShrinkBodyOverlayAbility;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@SuppressWarnings({"rawtypes", "ConstantConditions"})
@Mixin(LivingEntityRenderer.class)
public class LivingEntityRendererMixin {

    private float cachedShrink = 0F;

    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/LivingEntityRenderer;isBodyVisible(Lnet/minecraft/world/entity/LivingEntity;)Z"), method = "render(Lnet/minecraft/world/entity/LivingEntity;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V", cancellable = true)
    public void render(LivingEntity pEntity, float pEntityYaw, float pPartialTicks, PoseStack pMatrixStack, MultiBufferSource pBuffer, int pPackedLight, CallbackInfo ci) {
        float scale = ShrinkBodyOverlayAbility.getValue(pEntity);
        EntityModel<?> entityModel = ((LivingEntityRenderer) (Object) this).getModel();

        if (scale != 0F && entityModel instanceof PlayerModel<?> model) {
            float f = -0.1F * scale;
            this.cachedShrink = f;
            Vector3f vec = new Vector3f(f, f, f);
            for (BodyPart value : BodyPart.values()) {
                ModelPart part = value.getModelPart(model);
                if (value.isOverlay() && part != null) {
                    part.offsetScale(vec);
                }
            }
        }
    }

    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;isSpectator()Z"), method = "render(Lnet/minecraft/world/entity/LivingEntity;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V")
    public void preSpectator(LivingEntity pEntity, float pEntityYaw, float pPartialTicks, PoseStack pMatrixStack, MultiBufferSource pBuffer, int pPackedLight, CallbackInfo ci) {
        if (this.cachedShrink != 0F) {
            EntityModel<?> entityModel = ((LivingEntityRenderer) (Object) this).getModel();

            if(entityModel instanceof PlayerModel<?> model) {
                float f = -this.cachedShrink;
                this.cachedShrink = 0F;
                Vector3f vec = new Vector3f(f, f, f);
                for (BodyPart value : BodyPart.values()) {
                    ModelPart part = value.getModelPart(model);
                    if (value.isOverlay() && part != null) {
                        part.offsetScale(vec);
                    }
                }
            }
        }
    }

}
