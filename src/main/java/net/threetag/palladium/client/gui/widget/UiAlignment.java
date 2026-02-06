package net.threetag.palladium.client.gui.widget;

import com.mojang.serialization.Codec;
import net.minecraft.client.gui.TextAlignment;
import net.minecraft.util.StringRepresentable;
import org.jspecify.annotations.NonNull;

public enum UiAlignment implements StringRepresentable {

    TOP_LEFT("top_left", TextAlignment.LEFT, TextAlignment.LEFT),
    TOP_CENTER("top_center", TextAlignment.CENTER, TextAlignment.LEFT),
    TOP_RIGHT("top_right", TextAlignment.RIGHT, TextAlignment.LEFT),
    MIDDLE_LEFT("middle_left", TextAlignment.LEFT, TextAlignment.CENTER),
    CENTER("center", TextAlignment.CENTER, TextAlignment.CENTER),
    MIDDLE_RIGHT("middle_right", TextAlignment.RIGHT, TextAlignment.CENTER),
    BOTTOM_LEFT("bottom_left", TextAlignment.LEFT, TextAlignment.RIGHT),
    BOTTOM_CENTER("bottom_center", TextAlignment.CENTER, TextAlignment.RIGHT),
    BOTTOM_RIGHT("bottom_right", TextAlignment.RIGHT, TextAlignment.RIGHT);

    public static final Codec<UiAlignment> CODEC = StringRepresentable.fromEnum(UiAlignment::values);

    private final String name;
    private final TextAlignment horizontalAlignment, verticalAlignment;

    UiAlignment(String name, TextAlignment horizontalAlignment, TextAlignment verticalAlignment) {
        this.name = name;
        this.horizontalAlignment = horizontalAlignment;
        this.verticalAlignment = verticalAlignment;
    }

    @Override
    public @NonNull String getSerializedName() {
        return this.name;
    }

    public TextAlignment getHorizontalAlignment() {
        return this.horizontalAlignment;
    }

    public TextAlignment getVerticalAlignment() {
        return this.verticalAlignment;
    }

    public boolean isLeft() {
        return this.horizontalAlignment == TextAlignment.LEFT;
    }

    public boolean isHorizontalCenter() {
        return this.horizontalAlignment == TextAlignment.CENTER;
    }

    public boolean isRight() {
        return this.horizontalAlignment == TextAlignment.RIGHT;
    }

    public boolean isTop() {
        return this.verticalAlignment == TextAlignment.LEFT;
    }

    public boolean isMiddle() {
        return this.verticalAlignment == TextAlignment.CENTER;
    }

    public boolean isBottom() {
        return this.verticalAlignment == TextAlignment.RIGHT;
    }
}
