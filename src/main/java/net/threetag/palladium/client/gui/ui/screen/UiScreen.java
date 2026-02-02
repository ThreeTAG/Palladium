package net.threetag.palladium.client.gui.ui.screen;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.threetag.palladium.client.gui.ui.component.UiComponent;

public class UiScreen extends Screen {

    private final UiScreenConfiguration configuration;
    protected int innerLeftPos, outerLeftPos;
    protected int innerTopPos, outerTopPos;

    public UiScreen(UiScreenConfiguration configuration) {
        super(Component.empty());
        this.configuration = configuration;
    }

    @Override
    protected void init() {
        super.init();

        this.outerLeftPos = (this.width - this.configuration.width()) / 2;
        this.outerTopPos = (this.height - this.configuration.height()) / 2;
        this.innerLeftPos = this.outerLeftPos + this.configuration.padding();
        this.innerTopPos = this.outerTopPos + this.configuration.padding();

        for (UiComponent component : this.configuration.components()) {
            this.addRenderableWidget(component.buildWidget(this));
        }
    }

    @Override
    public void renderBackground(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.renderBackground(guiGraphics, mouseX, mouseY, partialTick);
        this.configuration.background().render(guiGraphics, this.outerLeftPos, this.outerTopPos, this.configuration.width(), this.configuration.height());
    }

    public ScreenRectangle getInnerRectangle() {
        return new ScreenRectangle(
                this.innerLeftPos,
                this.innerTopPos,
                this.configuration.width() - (this.configuration.padding() * 2),
                this.configuration.height() - (this.configuration.padding() * 2)
        );
    }
}
