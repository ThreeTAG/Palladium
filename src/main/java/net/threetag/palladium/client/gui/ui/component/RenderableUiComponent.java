package net.threetag.palladium.client.gui.ui.component;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.threetag.palladium.client.gui.ui.UiAlignment;
import net.threetag.palladium.client.gui.ui.screen.UiScreen;
import net.threetag.palladium.logic.context.DataContext;

public abstract class RenderableUiComponent extends UiComponent {

    public RenderableUiComponent(UiComponentProperties properties) {
        super(properties);
    }

    public abstract void render(Minecraft minecraft, GuiGraphics gui, DataContext context, int x, int y, int width, int height, int mouseX, int mouseY, UiAlignment alignment);

    @Override
    public AbstractWidget buildWidget(UiScreen screen, ScreenRectangle rectangle) {
        return new RenderableUiComponentWidget(this, rectangle);
    }
}
