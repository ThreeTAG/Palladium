package net.threetag.palladium.client.renderer.renderlayer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.threetag.palladium.accessory.Accessory;

public class AccessoryRenderLayer extends RenderLayer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> {

    private final RenderLayerParent<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> renderLayerParent;

    public AccessoryRenderLayer(RenderLayerParent<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> renderLayerParent) {
        super(renderLayerParent);
        this.renderLayerParent = renderLayerParent;
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource source, int packedLight, AbstractClientPlayer player, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        Accessory.getPlayerData(player).ifPresent(data -> data.getSlots().forEach((slot, accessories) -> {
            for (Accessory accessory : accessories) {
                if (accessory.isVisible(slot, player, false)) {
                    accessory.render(this.renderLayerParent, slot, poseStack, source, packedLight, player, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch);
                }
            }
        }));
    }
}
