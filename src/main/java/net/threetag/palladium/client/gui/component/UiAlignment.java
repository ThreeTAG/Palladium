package net.threetag.palladium.client.gui.component;

public enum UiAlignment {

    TOP_LEFT(true, true),
    TOP_RIGHT(false, true),
    BOTTOM_LEFT(true, false),
    BOTTOM_RIGHT(false, false);

    private final boolean left, top;

    UiAlignment(boolean left, boolean top) {
        this.left = left;
        this.top = top;
    }

    public boolean isLeft() {
        return left;
    }

    public boolean isRight() {
        return !left;
    }

    public boolean isTop() {
        return top;
    }

    public boolean isBottom() {
        return !top;
    }
}
