package net.threetag.threecore.accessoires;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.PlayerRenderer;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.Objects;

public abstract class AbstractReplaceLimbTextureAccessoire extends Accessoire {

    @OnlyIn(Dist.CLIENT)
    public abstract ResourceLocation getTexture(AbstractClientPlayerEntity player);

    @OnlyIn(Dist.CLIENT)
    @Override
    public void render(PlayerRenderer renderer, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, AbstractClientPlayerEntity player, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        PlayerModel<AbstractClientPlayerEntity> model = renderer.getEntityModel();
        IVertexBuilder builder = bufferIn.getBuffer(Objects.requireNonNull(getRenderType(player, getTexture(player), model)));
        model.setVisible(false);
        this.getPlayerPart().setVisibility(model, true);
        model.render(matrixStackIn, builder, packedLightIn, OverlayTexture.NO_OVERLAY, 1F, 1F, 1F, 1F);
    }

    @Nullable
    @Override
    public abstract PlayerPart getPlayerPart();
}
