package net.threetag.palladium.client.renderer.entity.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.threetag.palladium.client.util.ClientContextTypes;
import net.threetag.palladium.client.renderer.entity.ExtendedEntityRenderState;
import net.threetag.palladium.client.util.PerspectiveAwareConditions;
import org.jetbrains.annotations.NotNull;

public class PackRenderLayerRenderer<T extends LivingEntityRenderState> extends RenderLayer<T, EntityModel<T>> {

    public PackRenderLayerRenderer(RenderLayerParent<T, EntityModel<T>> renderer) {
        super(renderer);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    public void render(@NotNull PoseStack poseStack, @NotNull MultiBufferSource bufferSource, int packedLight, @NotNull LivingEntityRenderState renderState, float yRot, float xRot) {
        if (renderState instanceof ExtendedEntityRenderState ext && ext.palladium$hasData(ClientContextTypes.RENDER_LAYERS)) {
            ext.palladium$getData(ClientContextTypes.RENDER_LAYERS).forEach((layer, state) -> {
                if (layer.shouldRender(state, PerspectiveAwareConditions.Perspective.THIRD_PERSON)) {
                    EntityModel model = this.getParentModel();
                    layer.render(
                            state.getContext(),
                            poseStack,
                            bufferSource,
                            model,
                            renderState,
                            state,
                            packedLight,
                            ext.palladium$getData(ClientContextTypes.PARTIAL_TICK),
                            xRot,
                            yRot
                    );
                }
            });
        }
    }
}
