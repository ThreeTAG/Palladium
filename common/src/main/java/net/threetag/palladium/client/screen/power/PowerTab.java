package net.threetag.palladium.client.screen.power;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.threetag.palladium.power.IPowerHolder;
import net.threetag.palladium.util.context.DataContext;
import net.threetag.palladium.util.icon.IIcon;

public abstract class PowerTab {

    protected final Minecraft minecraft;
    protected final PowersScreen screen;
    protected final PowerTabType type;
    protected final int index;
    protected final IPowerHolder powerHolder;
    protected final IIcon icon;
    protected final Component title;
    public float fade = 0F;

    protected PowerTab(Minecraft minecraft, PowersScreen screen, PowerTabType type, int tabIndex, IPowerHolder powerHolder) {
        this.minecraft = minecraft;
        this.screen = screen;
        this.type = type;
        this.index = tabIndex;
        this.powerHolder = powerHolder;
        this.icon = powerHolder.getPower().getIcon();
        this.title = powerHolder.getPower().getName();
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

    public void mouseClicked(double mouseX, double mouseY, int button) {

    }

    public void onOpened() {

    }

    public void onClosed() {

    }
}
