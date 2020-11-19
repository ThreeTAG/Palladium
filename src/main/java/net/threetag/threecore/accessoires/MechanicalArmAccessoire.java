package net.threetag.threecore.accessoires;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.PlayerRenderer;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.HandSide;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.threetag.threecore.client.renderer.entity.model.MechanicalArmModel;
import net.threetag.threecore.util.PlayerUtil;

import java.util.Arrays;
import java.util.Collection;

public class MechanicalArmAccessoire extends Accessoire {

    @OnlyIn(Dist.CLIENT)
    @Override
    public void render(PlayerRenderer renderer, AccessoireSlot slot, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, AbstractClientPlayerEntity player, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        matrixStackIn.push();
        ModelRenderer part = getArm(renderer.getEntityModel(), slot == AccessoireSlot.MAIN_ARM, player.getPrimaryHand());
        HandSide handSide = part == renderer.getEntityModel().bipedRightArm ? HandSide.RIGHT : HandSide.LEFT;
        part.translateRotate(matrixStackIn);

        if(handSide == HandSide.RIGHT) {
            matrixStackIn.translate(-1F / 16F, -14F / 16F, 0);
            matrixStackIn.rotate(Vector3f.YP.rotationDegrees(180F));
        } else {
            matrixStackIn.translate(1F / 16F, -14F / 16F, 0);
        }

        boolean smallArms = PlayerUtil.hasSmallArms(player);
        MechanicalArmModel.INSTANCE.renderArm(smallArms, matrixStackIn, bufferIn.getBuffer(MechanicalArmModel.INSTANCE.getRenderType(smallArms ? MechanicalArmModel.TEXTURE_SLIM :
                MechanicalArmModel.TEXTURE)), packedLightIn, OverlayTexture.NO_OVERLAY, 1F, 1F, 1F, 1F);
        matrixStackIn.pop();
    }

    @Override
    public Collection<AccessoireSlot> getPossibleSlots() {
        return Arrays.asList(AccessoireSlot.MAIN_ARM, AccessoireSlot.OFF_ARM);
    }
}
