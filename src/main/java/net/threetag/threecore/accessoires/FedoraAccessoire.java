package net.threetag.threecore.accessoires;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.PlayerRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.threetag.threecore.client.renderer.entity.model.FedoraModel;

import java.util.Collection;
import java.util.Collections;

public class FedoraAccessoire extends Accessoire {

    @OnlyIn(Dist.CLIENT)
    @Override
    public void render(PlayerRenderer renderer, AccessoireSlot slot, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, AbstractClientPlayerEntity player, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        matrixStackIn.push();
        renderer.getEntityModel().bipedHead.translateRotate(matrixStackIn);
        matrixStackIn.translate(0, -31.5F/16F, 0);
        matrixStackIn.scale(1.05F, 1.05F, 1.05F);
        FedoraModel.INSTANCE.render(matrixStackIn, bufferIn.getBuffer(FedoraModel.INSTANCE.getRenderType(this.getTexture())), packedLightIn, OverlayTexture.NO_OVERLAY, 1F, 1F, 1F, 1F);
        matrixStackIn.pop();
    }

    @OnlyIn(Dist.CLIENT)
    private ResourceLocation getTexture() {
        return this == Accessoires.ELTON_HAT.get() ? FedoraModel.TEXTURE_ELTON_HAT : FedoraModel.TEXTURE_OWCA_FEDORA;
    }

    @Override
    public Collection<AccessoireSlot> getPossibleSlots() {
        return Collections.singletonList(AccessoireSlot.HAT);
    }
}
