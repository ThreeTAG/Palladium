package net.threetag.palladium.client.gui.screen.abilitybar;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;

import java.util.List;

public class CompoundAbilityBarComponent implements AbilityBarComponent {

    private final AbilityBarComponent[] components;
    public boolean vertical;
    public boolean center = false;
    public boolean reverseOrder = false;
    public int padding = 0;

    public CompoundAbilityBarComponent(List<AbilityBarComponent> components, boolean vertical) {
        this.components = components.toArray(new AbilityBarComponent[0]);
        this.vertical = vertical;
    }

    public CompoundAbilityBarComponent(boolean vertical, AbilityBarComponent... components) {
        this.components = components;
        this.vertical = vertical;
    }

    @Override
    public int getWidth() {
        int width = 0;

        for (AbilityBarComponent component : this.components) {
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

        for (AbilityBarComponent component : this.components) {
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
    public void render(Minecraft minecraft, GuiGraphics gui, int x, int y, AbilityBarAlignment alignment) {
        if (this.vertical) {
            verticalStackedRender(minecraft, gui, x, y, alignment, this.reverseOrder, this.center, this.padding, this.components);
        } else {
            horizontalStackedRender(minecraft, gui, x, y, alignment, this.reverseOrder, this.center, this.padding, this.components);
        }
    }

    public static void verticalStackedRender(Minecraft minecraft, GuiGraphics gui, int x, int y, AbilityBarAlignment alignment, boolean reverseOrder, boolean center, int padding, AbilityBarComponent... components) {
        int maxWidth = 0;

        for (AbilityBarComponent component : components) {
            maxWidth = Math.max(maxWidth, component.getWidth());
        }

        if (!reverseOrder) {
            for (AbilityBarComponent component : components) {
                int offsetX = x;

                if (center) {
                    offsetX += (maxWidth - component.getWidth()) / 2;
                } else if (alignment.isRight()) {
                    offsetX += maxWidth - component.getWidth();
                }

                component.render(minecraft, gui, offsetX, y, alignment);
                y += component.getHeight() + padding;
            }
        } else {
            for (int i = components.length - 1; i >= 0; i--) {
                var component = components[i];
                int offsetX = x;

                if (center) {
                    offsetX += (maxWidth - component.getWidth()) / 2;
                } else if (alignment.isRight()) {
                    offsetX += maxWidth - component.getWidth();
                }

                component.render(minecraft, gui, offsetX, y, alignment);
                y += component.getHeight() + padding;
            }
        }

    }

    public static void horizontalStackedRender(Minecraft minecraft, GuiGraphics gui, int x, int y, AbilityBarAlignment alignment, boolean reverseOrder, boolean center, int padding, AbilityBarComponent... components) {
        int maxHeight = 0;

        for (AbilityBarComponent component : components) {
            maxHeight = Math.max(maxHeight, component.getHeight());
        }

        if (!reverseOrder) {
            for (AbilityBarComponent component : components) {
                int offsetY = y;

                if (center) {
                    offsetY += (maxHeight - component.getHeight()) / 2;
                } else if (alignment.isBottom()) {
                    offsetY += maxHeight - component.getHeight();
                }

                component.render(minecraft, gui, x, offsetY, alignment);
                x += component.getWidth() + padding;
            }
        } else {
            for (int i = components.length - 1; i >= 0; i--) {
                var component = components[i];

                int offsetY = y;

                if (center) {
                    offsetY += (maxHeight - component.getHeight()) / 2;
                } else if (alignment.isBottom()) {
                    offsetY += maxHeight - component.getHeight();
                }

                component.render(minecraft, gui, x, offsetY, alignment);
                x += component.getWidth() + padding;
            }
        }
    }
}
