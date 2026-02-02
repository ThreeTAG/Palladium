package net.threetag.palladium.client.gui.ui.screen;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.threetag.palladium.client.gui.ui.component.UiComponent;
import net.threetag.palladium.logic.context.DataContext;

import java.util.HashMap;
import java.util.Map;

public class UiScreen extends Screen {

    private final UiScreenConfiguration configuration;
    protected int innerLeftPos, outerLeftPos;
    protected int innerTopPos, outerTopPos;
    private final Map<UiComponent, AbstractWidget> widgets = new HashMap<>();

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
        this.widgets.clear();

        for (UiComponent component : this.configuration.components()) {
            var widget = component.buildWidget(this);
            widget.visible = component.getProperties().visibility().test(DataContext.forEntity(this.minecraft.player));
            this.widgets.put(component, widget);
            this.addRenderableWidget(widget);
        }
    }

    @Override
    public void tick() {
        super.tick();
        var context = DataContext.forEntity(this.minecraft.player);

        for (Map.Entry<UiComponent, AbstractWidget> e : this.widgets.entrySet()) {
            e.getValue().visible = e.getKey().getProperties().visibility().test(context);
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
