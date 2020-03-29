package net.threetag.threecore.util;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.threetag.threecore.ThreeCore;
import net.threetag.threecore.ability.Ability;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nullable;
import java.awt.*;

@OnlyIn(Dist.CLIENT)
public class RenderUtil {

    public static float renderTickTime;
    private static LivingEntity currentEntityInItemRendering = null;
    private static Ability currentAbilityInIconRendering = null;

    public static void onRenderGlobal(TickEvent.RenderTickEvent e) {
        renderTickTime = e.renderTickTime;
    }

    public static void setLightmapTextureCoords(float x, float y) {
    }

    public static void restoreLightmapTextureCoords() {
    }

    public static void setCurrentEntityInItemRendering(LivingEntity entity) {
        RenderUtil.currentEntityInItemRendering = entity;
    }

    @Nullable
    public static LivingEntity getCurrentEntityInItemRendering() {
        return currentEntityInItemRendering;
    }

    public static void setCurrentAbilityInIconRendering(Ability ability) {
        RenderUtil.currentAbilityInIconRendering = ability;
    }

    public static Ability getCurrentAbilityInIconRendering() {
        return currentAbilityInIconRendering;
    }

    public static void renderFilledBox(Matrix4f matrix, IVertexBuilder builder, float startX, float startY, float startZ, float endX, float endY, float endZ, float red, float green, float blue, float alpha, int combinedLightIn) {
        //down
        builder.pos(matrix, startX, startY, startZ).color(red, green, blue, alpha).lightmap(combinedLightIn).endVertex();
        builder.pos(matrix, endX, startY, startZ).color(red, green, blue, alpha).lightmap(combinedLightIn).endVertex();
        builder.pos(matrix, endX, startY, endZ).color(red, green, blue, alpha).lightmap(combinedLightIn).endVertex();
        builder.pos(matrix, startX, startY, endZ).color(red, green, blue, alpha).lightmap(combinedLightIn).endVertex();

        //up
        builder.pos(matrix, startX, endY, startZ).color(red, green, blue, alpha).lightmap(combinedLightIn).endVertex();
        builder.pos(matrix, startX, endY, endZ).color(red, green, blue, alpha).lightmap(combinedLightIn).endVertex();
        builder.pos(matrix, endX, endY, endZ).color(red, green, blue, alpha).lightmap(combinedLightIn).endVertex();
        builder.pos(matrix, endX, endY, startZ).color(red, green, blue, alpha).lightmap(combinedLightIn).endVertex();

        //east
        builder.pos(matrix, startX, startY, startZ).color(red, green, blue, alpha).lightmap(combinedLightIn).endVertex();
        builder.pos(matrix, startX, endY, startZ).color(red, green, blue, alpha).lightmap(combinedLightIn).endVertex();
        builder.pos(matrix, endX, endY, startZ).color(red, green, blue, alpha).lightmap(combinedLightIn).endVertex();
        builder.pos(matrix, endX, startY, startZ).color(red, green, blue, alpha).lightmap(combinedLightIn).endVertex();

        //west
        builder.pos(matrix, startX, startY, endZ).color(red, green, blue, alpha).lightmap(combinedLightIn).endVertex();
        builder.pos(matrix, endX, startY, endZ).color(red, green, blue, alpha).lightmap(combinedLightIn).endVertex();
        builder.pos(matrix, endX, endY, endZ).color(red, green, blue, alpha).lightmap(combinedLightIn).endVertex();
        builder.pos(matrix, startX, endY, endZ).color(red, green, blue, alpha).lightmap(combinedLightIn).endVertex();

        //south
        builder.pos(matrix, endX, startY, startZ).color(red, green, blue, alpha).lightmap(combinedLightIn).endVertex();
        builder.pos(matrix, endX, endY, startZ).color(red, green, blue, alpha).lightmap(combinedLightIn).endVertex();
        builder.pos(matrix, endX, endY, endZ).color(red, green, blue, alpha).lightmap(combinedLightIn).endVertex();
        builder.pos(matrix, endX, startY, endZ).color(red, green, blue, alpha).lightmap(combinedLightIn).endVertex();

        //north
        builder.pos(matrix, startX, startY, startZ).color(red, green, blue, alpha).lightmap(combinedLightIn).endVertex();
        builder.pos(matrix, startX, startY, endZ).color(red, green, blue, alpha).lightmap(combinedLightIn).endVertex();
        builder.pos(matrix, startX, endY, endZ).color(red, green, blue, alpha).lightmap(combinedLightIn).endVertex();
        builder.pos(matrix, startX, endY, startZ).color(red, green, blue, alpha).lightmap(combinedLightIn).endVertex();
    }

