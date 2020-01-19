package net.threetag.threecore.util.math;

public class TCMathHelper {

    public static double round(double value, int places) {
        if (places < 0)
            throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }

    public static float interpolate(float f1, float f2, float partial) {
        return f1 + (f2 - f1) * partial;
    }

    public static double interpolate(double d1, double d2, float partial) {
        return d1 + (d2 - d1) * partial;
    }
}
