package net.threetag.palladium.client.gui.screen.abilitybar;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.threetag.palladium.client.gui.component.UiAlignment;

public interface AbilityBarComponent {

    int getWidth();

    int getHeight();

    void render(Minecraft minecraft, GuiGraphics gui, int x, int y, UiAlignment alignment);
}
