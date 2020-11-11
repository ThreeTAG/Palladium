package net.threetag.threecore.accessoires;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.PlayerRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.threetag.threecore.client.renderer.entity.model.WingsModel;

/**
 * Created by Swirtzly
 * on 26/04/2020 @ 18:34
 */
public class HaloAccessoire extends Accessoire {

    @OnlyIn(Dist.CLIENT)
    @Override
    public void render(PlayerRenderer renderer, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, AbstractClientPlayerEntity player, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        matrixStackIn.push();
        renderer.getEntityModel().bipedHead.translateRotate(matrixStackIn);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(player.world.getGameTime()));
        float ticksExisted = (float) player.ticksExisted + partialTicks;
        float offset = MathHelper.sin(ticksExisted * 0.2F) / 2.0F + 0.5F;
        offset = offset * offset + offset;
        matrixStackIn.translate(0, -0.2f + (offset * 0.05f), 0);
        WingsModel.INSTANCE.renderHalo(matrixStackIn, bufferIn.getBuffer(WingsModel.INSTANCE.getRenderType(WingsModel.TEXTURE)), packedLightIn, OverlayTexture.NO_OVERLAY, 1F, 1F, 1F, 1F);
        matrixStackIn.pop();
    }
}
