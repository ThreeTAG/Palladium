package net.threetag.palladium.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.threetag.palladium.entity.EffectEntity;

public class EffectEntityRenderer extends EntityRenderer<EffectEntity> {

    public EffectEntityRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(EffectEntity entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        Entity anchor = entity.getAnchorEntity();

        if(anchor != null) {
            double x = Mth.lerp(partialTicks, anchor.xOld, anchor.getX()) - Mth.lerp(partialTicks, entity.xOld, entity.getX());
            double y = Mth.lerp(partialTicks, anchor.yOld, anchor.getY()) - Mth.lerp(partialTicks, entity.yOld, entity.getY());
            double z = Mth.lerp(partialTicks, anchor.zOld, anchor.getZ()) - Mth.lerp(partialTicks, entity.zOld, entity.getZ());

            poseStack.pushPose();
            poseStack.translate(x, y, z);
            entity.entityEffect.render(entity, anchor, poseStack, buffer, packedLight, Minecraft.getInstance().player == anchor && Minecraft.getInstance().options.getCameraType().isFirstPerson(), partialTicks);
            poseStack.popPose();
        }
    }

    @Override
    public ResourceLocation getTextureLocation(EffectEntity entity) {
        return null;
    }
}
