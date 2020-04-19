package net.threetag.threecore.accessoires;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.PlayerRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.threetag.threecore.ThreeCore;
import net.threetag.threecore.client.renderer.entity.model.JayGarrickHelmetModel;

public class JayGarrickHelmetAccessoire extends Accessoire {

    public static JayGarrickHelmetModel MODEL = new JayGarrickHelmetModel(RenderType::getEntityTranslucent);
    public static final ResourceLocation TEXTURE = new ResourceLocation(ThreeCore.MODID, "textures/models/accessories/jay_garrick_helmet.png");

    @Override
    public boolean isAvailable(PlayerEntity entity) {
        return true;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void render(PlayerRenderer renderer, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, AbstractClientPlayerEntity player, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if (!player.getItemStackFromSlot(EquipmentSlotType.HEAD).isEmpty())
            return;

        matrixStackIn.push();
        MODEL = new JayGarrickHelmetModel(RenderType::getEntityTranslucent);
        renderer.getEntityModel().bipedHead.translateRotate(matrixStackIn);
        matrixStackIn.translate(0, -0.4F, 0);
        MODEL.render(matrixStackIn, bufferIn.getBuffer(MODEL.getRenderType(TEXTURE)), packedLightIn, OverlayTexture.NO_OVERLAY, 1F, 1F, 1F, 1F);
        matrixStackIn.pop();
    }
}
