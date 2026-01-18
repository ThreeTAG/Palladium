package net.threetag.palladium.client.renderer.entity.layer.pack;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.player.AvatarRenderer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.core.HolderLookup;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderLivingEvent;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.client.renderer.entity.state.PalladiumRenderStateKeys;
import net.threetag.palladium.client.util.PerspectiveAwareConditions;
import net.threetag.palladium.documentation.CodecDocumentationBuilder;
import net.threetag.palladium.logic.context.DataContext;
import net.threetag.palladium.util.PalladiumCodecs;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@EventBusSubscriber(modid = Palladium.MOD_ID, value = Dist.CLIENT)
public class VibrationPackRenderLayer extends PackRenderLayer<VibrationPackRenderLayer.State> {

    public static final MapCodec<VibrationPackRenderLayer> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            ExtraCodecs.POSITIVE_INT.fieldOf("intensity").forGetter(l -> l.intensity),
            PalladiumCodecs.TIME.optionalFieldOf("fade_ticks", 20).forGetter(l -> l.fadeTicks),
            propertiesCodec(), conditionsCodec()
    ).apply(instance, VibrationPackRenderLayer::new));

    public final int intensity;
    public final int fadeTicks;

    public VibrationPackRenderLayer(int intensity, int fadeTicks, PackRenderLayerProperties properties, PerspectiveAwareConditions conditions) {
        super(properties, conditions);
        this.intensity = intensity;
        this.fadeTicks = fadeTicks;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public static LivingEntityRenderState createRenderStateForVibrations(LivingEntity entity, float partialTick) {
        PalladiumRenderStateKeys.ignoreVibrationState();
        var entityState = Minecraft.getInstance().getEntityRenderDispatcher().getRenderer(entity).createRenderState(entity, partialTick);

        if (entityState instanceof LivingEntityRenderState livingEntityRenderState) {
            Map currentLayers = entityState.getRenderDataOrDefault(PalladiumRenderStateKeys.RENDER_LAYERS, Collections.emptyMap());
            Map<PackRenderLayer<PackRenderLayer.State>, PackRenderLayer.State> layers = new HashMap<>();

            currentLayers.forEach((l1, s1) -> {
                if (!(l1 instanceof VibrationPackRenderLayer)) {
                    layers.put((PackRenderLayer<PackRenderLayer.State>) l1, (PackRenderLayer.State) s1);
                }
            });

            entityState.setRenderData(PalladiumRenderStateKeys.OPACITY, 0.15F);
            entityState.setRenderData(PalladiumRenderStateKeys.RENDER_LAYERS, layers);
            entityState.setRenderData(PalladiumRenderStateKeys.TRAILS, Collections.emptyMap());
            entityState.shadowPieces.clear();
            return livingEntityRenderState;
        }

        return null;
    }

    @SuppressWarnings("rawtypes")
    @SubscribeEvent
    static void postRender(RenderLivingEvent.Post<LivingEntity, LivingEntityRenderState, EntityModel<LivingEntityRenderState>> e) {
        e.getRenderState().getRenderDataOrDefault(PalladiumRenderStateKeys.RENDER_LAYERS, Collections.emptyMap()).forEach((layer, state) -> {
            PackRenderLayer l = layer;
            if (l instanceof VibrationPackRenderLayer vibration && state instanceof VibrationPackRenderLayer.State vibrationState && layer.shouldRender(state, PerspectiveAwareConditions.Perspective.THIRD_PERSON)) {
                var mc = Minecraft.getInstance();
                var entityState = e.getRenderState().getRenderData(PalladiumRenderStateKeys.VIBRATION_RENDER_STATE);

                if (entityState != null) {
                    var poseStack = e.getPoseStack();
                    var rand = RandomSource.create();
                    var progress = vibrationState.getProgress(e.getPartialTick());

                    for (int i = 0; i < vibration.intensity; i++) {
                        poseStack.pushPose();
                        poseStack.translate(((rand.nextFloat() - 0.5F) / 150) * progress, 0, ((rand.nextFloat() - 0.5F) / 150) * progress);

                        mc.getEntityRenderDispatcher().submit(
                                entityState,
                                mc.gameRenderer.getLevelRenderState().cameraRenderState,
                                0, 0, 0,
                                poseStack, e.getSubmitNodeCollector()
                        );
                        poseStack.popPose();
                    }
                }
            }
        });
    }

    @Override
    public void submit(DataContext context, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, EntityModel<LivingEntityRenderState> parentModel, LivingEntityRenderState entityState, State layerState, int packedLight, float partialTick, float xRot, float yRot) {
        // nothing
    }

    @Override
    public void submitArm(DataContext context, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, HumanoidArm arm, ModelPart armPart, AvatarRenderer<?> playerRenderer, State layerState, int packedLight) {

    }

    @Override
    public PackRenderLayer.State createState(DataContext context) {
        return new State(this, context);
    }

    @Override
    public PackRenderLayerSerializer<?> getSerializer() {
        return PackRenderLayerSerializers.VIBRATION;
    }

    public static class State extends PackRenderLayer.State {

        private final VibrationPackRenderLayer renderLayer;
        private int progress;
        private int prevProgress;

        public State(VibrationPackRenderLayer renderLayer, DataContext context) {
            super(renderLayer, context);
            this.renderLayer = renderLayer;
        }

        @Override
        public void tick(LivingEntity entity) {
            super.tick(entity);
            this.prevProgress = this.progress;

            if (this.markedForRemoval && this.progress > 0) {
                this.progress--;
            } else if (!this.markedForRemoval && this.progress < this.renderLayer.fadeTicks) {
                this.progress++;
            }
        }

        @Override
        public boolean isMarkedForRemoval() {
            return super.isMarkedForRemoval() && this.progress == 0 && this.prevProgress == 0;
        }

        public float getProgress(float partialTick) {
            return Mth.lerp(partialTick, this.prevProgress, this.progress);
        }
    }

    public static class Serializer extends PackRenderLayerSerializer<VibrationPackRenderLayer> {

        @Override
        public MapCodec<VibrationPackRenderLayer> codec() {
            return CODEC;
        }

        @Override
        public void addDocumentation(CodecDocumentationBuilder<PackRenderLayer<? extends PackRenderLayer.State>, VibrationPackRenderLayer> builder, HolderLookup.Provider provider) {
            builder.setName("Vibration")
                    .setDescription("Renders a vibration effect on the entity.")
                    .add("intentiy", TYPE_INT, "The intensity of the vibration.")
                    .addExampleObject(new VibrationPackRenderLayer(
                            10, 20,
                            PackRenderLayerProperties.DEFAULT,
                            PerspectiveAwareConditions.EMPTY
                    ));
        }
    }

}
