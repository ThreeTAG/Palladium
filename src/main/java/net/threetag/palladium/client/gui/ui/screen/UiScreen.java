package net.threetag.palladium.client.gui.ui.screen;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.threetag.palladium.client.gui.screen.DelayedRenderCallReceiver;
import net.threetag.palladium.client.gui.ui.component.UiComponent;
import net.threetag.palladium.client.gui.ui.layout.UiLayout;
import net.threetag.palladium.logic.context.DataContext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class UiScreen extends Screen implements DelayedRenderCallReceiver {

    private UiLayout layout;
    protected int layoutWidth, layoutHeight;
    protected int leftPos;
    protected int topPos;
    private final Map<UiComponent, AbstractWidget> widgets = new HashMap<>();
    private final List<Consumer<GuiGraphics>> delayedRenderCalls = new ArrayList<>();

    public UiScreen(UiLayout layout) {
        super(Component.empty());
        this.layout = layout;
        this.layoutWidth = layout.getWidth();
        this.layoutHeight = layout.getHeight();
    }

    @Override
    protected void init() {
        super.init();

        this.leftPos = (this.width - this.layoutWidth) / 2;
        this.topPos = (this.height - this.layoutHeight) / 2;
        this.loadComponents(this.layout);
    }

    protected void loadComponents(UiLayout layout) {
        this.widgets.clear();

        layout.addComponents(this, this.leftPos, this.topPos, (component, widget) -> {
            widget.visible = component.getProperties().visibility().test(DataContext.forEntity(this.minecraft.player));
            this.widgets.put(component, widget);
            this.addRenderableWidget(widget);
        });
    }

    protected void changeLayout(UiLayout layout) {
        for (AbstractWidget widget : this.widgets.values()) {
            this.removeWidget(widget);
        }

        this.layout = layout;
        this.loadComponents(layout);
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
        this.layout.renderBackground(guiGraphics, this.leftPos, this.topPos);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);

        for (Consumer<GuiGraphics> renderCall : this.delayedRenderCalls) {
            renderCall.accept(guiGraphics);
        }

        this.delayedRenderCalls.clear();
    }

    public UiLayout getLayout() {
        return this.layout;
    }

    @Override
    public void renderDelayed(Consumer<GuiGraphics> consumer) {
        this.delayedRenderCalls.add(consumer);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
