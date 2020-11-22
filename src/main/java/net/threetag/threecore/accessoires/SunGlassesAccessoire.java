package net.threetag.threecore.accessoires;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.PlayerRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.threetag.threecore.client.renderer.entity.model.GlassesModel;

import java.util.Collection;
import java.util.Collections;

public class SunGlassesAccessoire extends Accessoire {

    @OnlyIn(Dist.CLIENT)
    @Override
    public void render(PlayerRenderer renderer, AccessoireSlot slot, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, AbstractClientPlayerEntity player, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        matrixStackIn.push();
        renderer.getEntityModel().bipedHead.translateRotate(matrixStackIn);
        matrixStackIn.translate(0, -25.5F/16F, 0);
        GlassesModel.INSTANCE.render(matrixStackIn, bufferIn.getBuffer(GlassesModel.INSTANCE.getRenderType(this.getTexture())), packedLightIn, OverlayTexture.NO_OVERLAY, 1F, 1F, 1F, 1F);
        matrixStackIn.pop();
    }

    @OnlyIn(Dist.CLIENT)
    private ResourceLocation getTexture() {
        return this == Accessoires.HEART_GLASSES.get() ? GlassesModel.TEXTURE_HEART_GLASSES : GlassesModel.TEXTURE_SUN_GLASSES;
    }

    @Override
    public Collection<AccessoireSlot> getPossibleSlots() {
        return Collections.singletonList(AccessoireSlot.FACE);
    }
}
