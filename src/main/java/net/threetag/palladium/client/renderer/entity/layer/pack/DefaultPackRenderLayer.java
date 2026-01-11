package net.threetag.palladium.client.renderer.entity.layer.pack;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.player.AvatarRenderer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.Unit;
import net.minecraft.world.entity.HumanoidArm;
import net.threetag.palladium.client.model.ModelLayerLocationCodec;
import net.threetag.palladium.client.renderer.RenderTypeFunction;
import net.threetag.palladium.client.renderer.RenderTypeRegistry;
import net.threetag.palladium.client.util.ModelUtil;
import net.threetag.palladium.client.util.PerspectiveAwareConditions;
import net.threetag.palladium.documentation.CodecDocumentationBuilder;
import net.threetag.palladium.documentation.SettingType;
import net.threetag.palladium.entity.SkinTypedValue;
import net.threetag.palladium.logic.context.DataContext;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class DefaultPackRenderLayer extends PackRenderLayer<PackRenderLayer.State> {

    public static final MapCodec<DefaultPackRenderLayer> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            SkinTypedValue.codec(ModelLayerLocationCodec.CODEC).optionalFieldOf("model_layer").forGetter(l -> Optional.ofNullable(l.modelLayers)),
            SkinTypedValue.codec(PackRenderLayerTexture.CODEC).fieldOf("texture").forGetter(l -> l.textures),
            RenderTypeRegistry.CODEC.optionalFieldOf("render_type", RenderTypeRegistry.ENTITY_TRANSLUCENT).forGetter(l -> l.renderType),
            ExtraCodecs.intRange(0, 15).optionalFieldOf("light_emission", 0).forGetter(l -> l.lightEmission),
            PackRenderLayerAnimation.CODEC.optionalFieldOf("animations", PackRenderLayerAnimation.EMPTY).forGetter(l -> l.animations),
            propertiesCodec(), conditionsCodec()
    ).apply(instance, (modelLayers, textures, renderType, lightEmission, animations, properties, conditions) -> {
        return new DefaultPackRenderLayer(modelLayers.orElse(null), textures, renderType, lightEmission, animations, properties, conditions);
    }));

    @Nullable
    private final SkinTypedValue<ModelLayerLocationCodec> modelLayers;
    private SkinTypedValue<Model.Simple> model;
    private final SkinTypedValue<PackRenderLayerTexture> textures;
    private final RenderTypeFunction renderType;
    private final int lightEmission;
    private final PackRenderLayerAnimation animations;

    public DefaultPackRenderLayer(@Nullable SkinTypedValue<ModelLayerLocationCodec> modelLayers, SkinTypedValue<PackRenderLayerTexture> textures, RenderTypeFunction renderType, int lightEmission, PackRenderLayerAnimation animations, PackRenderLayerProperties properties, PerspectiveAwareConditions conditions) {
        super(properties, conditions);

        this.modelLayers = modelLayers;
        this.textures = textures;
        this.renderType = renderType;
        this.lightEmission = lightEmission;
        this.animations = animations;
    }

    @Override
    public PackRenderLayerSerializer<?> getSerializer() {
        return PackRenderLayerSerializers.DEFAULT;
    }

    private void buildModels() {
        if (this.modelLayers != null) {
            var entityModels = Minecraft.getInstance().getEntityModels();
            this.model = new SkinTypedValue<>(
                    new Model.Simple(entityModels.bakeLayer(this.modelLayers.getWide().get()), RenderTypes::entityTranslucent),
                    new Model.Simple(entityModels.bakeLayer(this.modelLayers.getSlim().get()), RenderTypes::entityTranslucent)
            );
        }
    }

    @Override
    public void submit(DataContext context, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, EntityModel<LivingEntityRenderState> parentModel, LivingEntityRenderState state, State layerState, int packedLight, float partialTick, float xRot, float yRot) {
        var entity = context.getEntity();

        if (this.modelLayers != null) {
            if (this.model == null) {
                this.buildModels();
            }

            Model.Simple model = this.model.get(entity);
            for (ModelPart modelPart : model.allParts()) {
                modelPart.resetPose();
                modelPart.visible = true;
                modelPart.skipDraw = false;
            }
            mimicModelParts(parentModel.root(), model.root());

            this.animations.animate(model, context, partialTick);

            submitNodeCollector.submitModel(
                    model,
                    Unit.INSTANCE,
                    poseStack,
                    this.renderType.getRenderType(this.textures.get(entity).getTexture(context)),
                    LightTexture.lightCoordsWithEmission(packedLight, this.lightEmission),
                    OverlayTexture.NO_OVERLAY,
                    state.outlineColor,
                    null);

            if (context.getItem().hasFoil()) {
                submitNodeCollector.submitModel(
                        model,
                        Unit.INSTANCE,
                        poseStack,
                        RenderTypes.armorEntityGlint(),
                        LightTexture.lightCoordsWithEmission(packedLight, this.lightEmission),
                        OverlayTexture.NO_OVERLAY,
                        state.outlineColor,
                        null);
            }
        } else {
            for (ModelPart modelPart : parentModel.allParts()) {
                modelPart.visible = true;
                modelPart.skipDraw = false;
            }

            this.animations.animate(parentModel, context, partialTick);

            submitNodeCollector.submitModel(
                    parentModel,
                    state,
                    poseStack,
                    this.renderType.getRenderType(this.textures.get(entity).getTexture(context)),
                    LightTexture.lightCoordsWithEmission(packedLight, this.lightEmission),
                    OverlayTexture.NO_OVERLAY,
                    state.outlineColor,
                    null);

            if (context.getItem().hasFoil()) {
                submitNodeCollector.submitModel(
                        parentModel,
                        state,
                        poseStack,
                        RenderTypes.armorEntityGlint(),
                        LightTexture.lightCoordsWithEmission(packedLight, this.lightEmission),
                        OverlayTexture.NO_OVERLAY,
                        state.outlineColor,
                        null);
            }
        }
    }

    @Override
    public void submitArm(DataContext context, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, HumanoidArm arm, ModelPart armPart, AvatarRenderer<?> playerRenderer, State layerState, int packedLight) {
        var entity = context.getEntity();
        if (this.modelLayers != null) {
            if (this.model == null) {
                this.buildModels();
            }

            Model.Simple model = this.model.get(entity);
            for (ModelPart modelPart : model.allParts()) {
                modelPart.resetPose();
                modelPart.visible = true;
                modelPart.skipDraw = false;
            }
            var partName = arm == HumanoidArm.RIGHT ? "rightArm" : "leftArm";

            if (model.root().hasChild(partName)) {
                var foundPart = model.root().getChild(partName);
                ModelUtil.copyProperties(armPart, foundPart);
                armPart = foundPart;
            } else {
                return;
            }

            this.animations.animate(model, context, Minecraft.getInstance().getDeltaTracker().getGameTimeDeltaTicks());
        } else {
            armPart.visible = true;
            armPart.skipDraw = false;

            this.animations.animate(playerRenderer.getModel(), context, Minecraft.getInstance().getDeltaTracker().getGameTimeDeltaTicks());
        }

        submitNodeCollector.submitModelPart(armPart, poseStack,
                this.renderType.getRenderType(this.textures.get(entity).getTexture(context)),
                LightTexture.lightCoordsWithEmission(packedLight, this.lightEmission),
                OverlayTexture.NO_OVERLAY,
                null);

        if (context.getItem().hasFoil()) {
            submitNodeCollector.submitModelPart(armPart, poseStack,
                    RenderTypes.armorEntityGlint(),
                    LightTexture.lightCoordsWithEmission(packedLight, this.lightEmission),
                    OverlayTexture.NO_OVERLAY,
                    null);
        }
    }

    private static void mimicModelParts(ModelPart source, ModelPart target) {
        ModelUtil.copyProperties(source, target);

        source.children.forEach((name, modelPart) -> {
            if (target.hasChild(name)) {
                mimicModelParts(modelPart, target.getChild(name));
            }
        });
    }

    public static class Serializer extends PackRenderLayerSerializer<DefaultPackRenderLayer> {

        @Override
        public MapCodec<DefaultPackRenderLayer> codec() {
            return CODEC;
        }

        @Override
        public void addDocumentation(CodecDocumentationBuilder<PackRenderLayer<? extends State>, DefaultPackRenderLayer> builder, HolderLookup.Provider provider) {
            builder.setName("Default Render Layer")
                    .setDescription("Default render layer that renders a model with a texture.")
                    .addOptional("model_layer", TYPE_RESOURCE_LOCATION, "The model layer to render.", "If not present, the model of the parent entity will be used.")
                    .add("texture", TYPE_ANY_TEXTURE, "The texture to render the model with.")
                    .addOptional("render_type", SettingType.enumList(RenderTypeRegistry.types().stream().map(Identifier::toString).toList()), "The render type to render the model with.", RenderTypeRegistry.getKey(RenderTypeRegistry.ENTITY_TRANSLUCENT))
                    .addOptional("light_emission", TYPE_INT, "The light emission of the model. Must be within 0 - 15", 0)
                    .setExampleObject(new DefaultPackRenderLayer(
                            new SkinTypedValue<>(ModelLayerLocationCodec.parse("example:wide_model"), ModelLayerLocationCodec.parse("example:slim_model")),
                            new SkinTypedValue<>(new PackRenderLayerTexture(Identifier.fromNamespaceAndPath("example", "wide_texture")), new PackRenderLayerTexture(Identifier.fromNamespaceAndPath("example", "slim_texture"))),
                            RenderTypeRegistry.ENTITY_TRANSLUCENT,
                            5,
                            PackRenderLayerAnimation.EMPTY,
                            PackRenderLayerProperties.DEFAULT,
                            PerspectiveAwareConditions.EMPTY
                    ));
        }
    }

}
