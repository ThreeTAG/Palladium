package net.threetag.palladium.power.ability;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;
import net.threetag.palladium.util.property.*;
import net.threetag.palladiumcore.registry.client.OverlayRegistry;

import java.util.List;

public class GuiOverlayAbility extends Ability {

    public static final PalladiumProperty<ResourceLocation> TEXTURE = new ResourceLocationProperty("texture").sync(SyncType.SELF).configurable("Texture path for the gui overlay");
    public static final PalladiumProperty<Integer> TEXTURE_WIDTH = new IntegerProperty("texture_width").sync(SyncType.SELF).configurable("Width of the texture file");
    public static final PalladiumProperty<Integer> TEXTURE_HEIGHT = new IntegerProperty("texture_height").sync(SyncType.SELF).configurable("Width of the texture file");
    public static final PalladiumProperty<Vec3> TRANSLATE = new Vec3Property("translate").sync(SyncType.SELF).configurable("Translation of the rendered object");
    public static final PalladiumProperty<Vec3> ROTATE = new Vec3Property("rotate").sync(SyncType.SELF).configurable("Rotation of the rendered object");
    public static final PalladiumProperty<Vec3> SCALE = new Vec3Property("scale").sync(SyncType.SELF).configurable("Scale of the rendered object");
    public static final PalladiumProperty<TextureAlignmentProperty.TextureAlignment> ALIGNMENT = new TextureAlignmentProperty("alignment").sync(SyncType.SELF).configurable("Determines how the image is aligned on the screen");

    public GuiOverlayAbility() {
        this.withProperty(TEXTURE, new ResourceLocation("textures/gui/presets/isles.png"));
        this.withProperty(TEXTURE_WIDTH, 256);
        this.withProperty(TEXTURE_HEIGHT, 256);
        this.withProperty(TRANSLATE, Vec3.ZERO);
        this.withProperty(ROTATE, Vec3.ZERO);
        this.withProperty(SCALE, new Vec3(1, 1, 1));
        this.withProperty(ALIGNMENT, TextureAlignmentProperty.TextureAlignment.STRETCH);
    }

    @Override
    public boolean isEffect() {
        return true;
    }

    @SuppressWarnings("SuspiciousNameCombination")
    @Environment(EnvType.CLIENT)
    public static class Renderer implements OverlayRegistry.IIngameOverlay {

        @Override
        public void render(Minecraft minecraft, Gui gui, PoseStack poseStack, float partialTicks, int width, int height) {
            List<AbilityEntry> entries = AbilityUtil.getEnabledEntries(minecraft.player, Abilities.GUI_OVERLAY.get()).stream().sorted((a1, a2) -> (int) (a1.getProperty(TRANSLATE).z - a2.getProperty(TRANSLATE).z)).toList();
            for (AbilityEntry entry : entries) {
                RenderSystem.setShader(GameRenderer::getPositionTexShader);
                RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
                RenderSystem.setShaderTexture(0, entry.getProperty(TEXTURE));

                var textureWidth = entry.getProperty(TEXTURE_WIDTH);
                var textureHeight = entry.getProperty(TEXTURE_HEIGHT);
                var alignment = entry.getProperty(ALIGNMENT);
                var translate = entry.getProperty(TRANSLATE);
                var rotate = entry.getProperty(ROTATE);
                var scale = entry.getProperty(SCALE);

                poseStack.pushPose();

                if (alignment.isStretched()) {
                    scale = scale.multiply(width / (float) textureWidth, height / (float) textureHeight, 1);
                }

                poseStack.translate(translate.x + (textureWidth * scale.x) / 2F, translate.y + (textureWidth * scale.y) / 2F, translate.z);

                if (!alignment.isStretched()) {
                    var horizontal = alignment.getHorizontal();
                    var vertical = alignment.getVertical();

                    if (horizontal > 0) {
                        poseStack.translate(horizontal == 1 ? (width - textureWidth) / 2F : width - textureWidth, 0, 0);
                    }

                    if (vertical > 0) {
                        poseStack.translate(0, vertical == 1 ? (height - textureHeight) / 2F : height - textureHeight, 0);
                    }
                }

                if (rotate.x != 0D) {
                    poseStack.mulPose(Vector3f.ZP.rotationDegrees((float) rotate.x));
                }

                if (rotate.y != 0D) {
                    poseStack.mulPose(Vector3f.YP.rotationDegrees((float) rotate.y));
                }

                if (rotate.z != 0D) {
                    poseStack.mulPose(Vector3f.ZP.rotationDegrees((float) rotate.z));
                }


                renderImage(poseStack, gui, scale, textureWidth, textureHeight);
                poseStack.popPose();
            }
        }

        private void renderImage(PoseStack poseStack, Gui gui, Vec3 scale, int textureWidth, int textureHeight) {
            poseStack.scale((float) scale.x, (float) scale.y, (float) scale.z);
            Gui.blit(poseStack, -textureWidth / 2, -textureHeight / 2, gui.getBlitOffset(), 0, 0, textureWidth, textureHeight, textureWidth, textureHeight);
        }
    }

    @Override
    public String getDocumentationDescription() {
        return "Displays a gui overlay on the screen.";
    }
}
