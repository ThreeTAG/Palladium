package net.threetag.palladium.client.renderer.entity.layer.pack;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.threetag.palladium.client.renderer.entity.state.PalladiumRenderStateKeys;
import net.threetag.palladium.client.util.PerspectiveAwareConditions;

import java.util.Collections;

public class PackRenderLayerRenderer<S extends EntityRenderState, M extends EntityModel<? super S>> extends RenderLayer<S, M> {

    public PackRenderLayerRenderer(RenderLayerParent<S, M> renderer) {
        super(renderer);
    }

    @SuppressWarnings("rawtypes")
    @Override
    public void submit(PoseStack poseStack, SubmitNodeCollector nodeCollector, int packedLight, S renderState, float yRot, float xRot) {
        renderState.getRenderDataOrDefault(PalladiumRenderStateKeys.RENDER_LAYERS, Collections.emptyMap()).forEach((layer, state) -> {
            if (layer.shouldRender(state, PerspectiveAwareConditions.Perspective.THIRD_PERSON)) {
                EntityModel model = this.getParentModel();
                layer.submit(
                        state.getContext(),
                        poseStack,
                        nodeCollector,
                        model,
                        (LivingEntityRenderState) renderState,
                        state,
                        packedLight,
                        renderState.partialTick,
                        xRot,
                        yRot
                );
            }
        });
    }
}
