package net.threetag.palladium.client.renderer.trail;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.entity.PalladiumLivingEntityExtension;
import net.threetag.palladium.entity.TrailSegmentEntity;
import org.jetbrains.annotations.Nullable;

public abstract class TrailRenderer {

    public void render(PoseStack poseStack, MultiBufferSource buffer, int packedLight, LivingEntityRenderer<LivingEntity, EntityModel<LivingEntity>> renderLayerParent, LivingEntity livingEntity, TrailSegmentEntity segment, @Nullable TrailSegmentEntity nextSegment, float partialTick) {

    }

    public abstract int getLifetime();

    public static void renderTrails(PoseStack poseStack, MultiBufferSource buffer, int packedLight, LivingEntity livingEntity, LivingEntityRenderer<LivingEntity, EntityModel<LivingEntity>> renderer, float partialTick) {
        if (livingEntity instanceof PalladiumLivingEntityExtension extension) {
            var trailHandler = extension.palladium$getTrailHandler();

//            for (Map.Entry<TrailRenderer, List<TrailHandler.TrailSegment>> entry : trailHandler.getTrails().entrySet()) {
//                var trailRenderer = entry.getKey();
//                var trails = entry.getValue();
//
//                for (int i = 0; i < trails.size(); i++) {
//                    var next = i < trails.size() - 1 ? trails.get(i + 1) : null;
//                    trailRenderer.render(poseStack, buffer, packedLight, renderer, livingEntity, trails.get(i), next, partialTick);
//                }
//            }
        }
    }

}
