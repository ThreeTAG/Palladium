package net.threetag.threecore.abilities.client;

public enum EnumAbilityColor {

    WHITE(0, 0),
    ORANGE(22, 0),
    MAGENTA(44, 0),
    LIGHT_BLUE(66, 0),
    YELLOW(88, 0),
    LIME(110, 0),
    PINK(132, 0),
    GRAY(154, 0),
    LIGHT_GRAY(176, 0),
    CYAN(198, 0),
    PURPLE(220, 0),
    BLUE(0, 22),
    BROWN(22, 22),
    GREEN(44, 22),
    RED(66, 22),
    BLACK(88, 22);

    protected int x;
    protected int y;

    EnumAbilityColor(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