    public static void renderGuiTank(IFluidHandler fluidHandler, int tank, double x, double y, double zLevel, double width, double height) {
        FluidStack stack = fluidHandler.getFluidInTank(tank);
        int tankCapacity = fluidHandler.getTankCapacity(tank);
        renderGuiTank(stack, tankCapacity, x, y, zLevel, width, height);
    }

    public static void renderGuiTank(FluidStack stack, int tankCapacity, double x, double y, double zLevel, double width, double height) {
        int amount = stack.getAmount();
        if (stack.getFluid() == null || amount <= 0) {
            return;
        }

        ResourceLocation stillTexture = stack.getFluid().getAttributes().getStillTexture();
        TextureAtlasSprite icon = Minecraft.getInstance().getAtlasSpriteGetter(PlayerContainer.LOCATION_BLOCKS_TEXTURE).apply(stillTexture);

        int renderAmount = (int) Math.max(Math.min(height, amount * height / tankCapacity), 1);
        int posY = (int) (y + height - renderAmount);

        Minecraft.getInstance().getTextureManager().bindTexture(PlayerContainer.LOCATION_BLOCKS_TEXTURE);
        setColorRGBA(stack.getFluid().getAttributes().getColor(stack));

        RenderSystem.enableBlend();
        for (int i = 0; i < width; i += 16) {
            for (int j = 0; j < renderAmount; j += 16) {
                int drawWidth = (int) Math.min(width - i, 16);
                int drawHeight = Math.min(renderAmount - j, 16);

                int drawX = (int) (x + i);
                int drawY = posY + j;

                float minU = icon.getMinU();
                float maxU = icon.getMaxU();
                float minV = icon.getMinV();
                float maxV = icon.getMaxV();
                float u = minU + (maxU - minU) * drawWidth / 16F;
                float v = minV + (maxV - minV) * drawHeight / 16F;

                Tessellator tessellator = Tessellator.getInstance();
                BufferBuilder tes = tessellator.getBuffer();
                tes.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
                tes.pos(drawX, drawY + drawHeight, zLevel).tex(minU, v).endVertex();
                tes.pos(drawX + drawWidth, drawY + drawHeight, zLevel).tex(u, v).endVertex();
                tes.pos(drawX + drawWidth, drawY, zLevel).tex(u, minV).endVertex();
                tes.pos(drawX, drawY, zLevel).tex(minU, minV).endVertex();
                tessellator.draw();
            }
        }
        RenderSystem.disableBlend();
        RenderSystem.color4f(1, 1, 1, 1);
    }

    public static void drawLine(float width, float length) {
        drawLine(width, length, true, true);
    }

    public static void drawLine(float width, float length, boolean drawTop, boolean drawBottom) {
//        Tessellator tessellator = Tessellator.getInstance();
//        BufferBuilder bb = tessellator.getBuffer();
//
//        bb.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
//
//        // West Side
//        bb.pos(width, 0, -width).endVertex();
//        bb.pos(width, length, -width).endVertex();
//        bb.pos(width, length, width).endVertex();
//        bb.pos(width, 0, width).endVertex();
//
//        // East Side
//        bb.pos(-width, 0, -width).endVertex();
//        bb.pos(-width, length, -width).endVertex();
//        bb.pos(-width, length, width).endVertex();
//        bb.pos(-width, 0, width).endVertex();
//
//        // South
//        bb.pos(-width, 0, width).endVertex();
//        bb.pos(-width, length, width).endVertex();
//        bb.pos(width, length, width).endVertex();
//        bb.pos(width, 0, width).endVertex();
//
//        // North
//        bb.pos(-width, 0, -width).endVertex();
//        bb.pos(-width, length, -width).endVertex();
//        bb.pos(width, length, -width).endVertex();
//        bb.pos(width, 0, -width).endVertex();
//
//        if (drawTop) {
//            bb.pos(-width, length, -width).endVertex();
//            bb.pos(width, length, -width).endVertex();
//            bb.pos(width, length, width).endVertex();
//            bb.pos(-width, length, width).endVertex();
//        }
//
//        if (drawBottom) {
//            bb.pos(-width, 0, -width).endVertex();
//            bb.pos(width, 0, -width).endVertex();
//            bb.pos(width, 0, width).endVertex();
//            bb.pos(-width, 0, width).endVertex();
//        }
//
//        tessellator.draw();
    }

