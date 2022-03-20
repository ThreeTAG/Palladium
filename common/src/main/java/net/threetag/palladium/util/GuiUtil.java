package net.threetag.palladium.util;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class GuiUtil {

    public static void drawItem(PoseStack poseStack, ItemStack stack, int hash, boolean renderOverlay, @Nullable String text) {
        if (stack.isEmpty()) {
            return;
        }

        var mc = Minecraft.getInstance();
        var itemRenderer = mc.getItemRenderer();
        var bakedModel = itemRenderer.getModel(stack, null, mc.player, hash);

        Minecraft.getInstance().getTextureManager().getTexture(TextureAtlas.LOCATION_BLOCKS).setFilter(false, false);
        RenderSystem.setShaderTexture(0, TextureAtlas.LOCATION_BLOCKS);
        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        RenderSystem.setShaderColor(1F, 1F, 1F, 1F);
        PoseStack modelViewStack = RenderSystem.getModelViewStack();
        modelViewStack.pushPose();
        modelViewStack.mulPoseMatrix(poseStack.last().pose());
        // modelViewStack.translate(x, y, 100.0D + this.blitOffset);
        modelViewStack.scale(1F, -1F, 1F);
        modelViewStack.scale(16F, 16F, 16F);
        RenderSystem.applyModelViewMatrix();
        MultiBufferSource.BufferSource bufferSource = Minecraft.getInstance().renderBuffers().bufferSource();
        var flatLight = !bakedModel.usesBlockLight();

        if (flatLight) {
            Lighting.setupForFlatItems();
        }

        itemRenderer.render(stack, ItemTransforms.TransformType.GUI, false, new PoseStack(), bufferSource, 0xF000F0, OverlayTexture.NO_OVERLAY, bakedModel);
        bufferSource.endBatch();
        RenderSystem.enableDepthTest();

        if (flatLight) {
            Lighting.setupFor3DItems();
        }

        modelViewStack.popPose();
        RenderSystem.applyModelViewMatrix();

        if (renderOverlay) {
            var t = Tesselator.getInstance();
            var font = mc.font;

            if (stack.getCount() != 1 || text != null) {
                var s = text == null ? String.valueOf(stack.getCount()) : text;
                poseStack.pushPose();
                poseStack.translate(9D - font.width(s), 1D, 20D);
                font.drawInBatch(s, 0F, 0F, 0xFFFFFF, true, poseStack.last().pose(), bufferSource, false, 0, 0xF000F0);
                bufferSource.endBatch();
                poseStack.popPose();
            }

            if (stack.isBarVisible()) {
                RenderSystem.disableDepthTest();
                RenderSystem.disableTexture();
                RenderSystem.disableBlend();
                var barWidth = stack.getBarWidth();
                var barColor = stack.getBarColor();
                draw(poseStack, t, -6, 5, 13, 2, 0, 0, 0, 255);
                draw(poseStack, t, -6, 5, barWidth, 1, barColor >> 16 & 255, barColor >> 8 & 255, barColor & 255, 255);
                RenderSystem.enableBlend();
                RenderSystem.enableTexture();
                RenderSystem.enableDepthTest();
            }

            var cooldown = mc.player == null ? 0F : mc.player.getCooldowns().getCooldownPercent(stack.getItem(), mc.getFrameTime());

            if (cooldown > 0F) {
                RenderSystem.disableDepthTest();
                RenderSystem.disableTexture();
                RenderSystem.enableBlend();
                RenderSystem.defaultBlendFunc();
                draw(poseStack, t, -8, Mth.floor(16F * (1F - cooldown)) - 8, 16, Mth.ceil(16F * cooldown), 255, 255, 255, 127);
                RenderSystem.enableTexture();
                RenderSystem.enableDepthTest();
            }
        }
    }

    private static void draw(PoseStack matrixStack, Tesselator t, int x, int y, int width, int height, int red, int green, int blue, int alpha) {
        if (width <= 0 || height <= 0) {
            return;
        }

        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        var m = matrixStack.last().pose();
        var renderer = t.getBuilder();
        renderer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
        renderer.vertex(m, x, y, 0).color(red, green, blue, alpha).endVertex();
        renderer.vertex(m, x, y + height, 0).color(red, green, blue, alpha).endVertex();
        renderer.vertex(m, x + width, y + height, 0).color(red, green, blue, alpha).endVertex();
        renderer.vertex(m, x + width, y, 0).color(red, green, blue, alpha).endVertex();
        t.end();
    }
}
