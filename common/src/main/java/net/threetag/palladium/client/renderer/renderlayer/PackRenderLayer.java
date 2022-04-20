package net.threetag.palladium.client.renderer.renderlayer;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.client.dynamictexture.DynamicTexture;
import net.threetag.palladium.power.ability.AbilityEntry;
import net.threetag.palladium.util.SkinTypedValue;
import net.threetag.palladium.util.json.GsonUtil;

import java.util.function.BiFunction;

public class PackRenderLayer implements IPackRenderLayer {

    private final SkinTypedValue<ModelLookup.Model> modelLookup;
    private final SkinTypedValue<EntityModel<LivingEntity>> model;
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
    public void render(LivingEntity entity, AbilityEntry abilityEntry, PoseStack poseStack, MultiBufferSource bufferSource, EntityModel<LivingEntity> parentModel, int packedLight, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if (this.modelLookup.get(entity).fitsEntity(entity, parentModel)) {
            EntityModel<LivingEntity> entityModel = this.model.get(entity);

            if (entityModel instanceof HumanoidModel<LivingEntity> entityHumanoidModel && parentModel instanceof HumanoidModel<LivingEntity> parentHumanoid) {
                parentHumanoid.copyPropertiesTo(entityHumanoidModel);
            }

            parentModel.copyPropertiesTo(entityModel);
            entityModel.prepareMobModel(entity, limbSwing, limbSwingAmount, partialTicks);
            entityModel.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);

            // TODO apply enchant glint when item is enchanted
            VertexConsumer vertexConsumer = this.renderType.apply(bufferSource, this.texture.get(entity).getTexture(entity));
            entityModel.renderToBuffer(poseStack, vertexConsumer, packedLight, OverlayTexture.NO_OVERLAY, 1F, 1F, 1F, 1F);
        }
    }

    public static PackRenderLayer parse(JsonObject json) {
        SkinTypedValue<ModelLayerLocation> location = SkinTypedValue.fromJSON(json.get("model_layer"), js -> GsonUtil.convertToModelLayerLocation(js, "model_layer"));
        var renderType = PackRenderLayerManager.getRenderType(new ResourceLocation(GsonHelper.getAsString(json, "render_type", "solid")));

        SkinTypedValue<ModelLookup.Model> model;
        if (GsonHelper.isValidNode(json, "model")) {
            model = SkinTypedValue.fromJSON(json.get("model"), jsonElement -> {
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

        return new PackRenderLayer(model, location, SkinTypedValue.fromJSON(json.get("texture"), DynamicTexture::parse), renderType);
    }

}
