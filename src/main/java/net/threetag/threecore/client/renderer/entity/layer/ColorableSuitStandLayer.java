package net.threetag.threecore.client.renderer.entity.layer;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.item.DyeColor;
import net.threetag.threecore.client.renderer.entity.SuitStandRenderer;
import net.threetag.threecore.client.renderer.entity.model.SuitStandBasePlateModel;
import net.threetag.threecore.client.renderer.entity.model.SuitStandModel;
import net.threetag.threecore.entity.SuitStandEntity;

public class ColorableSuitStandLayer extends LayerRenderer<SuitStandEntity, SuitStandBasePlateModel> {

    private final SuitStandModel model;

    public ColorableSuitStandLayer(SuitStandRenderer suitStandRenderer, SuitStandModel suitStandModel) {
        super(suitStandRenderer);
        this.model = suitStandModel;
    }

    @Override
    // TODO fix parameters & variables
    public void render(MatrixStack matrixStack, IRenderTypeBuffer renderTypeBuffer, int i, SuitStandEntity suitStandEntity, float p_225628_5_, float p_225628_6_, float p_225628_7_, float p_225628_8_, float p_225628_9_, float p_225628_10_) {
        matrixStack.push();
        if (!suitStandEntity.isInvisible()) {
            float lvt_11_2_;
            float lvt_12_2_;
            float lvt_13_2_;
            if (suitStandEntity.hasCustomName() && "jeb_".equals(suitStandEntity.getName().getUnformattedComponentText())) {
                int lvt_15_1_ = suitStandEntity.ticksExisted / 25 + suitStandEntity.getEntityId();
                int lvt_16_1_ = DyeColor.values().length;
                int lvt_17_1_ = lvt_15_1_ % lvt_16_1_;
                int lvt_18_1_ = (lvt_15_1_ + 1) % lvt_16_1_;
                float lvt_19_1_ = ((float) (suitStandEntity.ticksExisted % 25) + p_225628_7_) / 25.0F;
                float[] lvt_20_1_ = SheepEntity.getDyeRgb(DyeColor.byId(lvt_17_1_));
                float[] lvt_21_1_ = SheepEntity.getDyeRgb(DyeColor.byId(lvt_18_1_));
                lvt_11_2_ = lvt_20_1_[0] * (1.0F - lvt_19_1_) + lvt_21_1_[0] * lvt_19_1_;
                lvt_12_2_ = lvt_20_1_[1] * (1.0F - lvt_19_1_) + lvt_21_1_[1] * lvt_19_1_;
                lvt_13_2_ = lvt_20_1_[2] * (1.0F - lvt_19_1_) + lvt_21_1_[2] * lvt_19_1_;
            } else {
                float[] lvt_14_2_ = SheepEntity.getDyeRgb(suitStandEntity.getDyeColor());
                lvt_11_2_ = lvt_14_2_[0];
                lvt_12_2_ = lvt_14_2_[1];
                lvt_13_2_ = lvt_14_2_[2];
            }

            float scale = 0.9375F;
            matrixStack.scale(scale, scale, scale);
            matrixStack.translate(0F, 0.1F, 0F);
            this.getEntityModel().setModelAttributes(this.model);

            renderCopyCutoutModel(this.getEntityModel(), this.model, SuitStandRenderer.TEXTURE, matrixStack, renderTypeBuffer, i, suitStandEntity, p_225628_5_, p_225628_6_, p_225628_8_, p_225628_9_, p_225628_10_, p_225628_7_, lvt_11_2_, lvt_12_2_, lvt_13_2_);
            matrixStack.pop();
        }
    }
}
