package net.threetag.palladium.client.renderer.entity.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.threetag.palladium.client.model.SuitStandBasePlateModel;
import net.threetag.palladium.client.model.SuitStandModel;
import net.threetag.palladium.client.renderer.entity.SuitStandRenderer;
import net.threetag.palladium.client.renderer.entity.state.SuitStandRenderState;

public class ColorableSuitStandLayer extends RenderLayer<SuitStandRenderState, SuitStandBasePlateModel> {

    private final SuitStandModel model;

    public ColorableSuitStandLayer(RenderLayerParent<SuitStandRenderState, SuitStandBasePlateModel> renderer, SuitStandModel model) {
        super(renderer);
        this.model = model;
    }

    @Override
    public void submit(PoseStack poseStack, SubmitNodeCollector nodeCollector, int packedLight, SuitStandRenderState renderState, float yRot, float xRot) {
        coloredCutoutModelCopyLayerRender(this.model, SuitStandRenderer.TEXTURE, poseStack, nodeCollector, packedLight, renderState, renderState.getDyeColor(), 0);
    }
}
