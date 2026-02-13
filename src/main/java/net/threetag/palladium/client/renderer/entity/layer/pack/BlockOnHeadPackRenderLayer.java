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
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.threetag.palladium.client.util.PerspectiveAwareConditions;
import net.threetag.palladium.documentation.CodecDocumentationBuilder;
import net.threetag.palladium.logic.context.DataContext;

public class BlockOnHeadPackRenderLayer extends PackRenderLayer<PackRenderLayer.State> {

    public static final MapCodec<BlockOnHeadPackRenderLayer> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            BlockState.CODEC.fieldOf("block").forGetter(l -> l.blockState),
            propertiesCodec(), conditionsCodec()
    ).apply(instance, BlockOnHeadPackRenderLayer::new));

    private final BlockState blockState;

    public BlockOnHeadPackRenderLayer(BlockState blockState, PackRenderLayerProperties properties, PerspectiveAwareConditions conditions) {
        super(properties, conditions);
        this.blockState = blockState;
    }

    @Override
    public void submit(DataContext context, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, EntityModel<LivingEntityRenderState> parentModel, LivingEntityRenderState entityState, State layerState, int packedLight, float partialTick, float xRot, float yRot) {
        if (parentModel.root().hasChild("head")) {
            var head = parentModel.root().getChild("head");

            poseStack.pushPose();
            head.translateAndRotate(poseStack);
            poseStack.mulPose(Axis.XP.rotationDegrees(180F));
            poseStack.translate(-0.5F, 0.5F, -0.5F);
            submitNodeCollector.submitBlock(poseStack, this.blockState, packedLight, OverlayTexture.NO_OVERLAY, entityState.outlineColor);
            poseStack.popPose();
        }
    }

    @Override
    public void submitArm(DataContext context, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, HumanoidArm arm, ModelPart armPart, AvatarRenderer<?> playerRenderer, State layerState, int packedLight) {

    }

    @Override
    public PackRenderLayerSerializer<?> getSerializer() {
        return PackRenderLayerSerializers.BLOCK_ON_HEAD;
    }

    public static class Serializer extends PackRenderLayerSerializer<BlockOnHeadPackRenderLayer> {

        @Override
        public MapCodec<BlockOnHeadPackRenderLayer> codec() {
            return CODEC;
        }

        @Override
        public void addDocumentation(CodecDocumentationBuilder<PackRenderLayer<? extends State>, BlockOnHeadPackRenderLayer> builder, HolderLookup.Provider provider) {
            builder.setName("Block on Head")
                    .setDescription("Renders a block on the top of the entity's head.")
                    .add("block", TYPE_BLOCK_TAG, "ID of the block/block state definition")
                    .addExampleObject(new BlockOnHeadPackRenderLayer(
                            Blocks.GLASS.defaultBlockState(),
                            PackRenderLayerProperties.DEFAULT,
                            PerspectiveAwareConditions.EMPTY
                    ));
        }
    }
}
