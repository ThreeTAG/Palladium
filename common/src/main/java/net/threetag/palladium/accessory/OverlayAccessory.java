package net.threetag.palladium.accessory;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.PlayerModel;
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
import net.threetag.palladium.util.PlayerUtil;
import net.threetag.palladium.util.SkinTypedValue;
import net.threetag.palladium.util.context.DataContext;

import java.util.Objects;

public class OverlayAccessory extends DefaultAccessory {

    protected final TextureReference texture;
    protected final TextureReference textureSlim;
    protected boolean glowing = false;
    protected boolean onlyRenderSlot = false;
    protected boolean handVisibilityFix = false;

    public OverlayAccessory(TextureReference texture, TextureReference textureSlim) {
        this.texture = texture;
        this.textureSlim = textureSlim;
    }

    public OverlayAccessory(TextureReference texture) {
        this.texture = this.textureSlim = texture;
    }

    public OverlayAccessory(ResourceLocation texture, ResourceLocation textureSlim) {
        this(TextureReference.normal(texture), TextureReference.normal(textureSlim));
    }

    public OverlayAccessory(ResourceLocation texture) {
        this(TextureReference.normal(texture));
    }

    public OverlayAccessory(String texture, String textureSlim) {
        this(Palladium.id("textures/models/accessories/" + texture + ".png"),
                Palladium.id("textures/models/accessories/" + textureSlim + ".png"));
    }

    public OverlayAccessory(String texture) {
        this(Palladium.id("textures/models/accessories/" + texture + ".png"));
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
        ResourceLocation texture = (PlayerUtil.hasSmallArms(player) ? this.textureSlim : this.texture).getTexture(DataContext.forEntity(player));
        var buffer = bufferSource.getBuffer(this.glowing ? RenderType.eyes(texture) : Objects.requireNonNull(getRenderType(player, texture, renderLayerParent.getModel())));
        model.renderToBuffer(poseStack, buffer, packedLightIn, OverlayTexture.NO_OVERLAY, 1F, 1F, 1F, 1F);
    }

    @Environment(EnvType.CLIENT)
    @Override
    public void renderArm(HumanoidArm arm, AbstractClientPlayer player, PlayerRenderer playerRenderer, ModelPart armPart, ModelPart armWearPart, AccessorySlot slot, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        ResourceLocation texture = (PlayerUtil.hasSmallArms(player) ? this.textureSlim : this.texture).getTexture(DataContext.forEntity(player));
        var buffer = bufferSource.getBuffer(this.glowing ? RenderType.eyes(texture) : Objects.requireNonNull(getRenderType(player, texture, playerRenderer.getModel())));
        armPart.xRot = 0.0F;
        armPart.visible = true;
        armPart.render(poseStack, buffer, packedLight, OverlayTexture.NO_OVERLAY);
        armWearPart.xRot = 0.0F;
        armWearPart.visible = true;
        armWearPart.render(poseStack, buffer, packedLight, OverlayTexture.NO_OVERLAY);
    }

    @Environment(EnvType.CLIENT)
    public void setVisibility(HumanoidModel<?> model, AbstractClientPlayer player, AccessorySlot slot) {
        if (this.onlyRenderSlot) {
            model.setAllVisible(false);
            slot.getHiddenBodyParts(player).forEach(p -> p.setVisibility(model, true));

            if (this.handVisibilityFix) {
                if (slot == AccessorySlot.MAIN_HAND) {
                    AccessorySlot.MAIN_ARM.getHiddenBodyParts(player).forEach(p -> p.setVisibility(model, true));
                } else if (slot == AccessorySlot.OFF_HAND) {
                    AccessorySlot.OFF_ARM.getHiddenBodyParts(player).forEach(p -> p.setVisibility(model, true));
                }
            }
        } else {
            model.setAllVisible(true);
        }
    }

    public static class Serializer implements AccessoryParser.TypeSerializer {

        @Override
        public DefaultAccessory parse(JsonObject json) {
            SkinTypedValue<TextureReference> texture = SkinTypedValue.fromJSON(json.get("texture"), j -> TextureReference.parse(j.getAsString()));
            var accessory = new OverlayAccessory(texture.getNormal(), texture.getSlim());

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
            builder.setTitle("Overlay");
            builder.setDescription("Renders a texture skin-tight on the player");

            builder.addProperty("texture", TextureReference.class)
                    .description("Texture of the overlay. Can be skin-typed by specifying 'normal' and 'slim' in a json object.")
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
            return Palladium.id("overlay");
        }
    }

}
