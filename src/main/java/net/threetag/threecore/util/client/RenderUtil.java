package net.threetag.threecore.util.client;

import com.mojang.blaze3d.platform.GLX;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL14;

import java.awt.*;

@OnlyIn(Dist.CLIENT)
public class RenderUtil {

    public static float renderTickTime;
    private static float lastBrightnessX = GLX.lastBrightnessX;
    private static float lastBrightnessY = GLX.lastBrightnessY;

    public static void onRenderGlobal(TickEvent.RenderTickEvent e) {
        renderTickTime = e.renderTickTime;
    }

    public static void setLightmapTextureCoords(float x, float y) {
        lastBrightnessX = GLX.lastBrightnessX;
        lastBrightnessY = GLX.lastBrightnessY;
        GLX.glMultiTexCoord2f(GLX.GL_TEXTURE1, x, y);
    }

    public static void restoreLightmapTextureCoords() {
        GLX.glMultiTexCoord2f(GLX.GL_TEXTURE1, lastBrightnessX, lastBrightnessY);
    }

    public static void drawSelectionBoundingBox(AxisAlignedBB box, float red, float green, float blue, float alpha) {
        drawBoundingBox(box.minX, box.minY, box.minZ, box.maxX, box.maxY, box.maxZ, red, green, blue, alpha);
    }

    public static void drawBoundingBox(double minX, double minY, double minZ, double maxX, double maxY, double maxZ, float red, float green, float blue, float alpha) {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(3, DefaultVertexFormats.POSITION_COLOR);
        drawBoundingBox(bufferbuilder, minX, minY, minZ, maxX, maxY, maxZ, red, green, blue, alpha);
        tessellator.draw();
    }

    public static void drawBoundingBox(BufferBuilder buffer, double minX, double minY, double minZ, double maxX, double maxY, double maxZ, float red, float green, float blue, float alpha) {
        buffer.pos(minX, minY, minZ).color(red, green, blue, 0.0F).endVertex();
        buffer.pos(minX, minY, minZ).color(red, green, blue, alpha).endVertex();
        buffer.pos(maxX, minY, minZ).color(red, green, blue, alpha).endVertex();
        buffer.pos(maxX, minY, maxZ).color(red, green, blue, alpha).endVertex();
        buffer.pos(minX, minY, maxZ).color(red, green, blue, alpha).endVertex();
        buffer.pos(minX, minY, minZ).color(red, green, blue, alpha).endVertex();
        buffer.pos(minX, maxY, minZ).color(red, green, blue, alpha).endVertex();
        buffer.pos(maxX, maxY, minZ).color(red, green, blue, alpha).endVertex();
        buffer.pos(maxX, maxY, maxZ).color(red, green, blue, alpha).endVertex();
        buffer.pos(minX, maxY, maxZ).color(red, green, blue, alpha).endVertex();
        buffer.pos(minX, maxY, minZ).color(red, green, blue, alpha).endVertex();
        buffer.pos(minX, maxY, maxZ).color(red, green, blue, 0.0F).endVertex();
        buffer.pos(minX, minY, maxZ).color(red, green, blue, alpha).endVertex();
        buffer.pos(maxX, maxY, maxZ).color(red, green, blue, 0.0F).endVertex();
        buffer.pos(maxX, minY, maxZ).color(red, green, blue, alpha).endVertex();
        buffer.pos(maxX, maxY, minZ).color(red, green, blue, 0.0F).endVertex();
        buffer.pos(maxX, minY, minZ).color(red, green, blue, alpha).endVertex();
        buffer.pos(maxX, minY, minZ).color(red, green, blue, 0.0F).endVertex();
    }

    public static void renderFilledBox(AxisAlignedBB aabb, float red, float green, float blue, float alpha) {
        renderFilledBox(aabb.minX, aabb.minY, aabb.minZ, aabb.maxX, aabb.maxY, aabb.maxZ, red, green, blue, alpha);
    }

