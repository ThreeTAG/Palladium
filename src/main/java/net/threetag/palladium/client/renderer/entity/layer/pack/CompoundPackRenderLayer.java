package net.threetag.palladium.client.renderer.entity.layer.pack;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.player.AvatarRenderer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.entity.HumanoidArm;
import net.threetag.palladium.client.util.PerspectiveAwareConditions;
import net.threetag.palladium.documentation.CodecDocumentationBuilder;
import net.threetag.palladium.logic.context.DataContext;

import java.util.List;

public class CompoundPackRenderLayer extends PackRenderLayer<PackRenderLayer.State> {

    public static final MapCodec<CompoundPackRenderLayer> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codecs.SIMPLE_CODEC.listOf().fieldOf("layers").forGetter(l -> l.layers),
            propertiesCodec(), conditionsCodec()
    ).apply(instance, CompoundPackRenderLayer::new));

    private final List<PackRenderLayer<?>> layers;

    public CompoundPackRenderLayer(List<PackRenderLayer<?>> layers, PackRenderLayerProperties properties, PerspectiveAwareConditions conditions) {
        super(properties, conditions);
        this.layers = layers;
    }

    @Override
    public void submit(DataContext context, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, EntityModel<LivingEntityRenderState> parentModel, LivingEntityRenderState state, State layerState, int packedLight, float partialTick, float xRot, float yRot) {
        // nothing
    }

    @Override
    public void submitArm(DataContext context, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, HumanoidArm arm, ModelPart armPart, AvatarRenderer<?> playerRenderer, State layerState, int packedLight) {
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
