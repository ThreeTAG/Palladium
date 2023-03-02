package net.threetag.palladium.power.ability;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.threetag.palladium.util.property.*;
import net.threetag.palladiumcore.registry.client.OverlayRegistry;

public class GuiOverlayAbility extends Ability {

    public static final PalladiumProperty<ResourceLocation> TEXTURE = new ResourceLocationProperty("texture").sync(SyncType.SELF).configurable("Texture path for the gui overlay");
    public static final PalladiumProperty<Integer> TEXTURE_WIDTH = new IntegerProperty("texture_width").sync(SyncType.SELF).configurable("Width of the texture file");
    public static final PalladiumProperty<Integer> TEXTURE_HEIGHT = new IntegerProperty("texture_height").sync(SyncType.SELF).configurable("Width of the texture file");
    public static final PalladiumProperty<TextureAlignmentProperty.TextureAlignment> ALIGNMENT = new TextureAlignmentProperty("alignment").sync(SyncType.SELF).configurable("Determines how the image is aligned on the screen");

    public GuiOverlayAbility() {
        this.withProperty(TEXTURE, new ResourceLocation("textures/gui/presets/isles.png"));
        this.withProperty(TEXTURE_WIDTH, 256);
        this.withProperty(TEXTURE_HEIGHT, 256);
        this.withProperty(ALIGNMENT, TextureAlignmentProperty.TextureAlignment.STRETCH);
    }

    @Override
    public boolean isEffect() {
        return true;
    }

    @Environment(EnvType.CLIENT)
    public static class Renderer implements OverlayRegistry.IIngameOverlay {

        @SuppressWarnings("SuspiciousNameCombination")
        @Override
        public void render(Minecraft minecraft, Gui gui, PoseStack mStack, float partialTicks, int width, int height) {
            for (AbilityEntry entry : AbilityUtil.getEnabledEntries(minecraft.player, Abilities.GUI_OVERLAY.get())) {
                RenderSystem.setShader(GameRenderer::getPositionTexShader);
                RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
                RenderSystem.setShaderTexture(0, entry.getProperty(TEXTURE));

                var textureWidth = entry.getProperty(TEXTURE_WIDTH);
                var textureHeight = entry.getProperty(TEXTURE_HEIGHT);
                var alignment = entry.getProperty(ALIGNMENT);

                mStack.pushPose();

                if (alignment.isStretched()) {
                    mStack.scale(width / (float) textureWidth, height / (float) textureHeight, 1);
                } else {
                    var horizontal = alignment.getHorizontal();
                    var vertical = alignment.getVertical();

                    if (horizontal > 0) {
                        mStack.translate(horizontal == 1 ? (width - textureWidth) / 2F : width - textureWidth,0, 0);
                    }

                    if (vertical > 0) {
                        mStack.translate(0, vertical == 1 ? (height - textureHeight) / 2F : height - textureHeight, 0);
                    }
                }

                Gui.blit(mStack, 0, 0, gui.getBlitOffset(), 0, 0, textureWidth, textureHeight, textureWidth, textureHeight);
                mStack.popPose();
            }
        }
    }

    @Override
    public String getDocumentationDescription() {
        return "Displays a gui overlay on the screen";
    }
}
