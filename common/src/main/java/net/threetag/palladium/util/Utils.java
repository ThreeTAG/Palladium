package net.threetag.palladium.util;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class Utils {

    public static <T> void ifNotNull(T value, Consumer<T> consumer) {
        if (value != null) {
            consumer.accept(value);
        }
    }

    public static void ifTrue(Boolean bool, Runnable runnable) {
        if (bool != null && bool) {
            runnable.run();
        }
    }

    @Nonnull
    public static <T> T orElse(@Nullable T val, T def) {
        return val != null ? val : def;
    }

    @Nonnull
    public static <T> T orElse(@Nullable T val, Supplier<T> def) {
        return val != null ? val : def.get();
    }

    public static String getFormattedNumber(long number) {
        if (number >= 1_000_000_000) {
            return number / 1_000_000_000 + "." + (number % 1_000_000_000 / 100_000_000) + (number % 100_000_000 / 10_000_000) + "G";
        } else if (number >= 1_000_000) {
            return number / 1_000_000 + "." + (number % 1_000_000 / 100_000) + (number % 100_000 / 10_000) + "M";
        } else if (number >= 1000) {
            return number / 1000 + "." + (number % 1000 / 100) + (number % 100 / 10) + "k";
        } else {
            return String.valueOf(number);
        }
    }

}
