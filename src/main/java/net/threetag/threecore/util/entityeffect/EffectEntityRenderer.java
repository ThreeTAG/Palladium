package net.threetag.threecore.util.entityeffect;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.threetag.threecore.util.MathUtil;

public class EffectEntityRenderer extends EntityRenderer<EffectEntity> {

    public EffectEntityRenderer(EntityRendererManager renderManager) {
        super(renderManager);
    }

    @Override
    public void render(EffectEntity entity, float entityYaw, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer renderTypeBuffer, int packedLightIn) {
        Entity anchor = entity.getAnchorEntity();

        if (anchor != null) {
            double x = MathUtil.interpolate(anchor.lastTickPosX, anchor.getPosX(), partialTicks) - MathUtil.interpolate(entity.lastTickPosX, entity.getPosX(), partialTicks);
            double y = MathUtil.interpolate(anchor.lastTickPosY, anchor.getPosY(), partialTicks) - MathUtil.interpolate(entity.lastTickPosY, entity.getPosY(), partialTicks);
            double z = MathUtil.interpolate(anchor.lastTickPosZ, anchor.getPosZ(), partialTicks) - MathUtil.interpolate(entity.lastTickPosZ, entity.getPosZ(), partialTicks);

            matrixStack.push();
            matrixStack.translate(x, y, z);
            entity.entityEffect.render(entity, anchor, matrixStack, renderTypeBuffer, packedLightIn, Minecraft.getInstance().player == anchor && Minecraft.getInstance().gameSettings.getPointOfView().func_243192_a(), partialTicks);
            matrixStack.pop();
        }
    }

    @Override
    public ResourceLocation getEntityTexture(EffectEntity entity) {
        return null;
    }
}
