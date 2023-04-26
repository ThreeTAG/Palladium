package net.threetag.palladium.client.renderer.renderlayer;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.HumanoidArm;
import net.threetag.palladium.addonpack.log.AddonPackLog;
import net.threetag.palladium.client.dynamictexture.DynamicTexture;
import net.threetag.palladium.entity.BodyPart;
import net.threetag.palladium.util.SkinTypedValue;
import net.threetag.palladium.util.json.GsonUtil;

import java.util.function.BiFunction;

@SuppressWarnings({"rawtypes", "unchecked"})
public class PackRenderLayer extends AbstractPackRenderLayer {

    private final SkinTypedValue<ModelLookup.Model> modelLookup;
    private final SkinTypedValue<EntityModel<Entity>> model;
    private final SkinTypedValue<DynamicTexture> texture;
    private final BiFunction<MultiBufferSource, ResourceLocation, VertexConsumer> renderType;

    @SuppressWarnings({"unchecked", "rawtypes"})
    public PackRenderLayer(SkinTypedValue<ModelLookup.Model> model, SkinTypedValue<ModelLayerLocation> modelLayerLocation, SkinTypedValue<DynamicTexture> texture, BiFunction<MultiBufferSource, ResourceLocation, VertexConsumer> renderType) {
        this.modelLookup = model;
        this.model = new SkinTypedValue(model.getNormal().getModel(Minecraft.getInstance().getEntityModels().bakeLayer(modelLayerLocation.getNormal())),
                model.getSlim().getModel(Minecraft.getInstance().getEntityModels().bakeLayer(modelLayerLocation.getSlim())));
        this.texture = texture;
        this.renderType = renderType;
    }

    @Override
    public void render(IRenderLayerContext context, PoseStack poseStack, MultiBufferSource bufferSource, EntityModel<Entity> parentModel, int packedLight, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        var entity = context.getEntity();
        if (IPackRenderLayer.conditionsFulfilled(entity, this.conditions, this.thirdPersonConditions) && this.modelLookup.get(entity).fitsEntity(entity, parentModel)) {
            EntityModel<Entity> entityModel = this.model.get(entity);

            if (entityModel instanceof HumanoidModel entityHumanoidModel && parentModel instanceof HumanoidModel parentHumanoid) {
                IPackRenderLayer.copyModelProperties(entity, parentHumanoid, entityHumanoidModel);
            }

            // TODO apply enchant glint when item is enchanted
            VertexConsumer vertexConsumer = this.renderType.apply(bufferSource, this.texture.get(entity).getTexture(entity));
            entityModel.renderToBuffer(poseStack, vertexConsumer, packedLight, OverlayTexture.NO_OVERLAY, 1F, 1F, 1F, 1F);
        }
    }

    @Override
    public void renderArm(IRenderLayerContext context, HumanoidArm arm, PlayerRenderer playerRenderer, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        var player = context.getEntity();
        if (IPackRenderLayer.conditionsFulfilled(player, this.conditions, this.firstPersonConditions) && this.modelLookup.get(player).fitsEntity(player, playerRenderer.getModel())) {
            EntityModel<Entity> entityModel = this.model.get(player);

            if (entityModel instanceof HumanoidModel humanoidModel) {
                playerRenderer.getModel().copyPropertiesTo(humanoidModel);
                VertexConsumer vertexConsumer = this.renderType.apply(bufferSource, this.texture.get(player).getTexture(player));

                humanoidModel.attackTime = 0.0F;
                humanoidModel.crouching = false;
                humanoidModel.swimAmount = 0.0F;

                if (arm == HumanoidArm.RIGHT) {
                    humanoidModel.rightArm.xRot = 0.0F;
                    humanoidModel.rightArm.render(poseStack, vertexConsumer, packedLight, OverlayTexture.NO_OVERLAY);
                } else {
                    humanoidModel.leftArm.xRot = 0.0F;
                    humanoidModel.leftArm.render(poseStack, vertexConsumer, packedLight, OverlayTexture.NO_OVERLAY);
                }
            }
        }
    }

    public static PackRenderLayer parse(JsonObject json) {
        SkinTypedValue<ModelLayerLocation> location = SkinTypedValue.fromJSON(json.get("model_layer"), js -> GsonUtil.convertToModelLayerLocation(js, "model_layer"));
        var renderType = PackRenderLayerManager.getRenderType(new ResourceLocation(GsonHelper.getAsString(json, "render_type", "solid")));

        SkinTypedValue<ModelLookup.Model> model;
        String modelTypeKey = "model_type";

        if (!json.has(modelTypeKey) && json.has("model")) {
            AddonPackLog.warning("Deprecated use of 'model' in render layer. Please switch to 'model_type'!");
            modelTypeKey = "model";
        }

        if (GsonHelper.isValidNode(json, modelTypeKey)) {
            model = SkinTypedValue.fromJSON(json.get(modelTypeKey), jsonElement -> {
                ResourceLocation modelId = new ResourceLocation(jsonElement.getAsString());
                ModelLookup.Model m = ModelLookup.get(modelId);

                if (m == null) {
                    throw new JsonParseException("Unknown model type '" + modelId + "'");
                }

                return m;
            });
        } else {
            model = new SkinTypedValue<>(ModelLookup.HUMANOID);
        }

        if (renderType == null) {
            throw new JsonParseException("Unknown render type '" + new ResourceLocation(GsonHelper.getAsString(json, "render_type", "solid")) + "'");
        }

        var layer = new PackRenderLayer(model, location, SkinTypedValue.fromJSON(json.get("texture"), DynamicTexture::parse), renderType);

        GsonUtil.ifHasKey(json, "hidden_body_parts", el -> {
            if (el.isJsonPrimitive()) {
                var string = el.getAsString();
                if (string.equalsIgnoreCase("all")) {
                    for (BodyPart bodyPart : BodyPart.values()) {
                        layer.addHiddenBodyPart(bodyPart);
                    }
                } else {
                    layer.addHiddenBodyPart(BodyPart.fromJson(string));
                }
            } else if (el.isJsonArray()) {
                JsonArray jsonArray = el.getAsJsonArray();
                for (JsonElement jsonElement : jsonArray) {
                    layer.addHiddenBodyPart(BodyPart.fromJson(jsonElement.getAsString()));
                }
            } else {
                throw new JsonParseException("hidden_body_parts setting must either be a string or an array");
            }
        });

        return IPackRenderLayer.parseConditions(layer, json);
    }

}
