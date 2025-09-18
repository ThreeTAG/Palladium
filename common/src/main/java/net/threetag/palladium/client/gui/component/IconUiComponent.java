package net.threetag.palladium.client.gui.component;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.threetag.palladium.client.icon.Icon;
import net.threetag.palladium.logic.context.DataContext;

public record IconUiComponent(Icon icon) implements UiComponent {

    @Override
    public int getWidth() {
        return 16;
    }

    @Override
    public int getHeight() {
        return 16;
    }

    @Override
    public void render(Minecraft minecraft, GuiGraphics gui, DeltaTracker deltaTracker, int x, int y, UiAlignment alignment) {
        this.icon.draw(minecraft, gui, DataContext.forEntity(minecraft.player), x, y);
    }
}
