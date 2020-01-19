package net.threetag.threecore.util.entityeffect;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.threetag.threecore.util.math.TCMathHelper;

import javax.annotation.Nullable;

public class EffectEntityRenderer extends EntityRenderer<EffectEntity> {

    protected EffectEntityRenderer(EntityRendererManager renderManager) {
        super(renderManager);
    }

    @Override
    public void doRender(EffectEntity entity, double x, double y, double z, float entityYaw, float partialTicks) {
        Entity anchor = entity.getAnchorEntity();

        if (anchor != null) {
            double d0 = TCMathHelper.interpolate(anchor.lastTickPosX, anchor.posX, partialTicks);
            double d1 = TCMathHelper.interpolate(anchor.lastTickPosY, anchor.posY, partialTicks);
            double d2 = TCMathHelper.interpolate(anchor.lastTickPosZ, anchor.posZ, partialTicks);
            x += d0 - TCMathHelper.interpolate(entity.lastTickPosX, entity.posX, partialTicks);
            y += d1 - TCMathHelper.interpolate(entity.lastTickPosY, entity.posY, partialTicks);
            z += d2 - TCMathHelper.interpolate(entity.lastTickPosZ, entity.posZ, partialTicks);

            GlStateManager.pushMatrix();
            GlStateManager.translated(x, y, z);
            entity.entityEffect.render(entity, anchor, Minecraft.getInstance().player == anchor && Minecraft.getInstance().gameSettings.thirdPersonView == 0, partialTicks);
            GlStateManager.popMatrix();
        }
    }

    @Nullable
    @Override
    protected ResourceLocation getEntityTexture(EffectEntity entity) {
        return null;
    }
}
