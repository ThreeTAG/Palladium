package net.threetag.palladium.client.gui.ui.component;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.threetag.palladium.client.gui.component.UiAlignment;
import net.threetag.palladium.client.gui.ui.screen.UiScreen;
import net.threetag.palladium.logic.context.DataContext;

public interface RenderableUiComponent extends UiComponent {

    void render(Minecraft minecraft, GuiGraphics gui, DataContext context, int x, int y, UiAlignment alignment);

    @Override
    default AbstractWidget buildWidget(UiScreen screen) {
        return new RenderableUiComponentWidget(this, screen);
    }
}
