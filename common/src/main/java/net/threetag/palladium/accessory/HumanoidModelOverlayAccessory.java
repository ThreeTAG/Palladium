package net.threetag.palladium.accessory;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.HumanoidArm;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.addonpack.parser.AccessoryParser;
import net.threetag.palladium.client.dynamictexture.TextureReference;
import net.threetag.palladium.documentation.JsonDocumentationBuilder;
import net.threetag.palladium.util.ModelLayerLocationUtil;
import net.threetag.palladium.util.PlayerUtil;
import net.threetag.palladium.util.SkinTypedValue;
import net.threetag.palladium.util.context.DataContext;
import net.threetag.palladium.util.json.GsonUtil;

import java.util.Objects;
import java.util.function.Supplier;

@SuppressWarnings({"rawtypes", "PatternVariableHidesField"})
public class HumanoidModelOverlayAccessory extends OverlayAccessory {

    private final Supplier<Object> modelLayer, modelLayerSlim;
    private Object model, modelSlim;

    public HumanoidModelOverlayAccessory(Supplier<Object> modelLayer, TextureReference texture, TextureReference textureSlim) {
        super(texture, textureSlim);
        this.modelLayer = this.modelLayerSlim = modelLayer;
    }

    public HumanoidModelOverlayAccessory(Supplier<Object> modelLayer, Supplier<Object> modelLayerSlim, TextureReference texture, TextureReference textureSlim) {
        super(texture, textureSlim);
        this.modelLayer = modelLayer;
        this.modelLayerSlim = modelLayerSlim;
    }

    public HumanoidModelOverlayAccessory(Supplier<Object> modelLayer, TextureReference texture) {
        super(texture);
        this.modelLayer = this.modelLayerSlim = modelLayer;
    }

    public HumanoidModelOverlayAccessory(Supplier<Object> modelLayer, Supplier<Object> modelLayerSlim, TextureReference texture) {
        super(texture);
        this.modelLayer = modelLayer;
        this.modelLayerSlim = modelLayerSlim;
    }

    public HumanoidModelOverlayAccessory(Supplier<Object> modelLayer, ResourceLocation texture, ResourceLocation textureSlim) {
        super(texture, textureSlim);
        this.modelLayer = this.modelLayerSlim = modelLayer;
    }

    public HumanoidModelOverlayAccessory(Supplier<Object> modelLayer, Supplier<Object> modelLayerSlim, ResourceLocation texture, ResourceLocation textureSlim) {
        super(texture, textureSlim);
        this.modelLayer = modelLayer;
        this.modelLayerSlim = modelLayerSlim;
    }

    public HumanoidModelOverlayAccessory(Supplier<Object> modelLayer, ResourceLocation texture) {
        super(texture);
        this.modelLayer = this.modelLayerSlim = modelLayer;
    }

    public HumanoidModelOverlayAccessory(Supplier<Object> modelLayer, Supplier<Object> modelLayerSlim, ResourceLocation texture) {
        super(texture);
        this.modelLayer = modelLayer;
        this.modelLayerSlim = modelLayerSlim;
    }

    public HumanoidModelOverlayAccessory(Supplier<Object> modelLayer, String texture, String textureSlim) {
        super(texture, textureSlim);
        this.modelLayer = this.modelLayerSlim = modelLayer;
    }

    public HumanoidModelOverlayAccessory(Supplier<Object> modelLayer, Supplier<Object> modelLayerSlim, String texture, String textureSlim) {
        super(texture, textureSlim);
        this.modelLayer = modelLayer;
        this.modelLayerSlim = modelLayerSlim;
    }

    public HumanoidModelOverlayAccessory(Supplier<Object> modelLayer, String texture) {
        super(texture);
        this.modelLayer = this.modelLayerSlim = modelLayer;
    }

    public HumanoidModelOverlayAccessory(Supplier<Object> modelLayer, Supplier<Object> modelLayerSlim, String texture) {
        super(texture);
        this.modelLayer = modelLayer;
        this.modelLayerSlim = modelLayerSlim;
    }

    @Environment(EnvType.CLIENT)
    @Override
    public void onReload(EntityModelSet entityModelSet) {
        Object loc = this.modelLayer.get();
        Object locSlim = this.modelLayerSlim.get();

        if (loc instanceof ModelLayerLocation loc1) {
            this.model = new HumanoidModel<>(entityModelSet.bakeLayer(loc1));
        }

        if (locSlim instanceof ModelLayerLocation loc1) {
            this.modelSlim = new HumanoidModel<>(entityModelSet.bakeLayer(loc1));
        }
    }

