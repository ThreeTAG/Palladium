package net.threetag.palladium.util.property;

import java.util.Locale;

public class TextureAlignmentProperty extends EnumPalladiumProperty<TextureAlignmentProperty.TextureAlignment> {

    public TextureAlignmentProperty(String key) {
        super(key);
    }

    @Override
    public TextureAlignment[] getValues() {
        return TextureAlignment.values();
    }

    @Override
    public String getNameFromEnum(TextureAlignment value) {
        return value.name().toLowerCase(Locale.ROOT);
    }

    public enum TextureAlignment {

        TOP_LEFT(0, 0, false),
        TOP_CENTER(1, 0, false),
        TOP_RIGHT(2, 0, false),
        MIDDLE_LEFT(0, 1, false),
        CENTER(1, 1, false),
        MIDDLE_RIGHT(2, 1, false),
        BOTTOM_LEFT(0, 2, false),
        BOTTOM_CENTER(1, 2, false),
        BOTTOM_RIGHT(2, 2, false),
        STRETCH(0, 0, true);

        private final int horizontal, vertical;
        private final boolean stretch;

        TextureAlignment(int horizontal, int vertical, boolean stretch) {
            this.horizontal = horizontal;
            this.vertical = vertical;
            this.stretch = stretch;
        }

        public int getHorizontal() {
            return horizontal;
        }

        public int getVertical() {
            return vertical;
        }

        public boolean isStretched() {
            return stretch;
        }
    }

}
