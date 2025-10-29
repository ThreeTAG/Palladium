package net.threetag.palladium.client.renderer.entity.layer.pack;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.player.AvatarRenderer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.core.HolderLookup;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import net.threetag.palladium.client.renderer.LaserRenderer;
import net.threetag.palladium.client.util.PerspectiveAwareConditions;
import net.threetag.palladium.documentation.CodecDocumentationBuilder;
import net.threetag.palladium.logic.context.DataContext;
import org.joml.Vector2f;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class LightningSparksPackRenderLayer extends PackRenderLayer<LightningSparksPackRenderLayer.State> {

    public static final MapCodec<LightningSparksPackRenderLayer> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            ExtraCodecs.NON_NEGATIVE_FLOAT.optionalFieldOf("frequency", 0.5F).forGetter(l -> l.frequency),
            ExtraCodecs.POSITIVE_INT.optionalFieldOf("amount", 5).forGetter(l -> l.amount),
            LaserRenderer.codec(1).fieldOf("render_settings").forGetter(l -> l.laserRenderer),
            conditionsCodec()
    ).apply(instance, LightningSparksPackRenderLayer::new));

    private final float frequency;
    private final int amount;
    private final LaserRenderer laserRenderer;

    public LightningSparksPackRenderLayer(float frequency, int amount, LaserRenderer laserRenderer, PerspectiveAwareConditions conditions) {
        super(conditions);
        this.frequency = frequency;
        this.amount = amount;
        this.laserRenderer = laserRenderer;
    }

    @Override
    public State createState(DataContext context) {
        return new State(this, context);
    }

    @Override
    public void submit(DataContext context, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, EntityModel<LivingEntityRenderState> parentModel, LivingEntityRenderState state, State layerState, int packedLight, float partialTick, float xRot, float yRot) {
        var entity = context.getLivingEntity();

        if (entity != null) {
            for (Spark spark : layerState.sparks) {
                float opacity = 1F - Mth.clamp((spark.ticks + partialTick) / 4, 0, 1);

                poseStack.pushPose();
                poseStack.translate(0, 24 / 16F, 0);
                poseStack.mulPose(Axis.XP.rotationDegrees(180));
                poseStack.translate(entity.getBbWidth() / -2, 0, entity.getBbWidth() / -2);
                poseStack.translate(spark.pos1.x, spark.pos1.y, spark.pos1.z);
                this.laserRenderer.faceAndRender(poseStack, submitNodeCollector, spark.pos1, spark.pos2, spark.ticks, partialTick, 1F, opacity);
                this.laserRenderer.faceAndRender(poseStack, submitNodeCollector, spark.pos1, spark.pos3, spark.ticks, partialTick, 1F, opacity);
                poseStack.popPose();
            }
        }
    }

    @Override
    public void submitArm(DataContext context, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, HumanoidArm arm, ModelPart armPart, AvatarRenderer<?> playerRenderer, State layerState, int packedLight) {

    }

    @Override
    public PackRenderLayerSerializer<?> getSerializer() {
        return PackRenderLayerSerializers.LIGHTNING_SPARKS;
    }

    public static class State extends PackRenderLayer.State {

        private final LightningSparksPackRenderLayer layer;
        private List<Spark> sparks = new ArrayList<>();

        public State(LightningSparksPackRenderLayer renderLayer, DataContext context) {
            super(renderLayer, context);
            this.layer = renderLayer;
        }

        @Override
        public void tick(LivingEntity entity) {
            super.tick(entity);
            this.sparks = this.sparks.stream().peek(Spark::tick).filter(s -> s.ticks < 5).collect(Collectors.toList());

            if (this.sparks.isEmpty() && !this.markedForRemoval && Math.random() < this.layer.frequency) {
                for (int i = 0; i < this.layer.amount; i++) {
                    this.sparks.add(new Spark(entity, RandomSource.create()));
                }
            }
        }

        @Override
        public boolean isMarkedForRemoval() {
            return super.isMarkedForRemoval() && this.sparks.isEmpty();
        }
    }

    public static class Spark {

        public final Vec3 pos1;
        public final Vec3 pos2;
        public final Vec3 pos3;
        private int ticks;

        public Spark(Entity entity, RandomSource randomSource) {
            var random = new Random();
            this.pos1 = new Vec3(entity.getBbWidth() * random.nextFloat(), entity.getBbHeight() * random.nextFloat(), entity.getBbWidth() * random.nextFloat());
            this.pos2 = this.makePos(this.pos1, entity.getBbWidth(), entity.getBbHeight(), randomSource);
            this.pos3 = this.makePos(this.pos1, entity.getBbWidth(), entity.getBbHeight(), randomSource);
        }

        private Vec3 makePos(Vec3 center, float width, float height, RandomSource source) {
            float length = (width + height) / 20F;
            float x = (float) Mth.clamp(center.x + ((source.nextFloat() * 2 - 1) * length), 0, width);
            float y = (float) Mth.clamp(center.y + ((source.nextFloat() * 2 - 1) * length), 0, height);
            float z = (float) Mth.clamp(center.z + ((source.nextFloat() * 2 - 1) * length), 0, width);
            return new Vec3(x, y, z);
        }

        private void tick() {
            this.ticks++;
        }
    }

    public static class Serializer extends PackRenderLayerSerializer<LightningSparksPackRenderLayer> {

        @Override
        public MapCodec<LightningSparksPackRenderLayer> codec() {
            return CODEC;
        }

        @Override
        public void addDocumentation(CodecDocumentationBuilder<PackRenderLayer<? extends PackRenderLayer.State>, LightningSparksPackRenderLayer> builder, HolderLookup.Provider provider) {
            builder.setName("Lightning Sparks")
                    .setDescription("Renders lightning sparks around the entity.")
                    .addOptional("frequency", TYPE_FLOAT, "The frequency of the sparks.", 0.5F)
                    .addOptional("amount", TYPE_INT, "The amount of sparks to spawn during each occurence.", 5)
                    .add("render_settings", TYPE_LASER_RENDERER, "The render settings for the sparks.")
                    .setExampleObject(new LightningSparksPackRenderLayer(
                            0.2F,
                            10,
                            new LaserRenderer(
                                    new LaserRenderer.LaserPart(Color.BLUE, 1F, 0F, null),
                                    new LaserRenderer.LaserPart(Color.WHITE, 1F, 0F, null),
                                    2, new Vector2f(2 / 16F, 2 / 16F), 0, 0
                            ),
                            PerspectiveAwareConditions.EMPTY
                    ));
        }

    }
}
