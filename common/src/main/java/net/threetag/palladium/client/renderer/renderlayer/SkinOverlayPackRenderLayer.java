package net.threetag.palladium.client.renderer.renderlayer;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.HumanoidArm;
import net.threetag.palladium.client.dynamictexture.DynamicTexture;
import net.threetag.palladium.client.dynamictexture.DynamicTextureManager;
import net.threetag.palladium.client.renderer.DynamicColor;
import net.threetag.palladium.util.SkinTypedValue;
import net.threetag.palladium.util.context.DataContext;

import java.awt.*;

public class SkinOverlayPackRenderLayer extends AbstractPackRenderLayer {

    private final SkinTypedValue<DynamicTexture> texture;
    private final RenderTypeFunction renderType;
    private final DynamicColor tint;

    public SkinOverlayPackRenderLayer(SkinTypedValue<DynamicTexture> texture, RenderTypeFunction renderType, DynamicColor tint) {
        this.texture = texture;
        this.renderType = renderType;
        this.tint = tint;
    }

    @Override
    public void render(DataContext context, PoseStack poseStack, MultiBufferSource bufferSource, EntityModel<Entity> parentModel, int packedLight, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        var entity = context.getEntity();
        if (IPackRenderLayer.conditionsFulfilled(entity, this.conditions, this.thirdPersonConditions)) {
            var tint = Color.WHITE;

            if (this.tint != null) {
                tint = this.tint.getColor(context);
            }

            VertexConsumer vertexConsumer = this.renderType.createVertexConsumer(bufferSource, this.texture.get(entity).getTexture(context), context.getItem().hasFoil());
            parentModel.renderToBuffer(
                    poseStack,
                    vertexConsumer,
                    this.renderType.getPackedLight(packedLight),
                    OverlayTexture.NO_OVERLAY,
                    tint.getRed() / 255F,
                    tint.getGreen() / 255F,
                    tint.getBlue() / 255F,
                    tint.getAlpha() / 255F
            );
        }
    }

    @Override
    public void renderArm(DataContext context, HumanoidArm arm, PlayerRenderer playerRenderer, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        var player = context.getEntity();
        if (IPackRenderLayer.conditionsFulfilled(player, this.conditions, this.firstPersonConditions)) {
            PlayerModel<AbstractClientPlayer> entityModel = playerRenderer.getModel();
            VertexConsumer vertexConsumer = this.renderType.createVertexConsumer(bufferSource, this.texture.get(player).getTexture(context), context.getItem().hasFoil());

            entityModel.attackTime = 0.0F;
            entityModel.crouching = false;
            entityModel.swimAmount = 0.0F;

            var tint = Color.WHITE;

            if (this.tint != null) {
                tint = this.tint.getColor(context);
            }

            ModelPart armToRender;

            if (arm == HumanoidArm.RIGHT) {
                armToRender = entityModel.rightArm;
            } else {
                armToRender = entityModel.leftArm;
            }

            armToRender.xRot = 0.0F;
            armToRender.render(
                    poseStack,
                    vertexConsumer,
                    this.renderType.getPackedLight(packedLight),
                    OverlayTexture.NO_OVERLAY,
                    tint.getRed() / 255F,
                    tint.getGreen() / 255F,
                    tint.getBlue() / 255F,
                    tint.getAlpha() / 255F
            );
        }
    }

    public static SkinOverlayPackRenderLayer parse(JsonObject json) {
        var renderType = PackRenderLayerManager.getRenderType(new ResourceLocation(GsonHelper.getAsString(json, "render_type", "solid")));

        if (renderType == null) {
            throw new JsonParseException("Unknown render type '" + new ResourceLocation(GsonHelper.getAsString(json, "render_type", "solid")) + "'");
        }

        return new SkinOverlayPackRenderLayer(
                SkinTypedValue.fromJSON(json.get("texture"), DynamicTextureManager::fromJson),
                renderType,
                DynamicColor.getFromJson(json, "tint", null)
        );
    }
}
