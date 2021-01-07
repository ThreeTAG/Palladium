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

import java.util.Collection;
import java.util.Collections;

/**
 * Created by Swirtzly
 * on 26/04/2020 @ 21:54
 */
public class WingsAccessoire extends Accessoire {

    @OnlyIn(Dist.CLIENT)
    @Override
    public void render(PlayerRenderer renderer, AccessoireSlot slot, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, AbstractClientPlayerEntity player, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        float motion = Math.abs(MathHelper.sin(limbSwing * 0.033F + (float) Math.PI) * 0.4F) * limbSwingAmount;
        boolean flapWings = player.world.isAirBlock(player.getPosition().down());
        float speed = 0.55f + 0.5f * motion;
        float y = MathHelper.sin(ageInTicks * 0.35F);
        float flap = y * 0.5f * speed;
        matrixStackIn.push();
        renderer.getEntityModel().bipedBody.translateRotate(matrixStackIn);

        //left Wing
        matrixStackIn.push();
        if (flapWings) {
            matrixStackIn.rotate(Vector3f.YP.rotationDegrees(flap * 20));
        }
        WingsModel.INSTANCE.left_wing_1.render(matrixStackIn, bufferIn.getBuffer(WingsModel.INSTANCE.getRenderType(WingsModel.TEXTURE)), packedLightIn, OverlayTexture.NO_OVERLAY, 1F, 1F, 1F, 1F);
        matrixStackIn.pop();

        //Right Wing
        matrixStackIn.push();
        if (flapWings) {
            matrixStackIn.rotate(Vector3f.YP.rotationDegrees(-flap * 20));
        }
        WingsModel.INSTANCE.right_wing_1.render(matrixStackIn, bufferIn.getBuffer(WingsModel.INSTANCE.getRenderType(WingsModel.TEXTURE)), packedLightIn, OverlayTexture.NO_OVERLAY, 1F, 1F, 1F, 1F);
        matrixStackIn.pop();
        matrixStackIn.pop();
    }

    @Override
    public Collection<AccessoireSlot> getPossibleSlots() {
        return Collections.singletonList(AccessoireSlot.BACK);
    }
}
