package net.threetag.palladium.client.gui.screen.abilitybar;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.threetag.palladium.client.gui.component.UiAlignment;
import net.threetag.palladium.client.renderer.icon.IconRenderer;
import net.threetag.palladium.icon.Icon;
import net.threetag.palladium.logic.context.DataContext;

public record IconAbilityBarComponent(Icon icon) implements AbilityBarComponent {

    @Override
    public int getWidth() {
        return 16;
    }

    @Override
    public int getHeight() {
        return 16;
    }

    @Override
    public void render(Minecraft minecraft, GuiGraphics gui, int x, int y, UiAlignment alignment) {
        IconRenderer.drawIcon(this.icon, minecraft, gui, DataContext.forEntity(minecraft.player), x, y);
    }
}
