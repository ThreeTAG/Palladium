package net.threetag.palladium.client.gui.component;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.network.chat.Component;
import net.threetag.palladium.Palladium;

public class FlatButton extends ImageButton {

    public static final WidgetSprites SPRITES = new WidgetSprites(
            Palladium.id("widget/flat_button"), Palladium.id("widget/flat_button_disabled"), Palladium.id("widget/flat_button_highlighted")
    );

    public FlatButton(int x, int y, int width, int height, OnPress onPress) {
        super(x, y, width, height, SPRITES, onPress);
    }

    public FlatButton(int x, int y, int width, int height, OnPress onPress, Component message) {
        super(x, y, width, height, SPRITES, onPress, message);
    }

    public FlatButton(int width, int height, OnPress onPress, Component message) {
        super(width, height, SPRITES, onPress, message);
    }

    @Override
    public void renderContents(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.renderContents(guiGraphics, mouseX, mouseY, partialTick);
        this.renderDefaultLabel(guiGraphics.textRendererForWidget(this, GuiGraphics.HoveredTextEffects.NONE));
    }
}
