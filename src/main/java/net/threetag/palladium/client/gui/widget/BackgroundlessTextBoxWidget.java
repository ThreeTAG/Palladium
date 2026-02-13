package net.threetag.palladium.client.gui.widget;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.FittingMultiLineTextWidget;
import net.minecraft.network.chat.Component;

public class BackgroundlessTextBoxWidget extends FittingMultiLineTextWidget {

    public BackgroundlessTextBoxWidget(int x, int y, int width, int height, Component message, Font font) {
        super(x, y, width - 6, height, message, font);
    }

    @Override
    protected void renderBackground(GuiGraphics guiGraphics) {

    }
}
