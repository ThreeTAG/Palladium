package net.threetag.palladium.client.renderer.entity.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.client.util.PerspectiveAwareConditions;
import net.threetag.palladium.logic.context.DataContext;

public abstract class PackRenderLayer<T extends PackRenderLayer.State> {

    protected final PerspectiveAwareConditions conditions;

    protected PackRenderLayer(PerspectiveAwareConditions conditions) {
        this.conditions = conditions;
    }

    public abstract void render(DataContext context, PoseStack poseStack, MultiBufferSource bufferSource, EntityModel<LivingEntityRenderState> parentModel, LivingEntityRenderState entityState, T layerState, int packedLight, float partialTick, float xRot, float yRot);

    public abstract void renderArm(DataContext context, PoseStack poseStack, MultiBufferSource bufferSource, HumanoidArm arm, ModelPart armPart, PlayerRenderer playerRenderer, T layerState, int packedLight);

    public boolean shouldRender(State state, PerspectiveAwareConditions.Perspective perspective) {
        return this.conditions.test(state.context, perspective);
    }

    public State createState(DataContext context) {
        return new State(this, context);
    }

    public boolean isOrContains(PackRenderLayer<?> layer) {
        return this == layer;
    }

    public abstract PackRenderLayerSerializer<?> getSerializer();

    protected static <T extends PackRenderLayer.State, B extends PackRenderLayer<T>> RecordCodecBuilder<B, PerspectiveAwareConditions> conditionsCodec() {
        return PerspectiveAwareConditions.CODEC.optionalFieldOf("conditions", PerspectiveAwareConditions.EMPTY).forGetter(l -> l.conditions);
    }

    public static class State {

        public final PackRenderLayer<?> renderLayer;
        private DataContext context;
        public int ticks = 0;
        protected boolean markedForRemoval = false;

        public State(PackRenderLayer<?> renderLayer, DataContext context) {
            this.renderLayer = renderLayer;
            this.context = context;
        }

        public void tick(LivingEntity entity) {
            this.ticks++;
        }

        public void updateContext(DataContext context) {
            this.context = context;
        }

        public DataContext getContext() {
            return this.context;
        }

        public void markForRemoval() {
            this.markedForRemoval = true;
        }

        public boolean isMarkedForRemoval() {
            return this.markedForRemoval;
        }

    }

    public static class Codecs {

        public static final Codec<PackRenderLayer<?>> DIRECT_CODEC = PackRenderLayerSerializer.TYPE_CODEC.dispatch(PackRenderLayer::getSerializer, PackRenderLayerSerializer::codec);

        public static Codec<PackRenderLayer<?>> SIMPLE_CODEC = Codec.withAlternative(DIRECT_CODEC, DefaultPackRenderLayer.CODEC.codec());

    }

}
