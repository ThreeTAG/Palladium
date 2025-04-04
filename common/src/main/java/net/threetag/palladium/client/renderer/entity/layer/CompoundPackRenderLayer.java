package net.threetag.palladium.client.renderer.entity.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.entity.HumanoidArm;
import net.threetag.palladium.condition.PerspectiveAwareConditions;
import net.threetag.palladium.data.DataContext;
import net.threetag.palladium.documentation.CodecDocumentationBuilder;

import java.util.List;

public class CompoundPackRenderLayer extends PackRenderLayer<PackRenderLayer.State> {

    public static final MapCodec<CompoundPackRenderLayer> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codecs.SIMPLE_CODEC.listOf().fieldOf("layers").forGetter(l -> l.layers),
            conditionsCodec()
    ).apply(instance, CompoundPackRenderLayer::new));

    private final List<PackRenderLayer<?>> layers;

    protected CompoundPackRenderLayer(List<PackRenderLayer<?>> layers, PerspectiveAwareConditions conditions) {
        super(conditions);
        this.layers = layers;
    }

    @Override
    public void render(DataContext context, PoseStack poseStack, MultiBufferSource bufferSource, EntityModel<LivingEntityRenderState> parentModel, LivingEntityRenderState state, State layerState, int packedLight, float partialTick, float xRot, float yRot) {
        // nothing
    }

    @Override
    public void renderArm(DataContext context, PoseStack poseStack, MultiBufferSource bufferSource, HumanoidArm arm, ModelPart armPart, PlayerRenderer playerRenderer, State layerState, int packedLight) {
        // nothing
    }

    @Override
    public PackRenderLayerSerializer<?> getSerializer() {
        return PackRenderLayerSerializers.COMPOUND;
    }

    @Override
    public boolean isOrContains(PackRenderLayer<?> layer) {
        if (super.isOrContains(layer)) {
            return true;
        }

        for (PackRenderLayer<?> child : this.layers) {
            if (child.isOrContains(layer)) {
                return true;
            }
        }

        return false;
    }

    public List<PackRenderLayer<?>> getLayers() {
        return this.layers;
    }

    public static class Serializer extends PackRenderLayerSerializer<CompoundPackRenderLayer> {

        @Override
        public MapCodec<CompoundPackRenderLayer> codec() {
            return CODEC;
        }

        @Override
        public void addDocumentation(CodecDocumentationBuilder<PackRenderLayer<? extends State>, CompoundPackRenderLayer> builder, HolderLookup.Provider provider) {
            builder.setName("Compound Render Layer")
                    .setDescription("A compound render layer that can contain multiple render layers.")
                    .add("layers", TYPE_RENDER_LAYERS, "The list of render layers this compound render layer contains.");
        }

    }
}
