package net.threetag.threecore.accessoires;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.entity.PlayerRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.threetag.threecore.ThreeCore;
import net.threetag.threecore.client.renderer.entity.model.WingsModel;

/**
 * Created by Swirtzly
 * on 26/04/2020 @ 21:54
 */
public class WingsAccessoire extends Accessoire {

    public static final ResourceLocation TEXTURE = new ResourceLocation(ThreeCore.MODID, "textures/models/accessories/halo_wings.png");
    WingsModel MODEL = new WingsModel(RenderType::getEntityTranslucent);

    @Override
    public boolean isAvailable(PlayerEntity entity) {
        return true;
    }

    @Override
    public void render(PlayerRenderer renderer, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, AbstractClientPlayerEntity player, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        float motion = Math.abs(MathHelper.sin(limbSwing * 0.033F + (float) Math.PI) * 0.4F) * limbSwingAmount;
        boolean flapWings = player.world.isAirBlock(player.getPosition().down());
        float speed = 0.55f + 0.5f * motion;
        float y = MathHelper.sin(ageInTicks * 0.35F);
        float flap = y * 0.5f * speed;

        //left Wing
        matrixStackIn.push();
        if (flapWings) {
            matrixStackIn.rotate(Vector3f.YP.rotationDegrees(flap * 20));
        }
        MODEL.left_wing_1.render(matrixStackIn, bufferIn.getBuffer(MODEL.getRenderType(TEXTURE)), packedLightIn, OverlayTexture.NO_OVERLAY, 1F, 1F, 1F, 1F);
        matrixStackIn.pop();

        //Right Wing
        matrixStackIn.push();
        if (flapWings) {
            matrixStackIn.rotate(Vector3f.YP.rotationDegrees(-flap * 20));
        }
        MODEL.right_wing_1.render(matrixStackIn, bufferIn.getBuffer(MODEL.getRenderType(TEXTURE)), packedLightIn, OverlayTexture.NO_OVERLAY, 1F, 1F, 1F, 1F);
        matrixStackIn.pop();

    }
}
