package net.threetag.palladium.client.renderer.entity.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.item.DyeColor;
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
    public void render(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, SuitStandRenderState renderState, float yRot, float xRot) {
        if (!renderState.isInvisible) {
            int r;

            if (renderState.customName != null && "Lucraft".equals(renderState.customName.getString())) {
                int j = 25;
                int k = Mth.floor(renderState.ageInTicks);
                int l = k / j + renderState.id;
                int m = DyeColor.values().length;
                int n = l % m;
                int o = (l + 1) % m;
                float h = ((float) (k % j) + Mth.frac(renderState.ageInTicks)) / 25.0F;
                int p = Sheep.getColor(DyeColor.byId(n));
                int q = Sheep.getColor(DyeColor.byId(o));
                r = ARGB.lerp(h, p, q);
            } else {
                r = Sheep.getColor(renderState.color);
            }

            this.getParentModel().copyPropertiesTo(this.model);
            coloredCutoutModelCopyLayerRender(this.model, SuitStandRenderer.TEXTURE, poseStack, bufferSource, packedLight, renderState, r);
        }
    }
}
