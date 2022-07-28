package net.threetag.palladium.accessory;

import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

public abstract class AbstractReplaceLimbTextureAccessory extends Accessory {

    @Environment(EnvType.CLIENT)
    public abstract ResourceLocation getTexture(AbstractClientPlayer player, AccessorySlot slot);

    @Environment(EnvType.CLIENT)
    @Override
    public void render(RenderLayerParent<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> renderLayerParent, AccessorySlot slot, PoseStack poseStack, MultiBufferSource bufferSource, int packedLightIn, AbstractClientPlayer player, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        PlayerModel<AbstractClientPlayer> model = renderLayerParent.getModel();
        RenderType renderType = getRenderType(player, getTexture(player, slot), model);
        if (renderType != null) {
            var buffer = bufferSource.getBuffer(renderType);
            model.setAllVisible(false);
            slot.setVisibility(model, player, true);
            model.renderToBuffer(poseStack, buffer, packedLightIn, OverlayTexture.NO_OVERLAY, 1F, 1F, 1F, 1F);
        }
    }
}
