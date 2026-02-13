package net.threetag.palladium.client.gui.screen.abilitybar;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.threetag.palladium.client.util.GuiUtil;
import net.threetag.palladium.client.util.RenderUtil;

public class TextAbilityBarComponent implements AbilityBarComponent {

    private final Component text;
    public boolean outline;
    private int width;

    public TextAbilityBarComponent(Component text) {
        this.text = text;
        this.width = Minecraft.getInstance().font.width(this.text) - 1;
    }

    public TextAbilityBarComponent(Component text, boolean outline) {
        this(text);
        this.outline = outline;

        if (outline) {
            this.width += 2;
        }
    }

    @Override
    public int getWidth() {
        return this.width;
    }

    @Override
    public int getHeight() {
        return Minecraft.getInstance().font.lineHeight - (this.outline ? 0 : 2);
    }

    @Override
    public void render(Minecraft minecraft, GuiGraphics gui, int x, int y, AbilityBarAlignment alignment) {
        if (this.outline) {
            GuiUtil.drawStringWithBlackOutline(gui, this.text, x, y, RenderUtil.FULL_WHITE);
        } else {
            gui.drawString(minecraft.font, this.text, x, y, RenderUtil.FULL_WHITE, false);
        }
    }
}
