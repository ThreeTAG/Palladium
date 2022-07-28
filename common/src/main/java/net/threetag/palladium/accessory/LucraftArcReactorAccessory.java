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
import net.threetag.palladium.Palladium;

import java.util.Collection;
import java.util.Collections;

public class LucraftArcReactorAccessory extends Accessory {

    @Environment(EnvType.CLIENT)
    @Override
    public void render(RenderLayerParent<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> renderLayerParent, AccessorySlot slot, PoseStack poseStack, MultiBufferSource bufferSource, int packedLightIn, AbstractClientPlayer player, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        renderLayerParent.getModel().setAllVisible(true);
        var buffer = bufferSource.getBuffer(RenderType.eyes(Palladium.id("textures/models/accessories/lucraft_arc_reactor.png")));
        renderLayerParent.getModel().renderToBuffer(poseStack, buffer, packedLightIn, OverlayTexture.NO_OVERLAY, 1F, 1F, 1F, 1F);
    }

    @Override
    public Collection<AccessorySlot> getPossibleSlots() {
        return Collections.singletonList(AccessorySlot.SPECIAL);
    }
}