    public static void renderFilledBox(double minX, double minY, double minZ, double maxX, double maxY, double maxZ, float red, float green, float blue, float alpha) {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(5, DefaultVertexFormats.POSITION_COLOR);
        addChainedFilledBoxVertices(bufferbuilder, minX, minY, minZ, maxX, maxY, maxZ, red, green, blue, alpha);
        tessellator.draw();
    }

    public static void addChainedFilledBoxVertices(BufferBuilder builder, double x1, double y1, double z1, double x2, double y2, double z2, float red, float green, float blue, float alpha) {
        builder.pos(x1, y1, z1).color(red, green, blue, alpha).endVertex();
        builder.pos(x1, y1, z1).color(red, green, blue, alpha).endVertex();
        builder.pos(x1, y1, z1).color(red, green, blue, alpha).endVertex();
        builder.pos(x1, y1, z2).color(red, green, blue, alpha).endVertex();
        builder.pos(x1, y2, z1).color(red, green, blue, alpha).endVertex();
        builder.pos(x1, y2, z2).color(red, green, blue, alpha).endVertex();
        builder.pos(x1, y2, z2).color(red, green, blue, alpha).endVertex();
        builder.pos(x1, y1, z2).color(red, green, blue, alpha).endVertex();
        builder.pos(x2, y2, z2).color(red, green, blue, alpha).endVertex();
        builder.pos(x2, y1, z2).color(red, green, blue, alpha).endVertex();
        builder.pos(x2, y1, z2).color(red, green, blue, alpha).endVertex();
        builder.pos(x2, y1, z1).color(red, green, blue, alpha).endVertex();
        builder.pos(x2, y2, z2).color(red, green, blue, alpha).endVertex();
        builder.pos(x2, y2, z1).color(red, green, blue, alpha).endVertex();
        builder.pos(x2, y2, z1).color(red, green, blue, alpha).endVertex();
        builder.pos(x2, y1, z1).color(red, green, blue, alpha).endVertex();
        builder.pos(x1, y2, z1).color(red, green, blue, alpha).endVertex();
        builder.pos(x1, y1, z1).color(red, green, blue, alpha).endVertex();
        builder.pos(x1, y1, z1).color(red, green, blue, alpha).endVertex();
        builder.pos(x2, y1, z1).color(red, green, blue, alpha).endVertex();
        builder.pos(x1, y1, z2).color(red, green, blue, alpha).endVertex();
        builder.pos(x2, y1, z2).color(red, green, blue, alpha).endVertex();
        builder.pos(x2, y1, z2).color(red, green, blue, alpha).endVertex();
        builder.pos(x1, y2, z1).color(red, green, blue, alpha).endVertex();
        builder.pos(x1, y2, z1).color(red, green, blue, alpha).endVertex();
        builder.pos(x1, y2, z2).color(red, green, blue, alpha).endVertex();
        builder.pos(x2, y2, z1).color(red, green, blue, alpha).endVertex();
        builder.pos(x2, y2, z2).color(red, green, blue, alpha).endVertex();
        builder.pos(x2, y2, z2).color(red, green, blue, alpha).endVertex();
        builder.pos(x2, y2, z2).color(red, green, blue, alpha).endVertex();
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
        TextureAtlasSprite icon = Minecraft.getInstance().getTextureMap().getSprite(stillTexture);

        int renderAmount = (int) Math.max(Math.min(height, amount * height / tankCapacity), 1);
        int posY = (int) (y + height - renderAmount);

        Minecraft.getInstance().getTextureManager().bindTexture(AtlasTexture.LOCATION_BLOCKS_TEXTURE);
        setColorRGBA(stack.getFluid().getAttributes().getColor(stack));

        GlStateManager.enableBlend();
        for (int i = 0; i < width; i += 16) {
            for (int j = 0; j < renderAmount; j += 16) {
                int drawWidth = (int) Math.min(width - i, 16);
                int drawHeight = Math.min(renderAmount - j, 16);

                int drawX = (int) (x + i);
                int drawY = posY + j;

                double minU = icon.getMinU();
                double maxU = icon.getMaxU();
                double minV = icon.getMinV();
                double maxV = icon.getMaxV();
                double u = minU + (maxU - minU) * drawWidth / 16F;
                double v = minV + (maxV - minV) * drawHeight / 16F;

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
        GlStateManager.disableBlend();
        GlStateManager.color3f(1, 1, 1);
    }

    public static void drawLine(float width, float length) {
        drawLine(width, length, true, true);
    }

    public static void drawLine(float width, float length, boolean drawTop, boolean drawBottom) {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bb = tessellator.getBuffer();

        bb.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);

        // West Side
        bb.pos(width, 0, -width).endVertex();
        bb.pos(width, length, -width).endVertex();
        bb.pos(width, length, width).endVertex();
        bb.pos(width, 0, width).endVertex();

        // East Side
        bb.pos(-width, 0, -width).endVertex();
        bb.pos(-width, length, -width).endVertex();
        bb.pos(-width, length, width).endVertex();
        bb.pos(-width, 0, width).endVertex();

        // South
        bb.pos(-width, 0, width).endVertex();
        bb.pos(-width, length, width).endVertex();
        bb.pos(width, length, width).endVertex();
        bb.pos(width, 0, width).endVertex();

        // North
        bb.pos(-width, 0, -width).endVertex();
        bb.pos(-width, length, -width).endVertex();
        bb.pos(width, length, -width).endVertex();
        bb.pos(width, 0, -width).endVertex();

        if (drawTop) {
            bb.pos(-width, length, -width).endVertex();
            bb.pos(width, length, -width).endVertex();
            bb.pos(width, length, width).endVertex();
            bb.pos(-width, length, width).endVertex();
        }

        if (drawBottom) {
            bb.pos(-width, 0, -width).endVertex();
            bb.pos(width, 0, -width).endVertex();
            bb.pos(width, 0, width).endVertex();
            bb.pos(-width, 0, width).endVertex();
        }

        tessellator.draw();
    }

    public static void drawGlowingLine(float width, float length, Color color, boolean extendedEnd, boolean drawTop, boolean drawBottom) {
        GL11.glPushAttrib(GL11.GL_LIGHTING_BIT);
        GlStateManager.disableTexture();
        GlStateManager.disableCull();
        RenderHelper.disableStandardItemLighting();
        RenderUtil.setLightmapTextureCoords(240, 240);
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE);
        GL14.glBlendEquation(GL14.GL_FUNC_ADD);
        float alpha = color.getAlpha() / 255F;
        GlStateManager.color4f(1F, 1F, 1F, alpha);
        RenderUtil.drawLine(width, length);

        for (int i = 1; i < 3; ++i) {
            GlStateManager.color4f(color.getRed() / 255F, color.getGreen() / 255F, color.getBlue() / 255F, (1F / i / 2) * alpha);

            float growWidth = width + i * 0.5F * 0.0625F;
            float growHeight = extendedEnd ? length + i * 0.5F * 0.0625F : length;

            RenderUtil.drawLine(growWidth, growHeight, drawTop, drawBottom);
        }

        GL14.glBlendEquation(GL14.GL_FUNC_ADD);
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        RenderUtil.restoreLightmapTextureCoords();
        RenderHelper.enableStandardItemLighting();
        GL11.glPopAttrib();
        GlStateManager.enableCull();
        GlStateManager.disableBlend();
        GlStateManager.enableTexture();
    }

    public static void setColorRGBA(int color) {
        float a = (float) alpha(color) / 255.0F;
        float r = (float) red(color) / 255.0F;
        float g = (float) green(color) / 255.0F;
        float b = (float) blue(color) / 255.0F;

        GlStateManager.color4f(r, g, b, a);
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


}
