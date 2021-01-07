package net.threetag.threecore.accessoires;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.PlayerRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.threetag.threecore.client.renderer.entity.model.WoodenLegModel;

import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;

public class WoodenLegAccessoire extends Accessoire {

    @OnlyIn(Dist.CLIENT)
    @Override
    public void render(PlayerRenderer renderer, AccessoireSlot slot, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, AbstractClientPlayerEntity player, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        matrixStackIn.push();
        (slot == AccessoireSlot.RIGHT_LEG ? renderer.getEntityModel().bipedRightLeg : renderer.getEntityModel().bipedLeftLeg).translateRotate(matrixStackIn);
        matrixStackIn.translate(0, -12.2F * 0.06125F, 0);
        IVertexBuilder builder = bufferIn.getBuffer(WoodenLegModel.INSTANCE.getRenderType(WoodenLegModel.TEXTURE));
        WoodenLegModel.INSTANCE.render(matrixStackIn, builder, packedLightIn, OverlayTexture.NO_OVERLAY, 1F, 1F, 1F, 1F);
        builder = bufferIn.getBuffer(Objects.requireNonNull(getRenderType(player, player.getLocationSkin(), WoodenLegModel.INSTANCE)));
        WoodenLegModel.INSTANCE.renderPlayerLeg(slot == AccessoireSlot.RIGHT_LEG, matrixStackIn, builder, packedLightIn, OverlayTexture.NO_OVERLAY, 1F, 1F, 1F, 1F);
        matrixStackIn.pop();
    }

    @Override
    public Collection<AccessoireSlot> getPossibleSlots() {
        return Arrays.asList(AccessoireSlot.RIGHT_LEG, AccessoireSlot.LEFT_LEG);
    }
}
