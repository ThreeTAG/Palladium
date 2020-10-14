package net.threetag.threecore.client.gui.ability;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.threetag.threecore.util.icon.IIcon;

public enum AbilityTabType {

    ABOVE(0, 0, 28, 32, 8),
    BELOW(84, 0, 28, 32, 8),
    LEFT(0, 64, 32, 28, 7),
    RIGHT(96, 64, 32, 28, 7);

    public static final int MAX_TABS = java.util.Arrays.stream(values()).mapToInt(e -> e.max).sum();
    private final int textureX;
    private final int textureY;
    private final int width;
    private final int height;
    private final int max;

    AbilityTabType(int textureX, int textureY, int widthIn, int heightIn, int max) {
        this.textureX = textureX;
        this.textureY = textureY;
        this.width = widthIn;
        this.height = heightIn;
        this.max = max;
    }

    public int getMax() {
        return this.max;
    }

    public void draw(AbstractGui guiIn, MatrixStack stack, int x, int y, boolean selected, int index) {
        int i = this.textureX;
        if (index > 0) {
            i += this.width;
        }

        if (index == this.max - 1) {
            i += this.width;
        }

        int j = selected ? this.textureY + this.height : this.textureY;
        guiIn.blit(stack, x + this.getX(index), y + this.getY(index), i, j, this.width, this.height);
    }

    public void drawIcon(MatrixStack stack, int x, int y, int index, IIcon icon) {
        int i = x + this.getX(index);
        int j = y + this.getY(index);
        switch (this) {
            case ABOVE:
                i += 6;
                j += 9;
                break;
            case BELOW:
                i += 6;
                j += 6;
                break;
            case LEFT:
                i += 10;
                j += 5;
                break;
            case RIGHT:
                i += 6;
                j += 5;
        }

        icon.draw(Minecraft.getInstance(), stack, i, j);
    }

    public int getX(int index) {
        switch (this) {
            case ABOVE:
            case BELOW:
                return (this.width + 4) * index;
            case LEFT:
                return -this.width + 4;
            case RIGHT:
                return 248;
            default:
                throw new UnsupportedOperationException("Don't know what this tab type is!" + this);
        }
    }

    public int getY(int index) {
        switch (this) {
            case ABOVE:
                return -this.height + 4;
            case BELOW:
                return 136;
            case LEFT:
            case RIGHT:
                return this.height * index;
            default:
                throw new UnsupportedOperationException("Don't know what this tab type is!" + this);
        }
    }

    public boolean isMouseOver(int x, int y, int index, double p_198891_4_, double p_198891_6_) {
        int i = x + this.getX(index);
        int j = y + this.getY(index);
        return p_198891_4_ > (double) i && p_198891_4_ < (double) (i + this.width) && p_198891_6_ > (double) j && p_198891_6_ < (double) (j + this.height);
    }

}
