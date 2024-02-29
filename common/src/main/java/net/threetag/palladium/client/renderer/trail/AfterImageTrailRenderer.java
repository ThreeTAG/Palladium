package net.threetag.palladium.client.renderer.trail;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.entity.TrailSegmentEntity;
import org.jetbrains.annotations.Nullable;

public class AfterImageTrailRenderer extends TrailRenderer {

    @Override
    public void render(PoseStack poseStack, MultiBufferSource buffer, int packedLight, LivingEntityRenderer<LivingEntity, EntityModel<LivingEntity>> renderer, LivingEntity livingEntity, TrailSegmentEntity segment, @Nullable TrailSegmentEntity nextSegment, float partialTick) {
        poseStack.pushPose();
        renderer.getModel().renderToBuffer(poseStack, buffer.getBuffer(RenderType.entityTranslucent(renderer.getTextureLocation(livingEntity))), packedLight, OverlayTexture.NO_OVERLAY, 1F, 1F, 1F, 1F - (segment.tickCount / (float) this.getLifetime()));
        poseStack.popPose();
    }

    @Override
    public int getLifetime() {
        return 10;
    }
}