    public static void drawGlowingLine(float width, float length, Color color, boolean extendedEnd, boolean drawTop, boolean drawBottom) {
//        GL11.glPushAttrib(GL11.GL_LIGHTING_BIT);
//        GlStateManager.disableTexture();
//        GlStateManager.disableCull();
//        RenderHelper.disableStandardItemLighting();
//        RenderUtil.setLightmapTextureCoords(240, 240);
//        GlStateManager.enableBlend();
//        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE);
//        GL14.glBlendEquation(GL14.GL_FUNC_ADD);
//        float alpha = color.getAlpha() / 255F;
//        GlStateManager.color4f(1F, 1F, 1F, alpha);
//        RenderUtil.drawLine(width, length);
//
//        for (int i = 1; i < 3; ++i) {
//            GlStateManager.color4f(color.getRed() / 255F, color.getGreen() / 255F, color.getBlue() / 255F, (1F / i / 2) * alpha);
//
//            float growWidth = width + i * 0.5F * 0.0625F;
//            float growHeight = extendedEnd ? length + i * 0.5F * 0.0625F : length;
//
//            RenderUtil.drawLine(growWidth, growHeight, drawTop, drawBottom);
//        }
//
//        GL14.glBlendEquation(GL14.GL_FUNC_ADD);
//        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
//        RenderUtil.restoreLightmapTextureCoords();
//        RenderHelper.enableStandardItemLighting();
//        GL11.glPopAttrib();
//        GlStateManager.enableCull();
//        GlStateManager.disableBlend();
//        GlStateManager.enableTexture();
    }

    public static void setColorRGBA(int color) {
        float a = (float) alpha(color) / 255.0F;
        float r = (float) red(color) / 255.0F;
        float g = (float) green(color) / 255.0F;
        float b = (float) blue(color) / 255.0F;

        RenderSystem.color4f(r, g, b, a);
    }

    public static int alpha(int c) {
        return (c >> 24) & 0xFF;
    }

    public static int red(int c) {
        return (c >> 16) & 0xFF;
    }

    public static int green(int c) {
        return (c >> 8) & 0xFF;
    }

    public static int blue(int c) {
        return (c) & 0xFF;
    }

    public static class RenderTypes extends RenderType {

        public RenderTypes(String p_i225992_1_, VertexFormat p_i225992_2_, int p_i225992_3_, int p_i225992_4_, boolean p_i225992_5_, boolean p_i225992_6_, Runnable p_i225992_7_, Runnable p_i225992_8_) {
            super(p_i225992_1_, p_i225992_2_, p_i225992_3_, p_i225992_4_, p_i225992_5_, p_i225992_6_, p_i225992_7_, p_i225992_8_);
        }

        public static final RenderType HYDRAULIC_PRESS_PISTONS = makeType(ThreeCore.MODID + ":hydraulic_press_pistons", DefaultVertexFormats.POSITION_COLOR_LIGHTMAP, GL11.GL_QUADS, 256, RenderType.State.getBuilder()
                .layer(PROJECTION_LAYERING)
                .transparency(TRANSLUCENT_TRANSPARENCY)
                .texture(NO_TEXTURE)
                .depthTest(DEPTH_LEQUAL)
                .shadeModel(RenderState.SHADE_ENABLED)
                .cull(CULL_ENABLED)
                .lightmap(RenderState.LIGHTMAP_ENABLED)
                .writeMask(COLOR_DEPTH_WRITE)
                .build(false));
    }

}
