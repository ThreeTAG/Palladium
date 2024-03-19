package net.threetag.palladium.client.renderer.renderlayer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.world.entity.Entity;
import net.threetag.palladium.entity.PalladiumEntityExtension;
import net.threetag.palladium.entity.TrailSegmentEntity;

import java.util.List;

public class PackRenderLayerRenderer extends RenderLayer<Entity, EntityModel<Entity>> {

    public PackRenderLayerRenderer(RenderLayerParent<Entity, EntityModel<Entity>> renderLayerParent) {
        super(renderLayerParent);
    }

    @Override
    public void render(PoseStack matrixStack, MultiBufferSource buffer, int packedLight, Entity entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        PackRenderLayerManager.forEachLayer(entity, (context, layer) -> {
            layer.render(context, matrixStack, buffer, this.getParentModel(), packedLight, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch);

            if (context.getEntity() instanceof PalladiumEntityExtension ext) {
                for (List<TrailSegmentEntity<?>> list : ext.palladium$getTrailHandler().getTrails().values()) {
                    for (TrailSegmentEntity<?> segment : list) {
                        if (!segment.snapshotsGathered) {
                            layer.createSnapshot(context, this.getParentModel(), segment::addSnapshot);
                        }
                    }
                }
            }
        });

        if (entity instanceof PalladiumEntityExtension ext) {
            for (List<TrailSegmentEntity<?>> list : ext.palladium$getTrailHandler().getTrails().values()) {
                for (TrailSegmentEntity<?> segment : list) {
                    segment.snapshotsGathered = true;
                }
            }
        }
    }
}
