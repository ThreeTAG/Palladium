package net.threetag.palladium.client.gui.component;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;

import java.util.List;

public class CompoundUiComponent implements UiComponent {

    private final UiComponent[] components;
    public boolean vertical;
    public boolean center = false;
    public boolean reverseOrder = false;
    public int padding = 0;

    public CompoundUiComponent(List<UiComponent> components, boolean vertical) {
        this.components = components.toArray(new UiComponent[0]);
        this.vertical = vertical;
    }

    public CompoundUiComponent(boolean vertical, UiComponent... components) {
        this.components = components;
        this.vertical = vertical;
    }

    @Override
    public int getWidth() {
        int width = 0;

        for (UiComponent component : this.components) {
            if (this.vertical) {
                width = Math.max(width, component.getWidth());
            } else {
                width += component.getWidth();
            }
        }

        if (!this.vertical) {
            width += (this.components.length - 1) * this.padding;
        }

        return width;
    }

    @Override
    public int getHeight() {
        int height = 0;

        for (UiComponent component : this.components) {
            if (this.vertical) {
                height += component.getHeight();
            } else {
                height = Math.max(height, component.getHeight());
            }
        }

        if (this.vertical) {
            height += (this.components.length - 1) * this.padding;
        }

        return height;
    }

    @Override
    public void render(Minecraft minecraft, GuiGraphics gui, DeltaTracker deltaTracker, int x, int y, UiAlignment alignment) {
        if (this.vertical) {
            UiComponent.verticalStackedRender(minecraft, gui, deltaTracker, x, y, alignment, this.reverseOrder, this.center, this.padding, this.components);
        } else {
            UiComponent.horizontalStackedRender(minecraft, gui, deltaTracker, x, y, alignment, this.reverseOrder, this.center, this.padding, this.components);
        }
    }
}
