package net.threetag.palladium.compat.geckolib.layer;

import com.google.common.collect.ImmutableMap;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.HumanoidArm;
import net.threetag.palladium.client.renderer.entity.layer.PackRenderLayer;
import net.threetag.palladium.client.renderer.entity.layer.PackRenderLayerSerializer;
import net.threetag.palladium.client.renderer.entity.layer.PackRenderLayerTexture;
import net.threetag.palladium.client.texture.TextureReference;
import net.threetag.palladium.compat.geckolib.GeckoLibCompatClient;
import net.threetag.palladium.condition.PerspectiveAwareConditions;
import net.threetag.palladium.data.DataContext;
import net.threetag.palladium.documentation.CodecDocumentationBuilder;
import net.threetag.palladium.documentation.SettingType;
import net.threetag.palladium.entity.SkinTypedValue;
import net.threetag.palladium.util.CodecExtras;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoRenderer;

import java.util.*;

public class GeoRenderLayer extends PackRenderLayer<GeoRenderLayerState> {

    public static final Map<Bone, String> DEFAULT_BONES = ImmutableMap.<Bone, String>builder()
            .put(Bone.HEAD, "head")
            .put(Bone.BODY, "body")
            .put(Bone.RIGHT_ARM, "right_arm")
            .put(Bone.LEFT_ARM, "left_arm")
            .put(Bone.RIGHT_LEG, "right_leg")
            .put(Bone.LEFT_LEG, "left_leg")
            .build();

