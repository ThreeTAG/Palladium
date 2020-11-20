package net.threetag.threecore.accessoires;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.PlayerRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.threetag.threecore.client.renderer.entity.model.HeartGlassesModel;

import java.util.Collection;
import java.util.Collections;

public class HeartGlassesAccessoire extends Accessoire {

    @OnlyIn(Dist.CLIENT)
    @Override
    public void render(PlayerRenderer renderer, AccessoireSlot slot, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, AbstractClientPlayerEntity player, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        matrixStackIn.push();
        renderer.getEntityModel().bipedHead.translateRotate(matrixStackIn);
        matrixStackIn.translate(0, -25.5F/16F, 0);
        HeartGlassesModel.INSTANCE.render(matrixStackIn, bufferIn.getBuffer(HeartGlassesModel.INSTANCE.getRenderType(HeartGlassesModel.TEXTURE)), packedLightIn, OverlayTexture.NO_OVERLAY, 1F, 1F, 1F, 1F);
        matrixStackIn.pop();
    }

    @Override
    public Collection<AccessoireSlot> getPossibleSlots() {
        return Collections.singletonList(AccessoireSlot.FACE);
    }
}
