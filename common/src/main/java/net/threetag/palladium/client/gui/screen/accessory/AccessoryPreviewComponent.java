package net.threetag.palladium.client.gui.screen.accessory;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.threetag.palladium.accessory.AccessorySlot;
import net.threetag.palladium.client.PoseStackTransformation;
import net.threetag.palladium.client.gui.component.grid.AbstractSelectionGrid;
import net.threetag.palladium.util.Easing;

import java.util.Objects;

public class AccessoryPreviewComponent extends AbstractWidget {

    private static final int TRANSITION_TIME = 10;

    private final Minecraft minecraft;
    private PoseStackTransformation targetTransform = AccessorySlot.DEFAULT_PREVIEW;
    private PoseStackTransformation prevTransform = AccessorySlot.DEFAULT_PREVIEW;
    private PoseStackTransformation renderedTransform = AccessorySlot.DEFAULT_PREVIEW;
    private int transitionTicks, prevTransitionTicks;

    public AccessoryPreviewComponent(int x, int y, int width, int height) {
        super(x, y, width, height, Component.empty());
        this.minecraft = Minecraft.getInstance();
    }

    @Override
    protected void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(guiGraphics);
        guiGraphics.pose().pushPose();
        guiGraphics.enableScissor(this.getX(), this.getY(), this.getRight(), this.getBottom());
        this.renderedTransform = PoseStackTransformation.lerp(Easing.INOUTSINE.apply(1F - (Mth.lerp(partialTick, this.prevTransitionTicks, this.transitionTicks) / TRANSITION_TIME)),
                this.prevTransform, this.targetTransform);
        guiGraphics.pose().translate(0, 0, 500.0);
        AccessoryScreen.renderEntityInInventory(guiGraphics, this.getX() + this.width / 2F, this.getY() + this.height / 2F, 80, this.renderedTransform, Objects.requireNonNull(this.minecraft.player));
        guiGraphics.disableScissor();
        guiGraphics.pose().popPose();
        this.renderSeparators(guiGraphics);
    }

    protected void renderSeparators(GuiGraphics guiGraphics) {
        ResourceLocation resourceLocation = this.minecraft.level == null ? Screen.HEADER_SEPARATOR : Screen.INWORLD_HEADER_SEPARATOR;
        ResourceLocation resourceLocation2 = this.minecraft.level == null ? Screen.FOOTER_SEPARATOR : Screen.INWORLD_FOOTER_SEPARATOR;
        guiGraphics.blit(RenderType::guiTextured, resourceLocation, this.getX(), this.getY() - 2, 0.0F, 0.0F, this.getWidth(), 2, 32, 2);
        guiGraphics.blit(RenderType::guiTextured, resourceLocation2, this.getX(), this.getBottom(), 0.0F, 0.0F, this.getWidth(), 2, 32, 2);
    }

    protected void renderBackground(GuiGraphics guiGraphics) {
        ResourceLocation resourceLocation = this.minecraft.level == null ? AbstractSelectionGrid.MENU_LIST_BACKGROUND : AbstractSelectionGrid.INWORLD_MENU_LIST_BACKGROUND;
        guiGraphics.blit(RenderType::guiTextured, resourceLocation, this.getX(), this.getY(), (float) this.getRight(), (float) this.getBottom(), this.getWidth(), this.getHeight(), 32, 32);
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

    public void setTargetTransformation(PoseStackTransformation transformation) {
        this.prevTransform = this.renderedTransform;
        this.targetTransform = transformation != null ? transformation : AccessorySlot.DEFAULT_PREVIEW;
        this.transitionTicks = TRANSITION_TIME;
        this.prevTransitionTicks = TRANSITION_TIME;
    }

}
