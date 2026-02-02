package net.threetag.palladium.client.gui.ui.component;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.input.MouseButtonInfo;
import net.minecraft.network.chat.Component;
import net.threetag.palladium.client.gui.ui.screen.UiScreen;
import net.threetag.palladium.logic.context.DataContext;

public class RenderableUiComponentWidget extends AbstractWidget {

    private final RenderableUiComponent component;

    public RenderableUiComponentWidget(RenderableUiComponent component, UiScreen parent) {
        super(component.getX(parent), component.getY(parent), component.getWidth(), component.getHeight(), Component.empty());
        this.component = component;
    }

    @Override
    protected void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.component.render(Minecraft.getInstance(), guiGraphics, DataContext.forEntity(Minecraft.getInstance().player), this.getX(), this.getY(), this.component.getPosition().alignment());
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {

    }

    @Override
    protected boolean isValidClickButton(MouseButtonInfo buttonInfo) {
        return false;
    }
}