    public static final MapCodec<GeoRenderLayer> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            SkinTypedValue.codec(PackRenderLayerTexture.CODEC).fieldOf("texture").forGetter(l -> l.texture),
            SkinTypedValue.codec(TextureReference.CODEC).fieldOf("model").forGetter(l -> l.model),
            SkinTypedValue.codec(ResourceLocation.CODEC).optionalFieldOf("animations").forGetter(l -> Optional.ofNullable(l.animations)),
            CodecExtras.listOrPrimitive(AnimationControllerFactory.CODEC).optionalFieldOf("animation_controller", Collections.emptyList()).forGetter(l -> l.animationController),
            ExtraCodecs.intRange(0, 15).optionalFieldOf("light_emission", 0).forGetter(l -> l.lightEmission),
            Codec.unboundedMap(Bone.CODEC, Codec.STRING).optionalFieldOf("bones", DEFAULT_BONES).forGetter(l -> l.bones),
            conditionsCodec()
    ).apply(instance, (texture, model, animations, animController, light, bones, conditions) ->
            new GeoRenderLayer(texture, model, animations.orElse(null), animController, light, bones, conditions)));

    private static ResourceLocation CACHED_TEXTURE = null;
    private static ResourceLocation CACHED_MODEL = null;
    private static ResourceLocation CACHED_ANIMATIONS = null;

    private final SkinTypedValue<PackRenderLayerTexture> texture;
    private final SkinTypedValue<TextureReference> model;
    @Nullable
    private final SkinTypedValue<ResourceLocation> animations;
    protected final List<AnimationControllerFactory<?>> animationController;
    public GeoRenderLayerRenderer renderer;
    private final int lightEmission;
    private final Map<Bone, String> bones;

    public GeoRenderLayer(SkinTypedValue<PackRenderLayerTexture> texture, SkinTypedValue<TextureReference> model,
                          @Nullable SkinTypedValue<ResourceLocation> animations, List<AnimationControllerFactory<?>> animationController,
                          int lightEmission, Map<Bone, String> bones, PerspectiveAwareConditions conditions) {
        super(conditions);
        this.texture = texture;
        this.model = model;
        this.animations = animations;
        this.animationController = animationController;
        this.lightEmission = lightEmission;
        this.bones = bones;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void render(DataContext context, PoseStack poseStack, MultiBufferSource bufferSource, EntityModel<LivingEntityRenderState> parentModel, LivingEntityRenderState entityState, GeoRenderLayerState layerState, int packedLight, float partialTick, float xRot, float yRot) {
        if (this.renderer == null) {
            this.renderer = new GeoRenderLayerRenderer(this.bones, new GeoModel<>() {
                @Override
                public ResourceLocation getModelResource(GeoRenderLayerState animatable, @Nullable GeoRenderer<GeoRenderLayerState> renderer) {
                    return CACHED_MODEL;
                }

                @Override
                public ResourceLocation getTextureResource(GeoRenderLayerState animatable, @Nullable GeoRenderer<GeoRenderLayerState> renderer) {
                    return CACHED_TEXTURE;
                }

                @Override
                public ResourceLocation getAnimationResource(GeoRenderLayerState animatable) {
                    return CACHED_ANIMATIONS;
                }
            });
        }

        if (parentModel instanceof HumanoidModel<?> humanoidModel) {
            CACHED_TEXTURE = this.texture.get(context).getTexture(context);
            CACHED_MODEL = this.model.get(context).getTexture(context);
            CACHED_ANIMATIONS = this.animations != null ? this.animations.get(context) : null;

            this.renderer.prepForRender(layerState, context, humanoidModel, bufferSource, partialTick, yRot, xRot);
            humanoidModel.copyPropertiesTo(this.renderer);

            if (context.getSlot() != null) {
                this.renderer.applyBoneVisibilityBySlot(Objects.requireNonNull(context.getSlot().getEquipmentSlot()));
            }

            this.renderer.render(
                    poseStack,
                    bufferSource,
                    packedLight,
                    OverlayTexture.NO_OVERLAY,
                    LightTexture.lightCoordsWithEmission(packedLight, this.lightEmission)
            );
        }
    }

    @Override
    public void renderArm(DataContext context, PoseStack poseStack, MultiBufferSource bufferSource, HumanoidArm arm, ModelPart armPart, PlayerRenderer playerRenderer, GeoRenderLayerState layerState, int packedLight) {

    }

    @Override
    public PackRenderLayerSerializer<?> getSerializer() {
        return GeckoLibCompatClient.RENDER_LAYER;
    }

    @Override
    public PackRenderLayer.State createState(DataContext context) {
        return new GeoRenderLayerState(this, context);
    }

    public enum Bone implements StringRepresentable {

        HEAD("head"),
        BODY("body"),
        RIGHT_ARM("right_arm"),
        LEFT_ARM("left_arm"),
        RIGHT_LEG("right_leg"),
        LEFT_LEG("left_leg");

        public static final Codec<Bone> CODEC = StringRepresentable.fromEnum(Bone::values);

        private final String name;

        Bone(String name) {
            this.name = name;
        }

        @Override
        public @NotNull String getSerializedName() {
            return this.name;
        }
    }

    public static class Serializer extends PackRenderLayerSerializer<GeoRenderLayer> {

        @Override
        public MapCodec<GeoRenderLayer> codec() {
            return CODEC;
        }

        @Override
        public void addDocumentation(CodecDocumentationBuilder<PackRenderLayer<? extends State>, GeoRenderLayer> builder, HolderLookup.Provider provider) {
            builder.setName("Geo Render Layer")
                    .setDescription("Uses GeckoLib to render a geo model, including animations")
                    .add("texture", TYPE_ANY_TEXTURE, "The texture of the model")
                    .add("model", TYPE_RESOURCE_LOCATION, "The model to render")
                    .addOptional("animations", TYPE_RESOURCE_LOCATION, "The animations to use")
                    .addOptional("animation_controller", TYPE_GEO_ANIMATION_CONTROLLER, "The animation controllers to control or trigger the animations.")
                    .addOptional("light_emission", TYPE_INT, "The light emission of the model. Must be within 0 - 15", 0)
                    .addOptional("bones", SettingType.simple("Map<Bone, String>"), "The bones of the model. You can specify here what the bones for each body part are called in your model file.", DEFAULT_BONES.toString());
        }

    }

}
