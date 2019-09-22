package net.threetag.threecore.sizechanging;

import com.mojang.blaze3d.platform.GLX;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.entity.LivingEntity;

public class Test {

    public static void drawEntityOnScreen(int p_147046_0_, int p_147046_1_, int p_147046_2_, float p_147046_3_, float p_147046_4_, LivingEntity p_147046_5_) {
        GlStateManager.enableColorMaterial();
        GlStateManager.pushMatrix();
        GlStateManager.translatef((float)p_147046_0_, (float)p_147046_1_, 50.0F);
        GlStateManager.scalef((float)(-p_147046_2_), (float)p_147046_2_, (float)p_147046_2_);
        SizeManager.renderInInvCallback(p_147046_5_);
        GlStateManager.rotatef(180.0F, 0.0F, 0.0F, 1.0F);
        float lvt_6_1_ = p_147046_5_.renderYawOffset;
        float lvt_7_1_ = p_147046_5_.rotationYaw;
        float lvt_8_1_ = p_147046_5_.rotationPitch;
        float lvt_9_1_ = p_147046_5_.prevRotationYawHead;
        float lvt_10_1_ = p_147046_5_.rotationYawHead;
        GlStateManager.rotatef(135.0F, 0.0F, 1.0F, 0.0F);
        RenderHelper.enableStandardItemLighting();
        GlStateManager.rotatef(-135.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotatef(-((float)Math.atan((double)(p_147046_4_ / 40.0F))) * 20.0F, 1.0F, 0.0F, 0.0F);
        p_147046_5_.renderYawOffset = (float)Math.atan((double)(p_147046_3_ / 40.0F)) * 20.0F;
        p_147046_5_.rotationYaw = (float)Math.atan((double)(p_147046_3_ / 40.0F)) * 40.0F;
        p_147046_5_.rotationPitch = -((float)Math.atan((double)(p_147046_4_ / 40.0F))) * 20.0F;
        p_147046_5_.rotationYawHead = p_147046_5_.rotationYaw;
        p_147046_5_.prevRotationYawHead = p_147046_5_.rotationYaw;
        GlStateManager.translatef(0.0F, 0.0F, 0.0F);
        EntityRendererManager lvt_11_1_ = Minecraft.getInstance().getRenderManager();
        lvt_11_1_.setPlayerViewY(180.0F);
        lvt_11_1_.setRenderShadow(false);
        lvt_11_1_.renderEntity(p_147046_5_, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F, false);
        lvt_11_1_.setRenderShadow(true);
        p_147046_5_.renderYawOffset = lvt_6_1_;
        p_147046_5_.rotationYaw = lvt_7_1_;
        p_147046_5_.rotationPitch = lvt_8_1_;
        p_147046_5_.prevRotationYawHead = lvt_9_1_;
        p_147046_5_.rotationYawHead = lvt_10_1_;
        GlStateManager.popMatrix();
        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableRescaleNormal();
        GlStateManager.activeTexture(GLX.GL_TEXTURE1);
        GlStateManager.disableTexture();
        GlStateManager.activeTexture(GLX.GL_TEXTURE0);
    }

}
