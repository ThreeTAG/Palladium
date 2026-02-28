package net.threetag.palladium.customization;

public class EyeSelectionUtil {

    // Steve skin eye position
    public static final long DEFAULT_EYES = 438086664192L;

    public static boolean isPixelSelected(long eyeSelection, int x, int y) {
        int index = y * 8 + x;
        return (eyeSelection & (1L << index)) != 0;
    }

    public static long setPixelSelected(long eyeSelection, int x, int y) {
        int index = y * 8 + x;
        return eyeSelection | (1L << index);
    }

    public static long setPixelSelected(long eyeSelection, int x, int y, boolean selected) {
        int index = y * 8 + x;
        return selected ? eyeSelection | (1L << index) : eyeSelection & ~(1L << index);
    }

}
