package net.threetag.palladium.power.ability;

public enum AbilityColor {

    WHITE(104, 0),
    ORANGE(104, 24),
    MAGENTA(104, 48),
    LIGHT_BLUE(104, 72),
    YELLOW(104, 96),
    LIME(104, 120),
    PINK(104, 133),
    GRAY(104, 168),
    LIGHT_GRAY(128, 0),
    CYAN(128, 24),
    PURPLE(128, 48),
    BLUE(128, 72),
    BROWN(128, 96),
    GREEN(128, 120),
    RED(128, 144),
    BLACK(128, 168);

    private final int x;
    private final int y;

    AbilityColor(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public static AbilityColor getByName(String name) {
        for (AbilityColor type : values()) {
            if (type.name().equalsIgnoreCase(name)) {
                return type;
            }
        }
        return null;
    }

}
