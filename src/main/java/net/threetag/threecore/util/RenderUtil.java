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
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.threetag.threecore.ThreeCore;
import net.threetag.threecore.ability.Ability;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nullable;

@OnlyIn(Dist.CLIENT)
public class RenderUtil {

    public static float renderTickTime;
    private static LivingEntity currentEntityInItemRendering = null;
    private static Ability currentAbilityInIconRendering = null;

    public static void onRenderGlobal(TickEvent.RenderTickEvent e) {
        renderTickTime = e.renderTickTime;
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

    public static void renderFilledBox(Matrix4f matrix, IVertexBuilder builder, AxisAlignedBB boundingBox, float red, float green, float blue, float alpha, int combinedLightIn) {
        renderFilledBox(matrix, builder, (float) boundingBox.minX, (float) boundingBox.minY, (float) boundingBox.minZ, (float) boundingBox.maxX, (float) boundingBox.maxY, (float) boundingBox.maxZ, red, green, blue, alpha, combinedLightIn);
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

    public static void drawGlowingLine(Matrix4f matrix, IVertexBuilder builder, float length, float width, float red, float green, float blue, float alpha, int combinedLightIn) {
        AxisAlignedBB box = new AxisAlignedBB(-width / 2F, 0, -width / 2F, width / 2F, length, width / 2F);
        renderFilledBox(matrix, builder, box, 1F, 1F, 1F, alpha, combinedLightIn);

        for(int i = 0; i < 3; i++) {
            renderFilledBox(matrix, builder, box.grow(i * 0.5F * 0.0625F), red, green, blue, (1F / i / 2) * alpha, combinedLightIn);
        }
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

    public static void drawLine(Matrix4f matrix, IVertexBuilder builder, float width, float length, float red, float green, float blue, float alpha, int combinedLightIn) {
        drawLine(matrix, builder, width, length, true, true, red, green, blue, alpha, combinedLightIn);
    }

    public static void drawLine(Matrix4f matrix, IVertexBuilder builder, float width, float length, boolean drawTop, boolean drawBottom, float red, float green, float blue, float alpha, int combinedLightIn) {
        renderFilledBox(matrix, builder, -width / 2F, 0, -width / 2F, width / 2F, length, width / 2F, red, green, blue, alpha, combinedLightIn);
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

        public RenderTypes(String name, VertexFormat vertexFormat, int drawMode, int bufferSize, boolean useDelegate, boolean needsSorting, Runnable setupTask, Runnable clearTask) {
            super(name, vertexFormat, drawMode, bufferSize, useDelegate, needsSorting, setupTask, clearTask);
        }

        public static final RenderType HYDRAULIC_PRESS_PISTONS = makeType(ThreeCore.MODID + ":hydraulic_press_pistons", DefaultVertexFormats.POSITION_COLOR_LIGHTMAP, GL11.GL_QUADS, 256, State.getBuilder()
                .layer(field_239235_M_)
                .transparency(TRANSLUCENT_TRANSPARENCY)
                .texture(NO_TEXTURE)
                .depthTest(DEPTH_LEQUAL)
                .shadeModel(RenderState.SHADE_ENABLED)
                .cull(CULL_ENABLED)
                .lightmap(RenderState.LIGHTMAP_ENABLED)
                .writeMask(COLOR_DEPTH_WRITE)
                .build(false));

        public static final RenderType LASER = makeType(ThreeCore.MODID + ":laser", DefaultVertexFormats.POSITION_COLOR_LIGHTMAP, GL11.GL_QUADS, 256, false, false, State.getBuilder()
                .texture(RenderState.NO_TEXTURE)
                .cull(RenderState.CULL_ENABLED)
                .alpha(DEFAULT_ALPHA)
                .transparency(RenderState.LIGHTNING_TRANSPARENCY)
                .build(true));

        public static RenderType getGlowing(ResourceLocation locationIn) {
            TextureState textureState = new TextureState(locationIn, false, false);
            return makeType(ThreeCore.MODID + ":glowing", DefaultVertexFormats.ENTITY, 7, 256, false, true, State.getBuilder().transparency(TRANSLUCENT_TRANSPARENCY).alpha(DEFAULT_ALPHA).cull(CULL_DISABLED).overlay(OVERLAY_ENABLED).texture(textureState).fog(BLACK_FOG).build(false));
        }
    }

}
