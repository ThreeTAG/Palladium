package net.threetag.palladium.client.gui.ui.component;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.input.MouseButtonInfo;
import net.minecraft.network.chat.Component;
import net.threetag.palladium.client.gui.ui.screen.UiScreen;
import net.threetag.palladium.logic.context.DataContext;

public class RenderableUiComponentWidget extends AbstractWidget {

    private final RenderableUiComponent component;

    public RenderableUiComponentWidget(RenderableUiComponent component, UiScreen parent) {
        super(component.getX(parent.getInnerRectangle()), component.getY(parent.getInnerRectangle()), component.getWidth(), component.getHeight(), Component.empty());
        this.component = component;
        component.getProperties().tooltip().ifPresent(tooltip -> this.setTooltip(Tooltip.create(tooltip)));
    }

    @Override
    protected void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.component.render(Minecraft.getInstance(), guiGraphics, DataContext.forEntity(Minecraft.getInstance().player),
                this.getX(), this.getY(), this.getWidth(), this.getHeight(), mouseX, mouseY, this.component.getProperties().alignment());
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {

    }

    @Override
    protected boolean isValidClickButton(MouseButtonInfo buttonInfo) {
        return false;
    }
}
