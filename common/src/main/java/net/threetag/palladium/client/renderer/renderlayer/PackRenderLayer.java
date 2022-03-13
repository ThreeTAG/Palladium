package net.threetag.palladium.client.renderer.renderlayer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.power.ability.AbilityEntry;

import java.util.function.BiFunction;

public class PackRenderLayer implements IPackRenderLayer {

    private final HumanoidModel<LivingEntity> model;
    private final ResourceLocation texture;
    private final BiFunction<MultiBufferSource, ResourceLocation, VertexConsumer> renderType;

    public PackRenderLayer(ModelLayerLocation modelLayerLocation, ResourceLocation texture, BiFunction<MultiBufferSource, ResourceLocation, VertexConsumer> renderType) {
        this.model = new HumanoidModel<>(Minecraft.getInstance().getEntityModels().bakeLayer(modelLayerLocation));
        this.texture = texture;
        this.renderType = renderType;
    }

    @Override
    public void render(LivingEntity entity, AbilityEntry abilityEntry, PoseStack poseStack, MultiBufferSource bufferSource, HumanoidModel<LivingEntity> parentModel, int packedLight, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
//        this.model.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        parentModel.copyPropertiesTo(this.model);
        // TODO apply enchant glint when item is enchanted
        VertexConsumer vertexConsumer = this.renderType.apply(bufferSource, this.getTexture());
        this.model.renderToBuffer(poseStack, vertexConsumer, packedLight, OverlayTexture.NO_OVERLAY, 1F, 1F, 1F, 1F);
    }

    public ResourceLocation getTexture() {
        return this.texture;
    }

}
