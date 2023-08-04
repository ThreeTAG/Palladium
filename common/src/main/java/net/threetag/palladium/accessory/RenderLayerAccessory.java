package net.threetag.palladium.accessory;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.HumanoidArm;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.addonpack.log.AddonPackLog;
import net.threetag.palladium.addonpack.parser.AccessoryParser;
import net.threetag.palladium.client.renderer.renderlayer.IPackRenderLayer;
import net.threetag.palladium.client.renderer.renderlayer.PackRenderLayerManager;
import net.threetag.palladium.documentation.JsonDocumentationBuilder;
import net.threetag.palladium.util.context.DataContext;
import net.threetag.palladium.util.json.GsonUtil;

@SuppressWarnings({"unchecked", "rawtypes"})
public class RenderLayerAccessory extends DefaultAccessory {

    private final ResourceLocation renderLayerId;
    private Object renderLayer;

    public RenderLayerAccessory(ResourceLocation renderLayerId) {
        this.renderLayerId = renderLayerId;
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void onReload(EntityModelSet entityModelSet) {
        this.renderLayer = PackRenderLayerManager.getInstance().getLayer(this.renderLayerId);

        if (this.renderLayer == null) {
            AddonPackLog.warning("Unknown render layer used in accessory: " + this.renderLayerId);
        }
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void render(RenderLayerParent<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> renderLayerParent, AccessorySlot slot, PoseStack poseStack, MultiBufferSource bufferSource, int packedLightIn, AbstractClientPlayer player, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if (this.renderLayer instanceof IPackRenderLayer layer) {
            EntityModel entityModel = renderLayerParent.getModel();
            layer.render(DataContext.forEntity(player), poseStack, bufferSource, entityModel, packedLightIn, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch);
        }
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void renderArm(HumanoidArm arm, AbstractClientPlayer player, PlayerRenderer playerRenderer, ModelPart armPart, ModelPart armWearPart, AccessorySlot slot, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        if (this.renderLayer instanceof IPackRenderLayer layer) {
            layer.renderArm(DataContext.forEntity(player), arm, playerRenderer, poseStack, bufferSource, packedLight);
        }
    }

    public static class Serializer implements AccessoryParser.TypeSerializer {

        @Override
        public DefaultAccessory parse(JsonObject json) {
            return new RenderLayerAccessory(GsonUtil.getAsResourceLocation(json, "render_layer"));
        }

        @Override
        public void generateDocumentation(JsonDocumentationBuilder builder) {
            builder.setTitle("Render Layer");
            builder.setDescription("Let's you use a render layer as an accessory");

            builder.addProperty("render_layer", ResourceLocation.class)
                    .description("ID of the render layer that's being used")
                    .required().exampleJson(new JsonPrimitive("namespace:example_layer"));

            AccessoryParser.addSlotDocumentation(builder);
        }

        @Override
        public ResourceLocation getId() {
            return Palladium.id("render_layer");
        }
    }
}
