package net.threetag.palladium.client.gui.component;

import com.mojang.blaze3d.platform.Window;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import org.joml.Vector2i;

public interface UiComponent {

    int getWidth();

    int getHeight();

    void render(Minecraft minecraft, GuiGraphics gui, DeltaTracker deltaTracker, int x, int y, UiAlignment alignment);

    static Vector2i getPosition(UiComponent component, Window window, UiAlignment alignment) {
        return new Vector2i(
                alignment.isLeft() ? 0 : window.getGuiScaledWidth() - component.getWidth(),
                alignment.isBottom() ? window.getGuiScaledHeight() - component.getHeight() : 0
        );
    }

    static void verticalStackedRender(Minecraft minecraft, GuiGraphics gui, DeltaTracker deltaTracker, int x, int y, UiAlignment alignment, boolean reverseOrder, boolean center, int padding, UiComponent... components) {
        int maxWidth = 0;

        for (UiComponent component : components) {
            maxWidth = Math.max(maxWidth, component.getWidth());
        }

        if (!reverseOrder) {
            for (UiComponent component : components) {
                int offsetX = x;

                if (center) {
                    offsetX += (maxWidth - component.getWidth()) / 2;
                } else if (alignment.isRight()) {
                    offsetX += maxWidth - component.getWidth();
                }

                component.render(minecraft, gui, deltaTracker, offsetX, y, alignment);
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

                component.render(minecraft, gui, deltaTracker, offsetX, y, alignment);
                y += component.getHeight() + padding;
            }
        }

    }

    static void horizontalStackedRender(Minecraft minecraft, GuiGraphics gui, DeltaTracker deltaTracker, int x, int y, UiAlignment alignment, boolean reverseOrder, boolean center, int padding, UiComponent... components) {
        int maxHeight = 0;

        for (UiComponent component : components) {
            maxHeight = Math.max(maxHeight, component.getHeight());
        }

        if (!reverseOrder) {
            for (UiComponent component : components) {
                int offsetY = y;

                if (center) {
                    offsetY += (maxHeight - component.getHeight()) / 2;
                } else if (alignment.isBottom()) {
                    offsetY += maxHeight - component.getHeight();
                }

                component.render(minecraft, gui, deltaTracker, x, offsetY, alignment);
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

                component.render(minecraft, gui, deltaTracker, x, offsetY, alignment);
                x += component.getWidth() + padding;
            }
        }
    }

}
