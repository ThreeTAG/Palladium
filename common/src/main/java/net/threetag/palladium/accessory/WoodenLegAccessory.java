package net.threetag.palladium.accessory;

import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.threetag.palladium.Palladium;

import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;

public class WoodenLegAccessory extends Accessory {

    public static final ResourceLocation TEXTURE = Palladium.id("textures/models/accessories/wooden_leg.png");
    private Object woodenLegModel, shortenedLegsModel;

    @Environment(EnvType.CLIENT)
    @Override
    public void onReload(EntityModelSet entityModelSet) {
        this.shortenedLegsModel = new PlayerModel<>(entityModelSet.bakeLayer(new ModelLayerLocation(Palladium.id("player"), "shortened_legs")), false);
        this.woodenLegModel = new PlayerModel<>(entityModelSet.bakeLayer(new ModelLayerLocation(Palladium.id("humanoid"), "wooden_legs")), false);
    }

    @SuppressWarnings("unchecked")
    @Environment(EnvType.CLIENT)
    @Override
    public void render(RenderLayerParent<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> renderLayerParent, AccessorySlot slot, PoseStack poseStack, MultiBufferSource bufferSource, int packedLightIn, AbstractClientPlayer player, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if(this.woodenLegModel instanceof HumanoidModel woodLegs && this.shortenedLegsModel instanceof PlayerModel shortened) {
            renderLayerParent.getModel().copyPropertiesTo(woodLegs);
            renderLayerParent.getModel().copyPropertiesTo(shortened);
            woodLegs.setAllVisible(false);
            shortened.setAllVisible(false);
            slot.setVisibility(woodLegs, player, true);
            slot.setVisibility(shortened, player, true);

            var buffer = bufferSource.getBuffer(Objects.requireNonNull(getRenderType(player, player.getSkinTextureLocation(), renderLayerParent.getModel())));
            shortened.renderToBuffer(poseStack, buffer, packedLightIn, OverlayTexture.NO_OVERLAY, 1F, 1F, 1F, 1F);

            buffer = bufferSource.getBuffer(Objects.requireNonNull(getRenderType(player, TEXTURE, renderLayerParent.getModel())));
            woodLegs.renderToBuffer(poseStack, buffer, packedLightIn, OverlayTexture.NO_OVERLAY, 1F, 1F, 1F, 1F);
        }
    }

    @Override
    public Collection<AccessorySlot> getPossibleSlots() {
        return Arrays.asList(AccessorySlot.LEFT_LEG, AccessorySlot.RIGHT_LEG);
    }
}
