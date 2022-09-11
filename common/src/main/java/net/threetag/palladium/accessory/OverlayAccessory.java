package net.threetag.palladium.accessory;

import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.util.PlayerUtil;

import java.util.Objects;

public class OverlayAccessory extends DefaultAccessory {

    protected final ResourceLocation texture;
    protected final ResourceLocation textureSlim;
    protected boolean glowing = false;
    protected boolean onlyRenderSlot = false;
    protected boolean handVisibilityFix = false;

    public OverlayAccessory(ResourceLocation texture, ResourceLocation textureSlim) {
        this.texture = texture;
        this.textureSlim = textureSlim;
    }

    public OverlayAccessory(ResourceLocation texture) {
        this.texture = this.textureSlim = texture;
    }

    public OverlayAccessory(String texture, String textureSlim) {
        this.texture = Palladium.id("textures/models/accessories/" + texture + ".png");
        this.textureSlim = Palladium.id("textures/models/accessories/" + textureSlim + ".png");
    }

    public OverlayAccessory(String texture) {
        this.texture = this.textureSlim = Palladium.id("textures/models/accessories/" + texture + ".png");
    }

    public OverlayAccessory glowing() {
        this.glowing = true;
        return this;
    }

    public OverlayAccessory onlyRenderSlot() {
        this.onlyRenderSlot = true;
        return this;
    }

    public OverlayAccessory handVisibilityFix() {
        this.handVisibilityFix = true;
        return this.onlyRenderSlot();
    }

    @Environment(EnvType.CLIENT)
    @Override
    public void render(RenderLayerParent<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> renderLayerParent, AccessorySlot slot, PoseStack poseStack, MultiBufferSource bufferSource, int packedLightIn, AbstractClientPlayer player, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        PlayerModel<AbstractClientPlayer> model = renderLayerParent.getModel();
        this.setVisibility(model, player, slot);
        ResourceLocation texture = PlayerUtil.hasSmallArms(player) ? this.textureSlim : this.texture;
        var buffer = bufferSource.getBuffer(this.glowing ? RenderType.eyes(texture) : Objects.requireNonNull(getRenderType(player, texture, renderLayerParent.getModel())));
        model.renderToBuffer(poseStack, buffer, packedLightIn, OverlayTexture.NO_OVERLAY, 1F, 1F, 1F, 1F);
    }

    @Environment(EnvType.CLIENT)
    public void setVisibility(HumanoidModel<?> model, AbstractClientPlayer player, AccessorySlot slot) {
        if(this.onlyRenderSlot) {
            model.setAllVisible(false);
            slot.setVisibility(model, player, true);

            if(this.handVisibilityFix) {
                if(slot == AccessorySlot.MAIN_HAND) {
                    AccessorySlot.MAIN_ARM.setVisibility(model, player, true);
                } else if(slot == AccessorySlot.OFF_HAND) {
                    AccessorySlot.OFF_ARM.setVisibility(model, player, true);
                }
            }
        } else {
            model.setAllVisible(true);
        }
    }

}
