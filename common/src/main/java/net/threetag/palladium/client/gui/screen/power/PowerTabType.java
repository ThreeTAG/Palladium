package net.threetag.palladium.client.gui.screen.power;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.RenderType;
import net.threetag.palladium.client.icon.Icon;
import net.threetag.palladium.data.DataContext;

@Environment(EnvType.CLIENT)
public enum PowerTabType {

    ABOVE(0, 0, 28, 32, 8),
    BELOW(84, 0, 28, 32, 8),
    LEFT(0, 64, 32, 28, 5),
    RIGHT(96, 64, 32, 28, 5);

    public static final int MAX_TABS = java.util.Arrays.stream(values()).mapToInt(e -> e.max).sum();
    private final int textureX;
    private final int textureY;
    private final int width;
    private final int height;
    private final int max;

    PowerTabType(int j, int k, int l, int m, int n) {
        this.textureX = j;
        this.textureY = k;
        this.width = l;
        this.height = m;
        this.max = n;
    }

    public int getMax() {
        return this.max;
    }

    public void draw(GuiGraphics guiGraphics, int offsetX, int offsetY, boolean isSelected, int index) {
        int i = this.textureX;
        if (index > 0) {
            i += this.width;
        }

        if (index == this.max - 1) {
            i += this.width;
        }

        int j = isSelected ? this.textureY + this.height : this.textureY;
        guiGraphics.blit(RenderPipelines.GUI_TEXTURED,PowersScreen.TABS, offsetX + this.getX(index), offsetY + this.getY(index), i, j, this.width, this.height, 256, 256);
    }

    public void drawIcon(GuiGraphics guiGraphics, DataContext context, int offsetX, int offsetY, int index, Icon icon) {
        int i = offsetX + this.getX(index);
        int j = offsetY + this.getY(index);
        switch (this) {
            case ABOVE -> {
                i += 6;
                j += 9;
            }
            case BELOW -> {
                i += 6;
                j += 6;
            }
            case LEFT -> {
                i += 10;
                j += 5;
            }
            case RIGHT -> {
                i += 6;
                j += 5;
            }
        }

        icon.draw(Minecraft.getInstance(), guiGraphics, context, i, j);
    }

    public int getX(int index) {
        return switch (this) {
            case ABOVE, BELOW -> (this.width + 4) * index;
            case LEFT -> -this.width + 4;
            case RIGHT -> 248;
        };
    }

    public int getY(int index) {
        return switch (this) {
            case ABOVE -> -this.height + 4;
            case BELOW -> 192;
            case LEFT, RIGHT -> this.height * index;
        };
    }

    public boolean isMouseOver(int offsetX, int offsetY, int index, double mouseX, double mouseY) {
        int i = offsetX + this.getX(index);
        int j = offsetY + this.getY(index);
        return mouseX > (double) i && mouseX < (double) (i + this.width) && mouseY > (double) j && mouseY < (double) (j + this.height);
    }
}
