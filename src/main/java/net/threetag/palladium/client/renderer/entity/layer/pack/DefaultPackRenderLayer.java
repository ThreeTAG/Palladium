package net.threetag.palladium.client.renderer.entity.layer.pack;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.zigythebird.playeranim.accessors.IAvatarAnimationState;
import com.zigythebird.playeranim.animation.AvatarAnimManager;
import com.zigythebird.playeranim.util.RenderUtil;
import com.zigythebird.playeranimcore.bones.PlayerAnimBone;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.player.AvatarRenderer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.Unit;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.HumanoidArm;
import net.threetag.palladium.client.animation.ModelAnimationDefinition;
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
import org.joml.Quaternionf;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class DefaultPackRenderLayer extends PackRenderLayer<PackRenderLayer.State> {

    public static final MapCodec<DefaultPackRenderLayer> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            SkinTypedValue.codec(ModelLayerLocationCodec.CODEC).optionalFieldOf("model_layer").forGetter(l -> Optional.ofNullable(l.modelLayers)),
            SkinTypedValue.codec(PackRenderLayerTexture.CODEC).fieldOf("texture").forGetter(l -> l.textures),
            RenderTypeRegistry.CODEC.optionalFieldOf("render_type", RenderTypeRegistry.ENTITY_TRANSLUCENT).forGetter(l -> l.renderType),
            ExtraCodecs.intRange(0, 15).optionalFieldOf("light_emission", 0).forGetter(l -> l.lightEmission),
            ModelAnimationDefinition.CODEC.optionalFieldOf("overrides").forGetter(l -> Optional.ofNullable(l.overrides)),
            propertiesCodec(), conditionsCodec()
    ).apply(instance, (modelLayers, textures, renderType, lightEmission, overrides, properties, conditions) -> {
        return new DefaultPackRenderLayer(modelLayers.orElse(null), textures, renderType, lightEmission, overrides.orElse(null), properties, conditions);
    }));

    @Nullable
    private final SkinTypedValue<ModelLayerLocationCodec> modelLayers;
    private SkinTypedValue<Model.Simple> model;
    private final SkinTypedValue<PackRenderLayerTexture> textures;
    private final RenderTypeFunction renderType;
    private final int lightEmission;
    private final ModelAnimationDefinition overrides;

    public DefaultPackRenderLayer(@Nullable SkinTypedValue<ModelLayerLocationCodec> modelLayers, SkinTypedValue<PackRenderLayerTexture> textures, RenderTypeFunction renderType, int lightEmission, ModelAnimationDefinition overrides, PackRenderLayerProperties properties, PerspectiveAwareConditions conditions) {
        super(properties, conditions);

        this.modelLayers = modelLayers;
        this.textures = textures;
        this.renderType = renderType;
        this.lightEmission = lightEmission;
        this.overrides = overrides;
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
            mimicModelParts("root", parentModel.root(), model.root(), state, true);

            if (state instanceof IAvatarAnimationState animationState) {
                AvatarAnimManager emote = animationState.playerAnimLib$getAnimManager();
                if (emote != null && emote.isActive()) {
                    for (Map.Entry<String, ModelPart> e : model.root().children.entrySet()) {
                        animate(e.getValue(), e.getKey().equalsIgnoreCase("body") ? "torso" : e.getKey(), emote);
                    }
                }
            }

            if (this.overrides != null) {
                this.overrides.animate(model, context, state.partialTick);
            }

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

            if (this.overrides != null) {
                this.overrides.animate(parentModel, context, state.partialTick);
            }

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

    private void animate(ModelPart modelPart, String partName, AvatarAnimManager emote) {
        PlayerAnimBone bone = emote.get3DTransform(new PlayerAnimBone(partName));
        RenderUtil.copyVanillaPart(modelPart, bone);
        emote.updatePart(modelPart, bone);

        for (String name : modelPart.children.keySet()) {
            ModelPart child = modelPart.getChild(name);
            animate(child, name, emote);
        }
    }

    public List<String> getPartNames(Entity entity) {
        List<String> list = new ArrayList<>();

        if (this.model == null) {
            buildModels();
        }

        if (this.model != null) {
            this.addPartNames(this.model.get(entity).root(), list);
        }

        return list;
    }

    private void addPartNames(ModelPart m, List<String> list) {
        for (String name : m.children.keySet()) {
            list.add(name);
            addPartNames(m.getChild(name), list);
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
            var partName = arm == HumanoidArm.RIGHT ? "right_arm" : "left_arm";

            if (model.root().hasChild(partName)) {
                var foundPart = model.root().getChild(partName);
                ModelUtil.copyProperties(armPart, foundPart);
                armPart = foundPart;
            } else {
                return;
            }

            if (this.overrides != null) {
                this.overrides.animate(model, context, Minecraft.getInstance().getDeltaTracker().getGameTimeDeltaTicks());
            }
        } else {
            armPart.visible = true;
            armPart.skipDraw = false;

            if (this.overrides != null) {
                this.overrides.animate(playerRenderer.getModel(), context, Minecraft.getInstance().getDeltaTracker().getGameTimeDeltaTicks());
            }
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

    private static void mimicModelParts(String partName, ModelPart source, ModelPart target, LivingEntityRenderState state, boolean copy) {
        if (copy) {
            ModelUtil.copyProperties(source, target);
        }

        if (state instanceof AvatarRenderState avatar && partName.equalsIgnoreCase("root.body.cape")) {
            target.rotateBy((new Quaternionf()).rotateX((6.0F + avatar.capeLean / 2.0F + avatar.capeFlap) * ((float) Math.PI / 180F)).rotateZ(avatar.capeLean2 / 2.0F * ((float) Math.PI / 180F)).rotateY((180.0F - avatar.capeLean2 / 2.0F) * ((float) Math.PI / 180F)));
        }

        for (Map.Entry<String, ModelPart> e : target.children.entrySet()) {
            String childName = partName + "." + e.getKey();
            mimicModelParts(childName, source.getChild(e.getKey()), e.getValue(), state, source.hasChild(e.getKey()));
        }
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
                    .addOptional("model_layer", TYPE_IDENTIFIER, "The model layer to render.", "If not present, the model of the parent entity will be used.")
                    .add("texture", TYPE_ANY_TEXTURE, "The texture to render the model with.")
                    .addOptional("render_type", SettingType.enumList(RenderTypeRegistry.types().stream().map(Identifier::toString).toList()), "The render type to render the model with.", RenderTypeRegistry.getKey(RenderTypeRegistry.ENTITY_TRANSLUCENT))
                    .addOptional("light_emission", TYPE_INT, "The light emission of the model. Must be within 0 - 15", 0)
//                    .addOptional("overrides", TYPE_IDENTIFIER, "ID of an animations file")
                    .addExampleObject(new DefaultPackRenderLayer(
                            new SkinTypedValue<>(ModelLayerLocationCodec.parse("example:wide_model"), ModelLayerLocationCodec.parse("example:slim_model")),
                            new SkinTypedValue<>(new PackRenderLayerTexture(Identifier.fromNamespaceAndPath("example", "wide_texture")), new PackRenderLayerTexture(Identifier.fromNamespaceAndPath("example", "slim_texture"))),
                            RenderTypeRegistry.ENTITY_TRANSLUCENT,
                            5,
                            null,
                            PackRenderLayerProperties.DEFAULT,
                            PerspectiveAwareConditions.EMPTY
                    ));
        }
    }

}
