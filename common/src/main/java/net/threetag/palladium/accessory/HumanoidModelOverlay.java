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
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.threetag.palladium.util.PlayerUtil;

import java.util.Objects;

public class HumanoidModelOverlay extends OverlayAccessory {

    private final ModelLayerLocation modelLayer, modelLayerSlim;
    private Object model, modelSlim;

    public HumanoidModelOverlay(ModelLayerLocation modelLayer, ResourceLocation texture, ResourceLocation textureSlim) {
        super(texture, textureSlim);
        this.modelLayer = this.modelLayerSlim = modelLayer;
    }

    public HumanoidModelOverlay(ModelLayerLocation modelLayer, ModelLayerLocation modelLayerSlim, ResourceLocation texture, ResourceLocation textureSlim) {
        super(texture, textureSlim);
        this.modelLayer = modelLayer;
        this.modelLayerSlim = modelLayerSlim;
    }

    public HumanoidModelOverlay(ModelLayerLocation modelLayer, ResourceLocation texture) {
        super(texture);
        this.modelLayer = this.modelLayerSlim = modelLayer;
    }

    public HumanoidModelOverlay(ModelLayerLocation modelLayer,ModelLayerLocation modelLayerSlim, ResourceLocation texture) {
        super(texture);
        this.modelLayer = modelLayer;
        this.modelLayerSlim = modelLayerSlim;
    }

    public HumanoidModelOverlay(ModelLayerLocation modelLayer, String texture, String textureSlim) {
        super(texture, textureSlim);
        this.modelLayer = this.modelLayerSlim = modelLayer;
    }

    public HumanoidModelOverlay(ModelLayerLocation modelLayer,ModelLayerLocation modelLayerSlim, String texture, String textureSlim) {
        super(texture, textureSlim);
        this.modelLayer = modelLayer;
        this.modelLayerSlim = modelLayerSlim;
    }

    public HumanoidModelOverlay(ModelLayerLocation modelLayer, String texture) {
        super(texture);
        this.modelLayer = this.modelLayerSlim = modelLayer;
    }

    public HumanoidModelOverlay(ModelLayerLocation modelLayer, ModelLayerLocation modelLayerSlim, String texture) {
        super(texture);
        this.modelLayer = modelLayer;
        this.modelLayerSlim = modelLayerSlim;
    }

    @Environment(EnvType.CLIENT)
    @Override
    public void onReload(EntityModelSet entityModelSet) {
        this.model = new HumanoidModel<>(entityModelSet.bakeLayer(this.modelLayer));
        this.modelSlim = new HumanoidModel<>(entityModelSet.bakeLayer(this.modelLayerSlim));
    }

    @SuppressWarnings("unchecked")
    @Environment(EnvType.CLIENT)
    @Override
    public void render(RenderLayerParent<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> renderLayerParent, AccessorySlot slot, PoseStack poseStack, MultiBufferSource bufferSource, int packedLightIn, AbstractClientPlayer player, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if(this.getModel(player) instanceof HumanoidModel model) {
            renderLayerParent.getModel().copyPropertiesTo(model);
            this.setVisibility(model, player, slot);
            ResourceLocation texture = PlayerUtil.hasSmallArms(player) ? this.textureSlim : this.texture;
            var buffer = bufferSource.getBuffer(this.glowing ? RenderType.eyes(texture) : Objects.requireNonNull(getRenderType(player, texture, renderLayerParent.getModel())));
            model.renderToBuffer(poseStack, buffer, packedLightIn, OverlayTexture.NO_OVERLAY, 1F, 1F, 1F, 1F);
        }
    }

    @Environment(EnvType.CLIENT)
    public Object getModel(AbstractClientPlayer player) {
        return PlayerUtil.hasSmallArms(player) ? this.modelSlim : this.model;
    }
}
