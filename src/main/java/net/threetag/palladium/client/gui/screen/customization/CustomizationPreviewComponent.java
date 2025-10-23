package net.threetag.palladium.client.gui.screen.customization;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;
import net.threetag.palladium.customization.CustomizationCategory;
import net.threetag.palladium.client.gui.component.grid.AbstractSelectionGrid;
import net.threetag.palladium.customization.CustomizationPreview;
import net.threetag.palladium.util.Easing;

import java.util.Objects;

public class CustomizationPreviewComponent extends AbstractWidget {

    private static final int TRANSITION_TIME = 10;

    private final Minecraft minecraft;
    private CustomizationPreview targetTransform = CustomizationCategory.DEFAULT_PREVIEW;
    private CustomizationPreview prevTransform = CustomizationCategory.DEFAULT_PREVIEW;
    private CustomizationPreview renderedTransform = CustomizationCategory.DEFAULT_PREVIEW;
    private int transitionTicks, prevTransitionTicks;

    public CustomizationPreviewComponent(int x, int y, int width, int height) {
        super(x, y, width, height, Component.empty());
        this.minecraft = Minecraft.getInstance();
    }

    @Override
    protected void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(guiGraphics);
        guiGraphics.pose().pushMatrix();
        guiGraphics.enableScissor(this.getX(), this.getY(), this.getRight(), this.getBottom());
        this.renderedTransform = CustomizationPreview.lerp(Easing.INOUTSINE.apply(1F - (Mth.lerp(partialTick, this.prevTransitionTicks, this.transitionTicks) / TRANSITION_TIME)),
                this.prevTransform, this.targetTransform);
        PlayerCustomizationScreen.renderEntity(guiGraphics, this.getX(), this.getY(), this.getRight(), this.getBottom(), 70, this.renderedTransform, Objects.requireNonNull(this.minecraft.player), null);
        guiGraphics.disableScissor();
        guiGraphics.pose().popMatrix();
        this.renderSeparators(guiGraphics);
    }

    protected void renderSeparators(GuiGraphics guiGraphics) {
        ResourceLocation resourceLocation = this.minecraft.level == null ? Screen.HEADER_SEPARATOR : Screen.INWORLD_HEADER_SEPARATOR;
        ResourceLocation resourceLocation2 = this.minecraft.level == null ? Screen.FOOTER_SEPARATOR : Screen.INWORLD_FOOTER_SEPARATOR;
        guiGraphics.blit(RenderPipelines.GUI_TEXTURED,resourceLocation, this.getX(), this.getY() - 2, 0, 0, this.getWidth(), 2, 32, 2);
        guiGraphics.blit(RenderPipelines.GUI_TEXTURED,resourceLocation2, this.getX(), this.getBottom(), 0, 0, this.getWidth(), 2, 32, 2);
    }

    protected void renderBackground(GuiGraphics guiGraphics) {
        ResourceLocation resourceLocation = this.minecraft.level == null ? AbstractSelectionGrid.MENU_LIST_BACKGROUND : AbstractSelectionGrid.INWORLD_MENU_LIST_BACKGROUND;
        guiGraphics.blit(RenderPipelines.GUI_TEXTURED,resourceLocation, this.getX(), this.getY(), this.getRight(), this.getBottom(), this.getWidth(), this.getHeight(), 32, 32);
    }

    public void tick() {
        this.prevTransitionTicks = this.transitionTicks;

        if (this.transitionTicks > 0) {
            this.transitionTicks--;
        }
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {

    }

    public void setTargetTransformation(CustomizationPreview transformation) {
        this.prevTransform = this.renderedTransform;
        this.targetTransform = transformation != null ? transformation.invertYRot() : CustomizationCategory.DEFAULT_PREVIEW.invertYRot();
        this.transitionTicks = TRANSITION_TIME;
        this.prevTransitionTicks = TRANSITION_TIME;
    }

}
