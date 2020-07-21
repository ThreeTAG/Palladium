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
    public void render(MatrixStack matrixStack, IRenderTypeBuffer renderTypeBuffer, int packedLightIn, SuitStandEntity suitStandEntity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        matrixStack.push();
        if (!suitStandEntity.isInvisible()) {
            float red;
            float green;
            float blue;
            if (suitStandEntity.hasCustomName() && "jeb_".equals(suitStandEntity.getName().getUnformattedComponentText())) {
                int time = suitStandEntity.ticksExisted / 25 + suitStandEntity.getEntityId();
                int colors = DyeColor.values().length;
                int j = time % colors;
                int k = (time + 1) % colors;
                float f = ((float) (suitStandEntity.ticksExisted % 25) + partialTicks) / 25.0F;
                float[] afloat1 = SheepEntity.getDyeRgb(DyeColor.byId(j));
                float[] afloat2 = SheepEntity.getDyeRgb(DyeColor.byId(k));
                red = afloat1[0] * (1.0F - f) + afloat2[0] * f;
                green = afloat1[1] * (1.0F - f) + afloat2[1] * f;
                blue = afloat1[2] * (1.0F - f) + afloat2[2] * f;
            } else {
                float[] afloat = SheepEntity.getDyeRgb(suitStandEntity.getDyeColor());
                red = afloat[0];
                green = afloat[1];
                blue = afloat[2];
            }

            this.getEntityModel().setModelAttributes(this.model);
            renderCopyCutoutModel(this.getEntityModel(), this.model, SuitStandRenderer.TEXTURE, matrixStack, renderTypeBuffer, packedLightIn, suitStandEntity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, partialTicks, red, green, blue);
            matrixStack.pop();
        }
    }
}
