package net.threetag.palladium.client.gui.screen.power;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;
import net.threetag.palladium.icon.Icon;
import net.threetag.palladium.logic.context.DataContext;
import net.threetag.palladium.power.PowerHolder;

public abstract class PowerTab {

    protected final Minecraft minecraft;
    protected final PowersScreen screen;
    protected final PowerTabType type;
    protected final int index;
    protected final PowerHolder powerHolder;
    protected final Icon icon;
    protected final Component title;
    public float fade = 0F;

    protected PowerTab(Minecraft minecraft, PowersScreen screen, PowerTabType type, int tabIndex, PowerHolder powerHolder) {
        this.minecraft = minecraft;
        this.screen = screen;
        this.type = type;
        this.index = tabIndex;
        this.powerHolder = powerHolder;
        this.icon = powerHolder.getPower().value().getIcon();
        this.title = powerHolder.getPower().value().getName();
    }

    public void drawTab(GuiGraphics guiGraphics, int offsetX, int offsetY, boolean isSelected) {
        this.type.draw(guiGraphics, offsetX, offsetY, isSelected, this.index);
    }

    public void drawIcon(GuiGraphics guiGraphics, int offsetX, int offsetY) {
        this.type.drawIcon(guiGraphics, DataContext.forPower(this.minecraft.player, this.powerHolder), offsetX, offsetY, this.index, this.icon);
    }

    public boolean isMouseOver(int offsetX, int offsetY, double mouseX, double mouseY) {
        return this.type.isMouseOver(offsetX, offsetY, this.index, mouseX, mouseY);
    }

    public PowerTabType getType() {
        return this.type;
    }

    public int getIndex() {
        return this.index;
    }

    public Component getTitle() {
        return this.title;
    }

    public PowersScreen getScreen() {
        return this.screen;
    }

    public abstract void populate();

    public abstract void drawContents(GuiGraphics guiGraphics, int x, int y, int mouseX, int mouseY, float partialTick);

    public abstract void drawTooltips(GuiGraphics guiGraphics, int mouseX, int mouseY, int width, int height, float partialTick, boolean overlayActive);

    public void mouseClicked(MouseButtonEvent event, boolean isDoubleClick) {

    }

    public void onOpened() {

    }

    public void onClosed() {

    }
}
