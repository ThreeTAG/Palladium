package net.threetag.threecore.accessoires;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.entity.PlayerRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.threetag.threecore.ThreeCore;
import net.threetag.threecore.client.renderer.entity.model.HaloModel;

/**
 * Created by Swirtzly
 * on 26/04/2020 @ 18:34
 */
public class HaloAccessoire extends Accessoire {

    public static final ResourceLocation TEXTURE = new ResourceLocation(ThreeCore.MODID, "textures/models/accessories/halo_wings.png");
    public static HaloModel MODEL = new HaloModel(RenderType::getEntityTranslucent);

    @Override
    public boolean isAvailable(PlayerEntity entity) {
        return true;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void render(PlayerRenderer renderer, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, AbstractClientPlayerEntity player, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        matrixStackIn.push();
        renderer.getEntityModel().bipedHead.translateRotate(matrixStackIn);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(player.world.getGameTime()));
        float ticksExisted = (float) player.ticksExisted + Minecraft.getInstance().getRenderPartialTicks();
        float offset = MathHelper.sin(ticksExisted * 0.2F) / 2.0F + 0.5F;
        offset = offset * offset + offset;
        matrixStackIn.translate(0, -0.2f + (offset * 0.05f), 0);
        MODEL.render(matrixStackIn, bufferIn.getBuffer(MODEL.getRenderType(TEXTURE)), packedLightIn, OverlayTexture.NO_OVERLAY, 1F, 1F, 1F, 1F);
        matrixStackIn.pop();
    }
}
