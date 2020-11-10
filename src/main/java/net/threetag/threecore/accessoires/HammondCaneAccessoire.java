package net.threetag.threecore.accessoires;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.PlayerRenderer;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.HandSide;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.threetag.threecore.client.renderer.entity.model.HammondCaneModel;
import net.threetag.threecore.util.PlayerUtil;

public class HammondCaneAccessoire extends Accessoire {

    @Override
    public boolean isAvailable(PlayerEntity entity) {
        return true;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void render(PlayerRenderer renderer, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, AbstractClientPlayerEntity player, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        HandSide handSide = player.getPrimaryHand();

        if (!player.getHeldItem(Hand.OFF_HAND).isEmpty()) {
            return;
        }

        matrixStackIn.push();
        ModelRenderer part = handSide == HandSide.RIGHT ? renderer.getEntityModel().bipedLeftArm : renderer.getEntityModel().bipedRightArm;
        part.translateRotate(matrixStackIn);
        matrixStackIn.translate(0, -0.3F, 0.2F);
        matrixStackIn.rotate(Vector3f.XN.rotationDegrees(70F));
        if (PlayerUtil.hasSmallArms(player)) {
            matrixStackIn.translate(handSide == HandSide.LEFT ? -0.5F / 16F : 0.5F / 16F, 10F / 16F, 0);
        } else {
            matrixStackIn.translate(handSide == HandSide.LEFT ? -1F / 16F : 1F / 16F, 10F / 16F, 0);
        }
        matrixStackIn.rotate(Vector3f.XP.rotationDegrees(90F));
        HammondCaneModel.INSTANCE.render(matrixStackIn, bufferIn.getBuffer(HammondCaneModel.INSTANCE.getRenderType(HammondCaneModel.TEXTURE)), packedLightIn, OverlayTexture.NO_OVERLAY, 1F, 1F, 1F, 1F);
        matrixStackIn.pop();
    }

}
