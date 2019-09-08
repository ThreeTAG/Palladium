package net.threetag.threecore.util.client;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.event.TickEvent;

public class RenderUtil {

    public static float renderTickTime;

    public static void onRenderGlobal(TickEvent.RenderTickEvent e) {
        renderTickTime = e.renderTickTime;
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

}
