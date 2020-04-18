package net.threetag.threecore.accessoires;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.PlayerRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.threetag.threecore.ThreeCore;

public class HerobrineEyesAccessoire extends Accessoire {

    private static final RenderType RENDER_TYPE = RenderType.getEyes(new ResourceLocation(ThreeCore.MODID, "textures/models/accessories/herobrine_eyes.png"));

    @Override
    public boolean isAvailable(PlayerEntity entity) {
        return true;
    }

    @Override
    public void render(PlayerRenderer renderer, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, AbstractClientPlayerEntity player, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        renderer.getEntityModel().setVisible(true);
        IVertexBuilder ivertexbuilder = bufferIn.getBuffer(RenderType.getEntityTranslucent(new ResourceLocation(ThreeCore.MODID, "textures/models/accessories/herobrine_eyes.png")));
        renderer.getEntityModel().render(matrixStackIn, ivertexbuilder, packedLightIn, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
    }
}