    @SuppressWarnings("unchecked")
    @Environment(EnvType.CLIENT)
    @Override
    public void render(RenderLayerParent<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> renderLayerParent, AccessorySlot slot, PoseStack poseStack, MultiBufferSource bufferSource, int packedLightIn, AbstractClientPlayer player, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if (this.getModel(player) instanceof HumanoidModel model) {
            renderLayerParent.getModel().copyPropertiesTo(model);
            this.setVisibility(model, player, slot);
            ResourceLocation texture = (PlayerUtil.hasSmallArms(player) ? this.textureSlim : this.texture).getTexture(DataContext.forEntity(player));
            var buffer = bufferSource.getBuffer(this.glowing ? RenderType.eyes(texture) : Objects.requireNonNull(getRenderType(player, texture, renderLayerParent.getModel())));
            model.renderToBuffer(poseStack, buffer, packedLightIn, OverlayTexture.NO_OVERLAY, 1F, 1F, 1F, 1F);
        }
    }

    @Environment(EnvType.CLIENT)
    @Override
    public void renderArm(HumanoidArm arm, AbstractClientPlayer player, PlayerRenderer playerRenderer, ModelPart armPart, ModelPart armWearPart, AccessorySlot slot, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        if (this.getModel(player) instanceof HumanoidModel model) {
            ResourceLocation texture = (PlayerUtil.hasSmallArms(player) ? this.textureSlim : this.texture).getTexture(DataContext.forEntity(player));
            var buffer = bufferSource.getBuffer(this.glowing ? RenderType.eyes(texture) : Objects.requireNonNull(getRenderType(player, texture, playerRenderer.getModel())));
            model.rightArm.copyFrom(armPart);
            model.rightArm.visible = true;
            model.rightArm.render(poseStack, buffer, packedLight, OverlayTexture.NO_OVERLAY);
        }
    }

    @Environment(EnvType.CLIENT)
    public Object getModel(AbstractClientPlayer player) {
        return PlayerUtil.hasSmallArms(player) ? this.modelSlim : this.model;
    }

    public static class Serializer implements AccessoryParser.TypeSerializer {

        @Override
        public DefaultAccessory parse(JsonObject json) {
            SkinTypedValue<TextureReference> texture = SkinTypedValue.fromJSON(json.get("texture"), j -> TextureReference.parse(j.getAsString()));
            SkinTypedValue<ModelLayerLocationUtil> modelLayer = SkinTypedValue.fromJSON(json.get("model_layer"), j -> GsonUtil.convertToModelLayerLocationUtil(j, "model_layer"));
            var accessory = new HumanoidModelOverlayAccessory(modelLayer::getNormal, modelLayer::getSlim, texture.getNormal(), texture.getSlim());

            if (GsonHelper.getAsBoolean(json, "glowing", false)) {
                accessory.glowing();
            }

            if (GsonHelper.getAsBoolean(json, "only_render_for_slot", false)) {
                accessory.onlyRenderSlot();
            }

            if (GsonHelper.getAsBoolean(json, "hand_visibility_fix", false)) {
                accessory.handVisibilityFix();
            }

            return accessory;
        }

        @Override
        public void generateDocumentation(JsonDocumentationBuilder builder) {
            builder.setTitle("Humanoid Model Layer");
            builder.setDescription("Renders a humanoid model layer on the player");

            builder.addProperty("texture", TextureReference.class)
                    .description("Texture for the accessory. Can be skin-typed by specifying 'normal' and 'slim' in a json object.")
                    .required().exampleJson(new JsonPrimitive("example:textures/accessory/test.png"));

            builder.addProperty("model_layer", ModelLayerLocationUtil.class)
                    .description("Model layer for the accessory. Can be skin-typed by specifying 'normal' and 'slim' in a json object.")
                    .required().exampleJson(new JsonPrimitive("example:textures/accessory/test.png"));

            builder.addProperty("glowing", Boolean.class)
                    .description("Makes the overlay glow")
                    .fallback(false).exampleJson(new JsonPrimitive(false));

            builder.addProperty("only_render_for_slot", Boolean.class)
                    .description("If set to true, the texture will only render for the specified slot of the accessory. Example: The texture has a full player skin and the slot is set to 'head', only the head part of it will render.")
                    .fallback(false).exampleJson(new JsonPrimitive(false));

            builder.addProperty("hand_visibility_fix", Boolean.class)
                    .description("If 'only_render_for_slot' is set to true while the slot is for a hand but you want to have the accessory on the arm, set this to true.")
                    .fallback(false).exampleJson(new JsonPrimitive(false));

            AccessoryParser.addSlotDocumentation(builder);
        }

        @Override
        public ResourceLocation getId() {
            return Palladium.id("humanoid_model_layer");
        }
    }
}
