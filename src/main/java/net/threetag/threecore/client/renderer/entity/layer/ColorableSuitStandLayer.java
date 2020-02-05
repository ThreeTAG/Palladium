package net.threetag.threecore.client.renderer.entity.layer;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.passive.SheepEntity;
import net.threetag.threecore.client.renderer.entity.model.SuitStandBasePlateModel;
import net.threetag.threecore.client.renderer.entity.model.SuitStandModel;
import net.threetag.threecore.client.renderer.entity.SuitStandRenderer;
import net.threetag.threecore.entity.SuitStandEntity;

public class ColorableSuitStandLayer extends LayerRenderer<SuitStandEntity, SuitStandBasePlateModel> {

    private final SuitStandModel model;

    public ColorableSuitStandLayer(SuitStandRenderer suitStandRenderer, SuitStandModel suitStandModel) {
        super(suitStandRenderer);
        this.model = suitStandModel;
    }

    @Override
    public void render(SuitStandEntity suitStandEntity, float v, float v1, float v2, float v3, float v4, float v5, float v6) {
        float[] colors = SheepEntity.getDyeRgb(suitStandEntity.getDyeColor());
        GlStateManager.color3f(colors[0], colors[1], colors[2]);
        float scale = 0.9375F;
        GlStateManager.scalef(scale, scale, scale);
        GlStateManager.translatef(0F, 0.1F, 0F);
        this.getEntityModel().setModelAttributes(this.model);
        this.model.setLivingAnimations(suitStandEntity, v, v2, v3);
        this.model.render(suitStandEntity, v, v1, v3, v4, v5, v6);
    }

    @Override
    public boolean shouldCombineTextures() {
        return true;
    }
}
