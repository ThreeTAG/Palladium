package net.threetag.palladium.util;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class Utils {

    public static <T> T tap(T object, Consumer<T> consumer) {
        consumer.accept(object);
        return object;
    }

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

    @NotNull
    public static <T> T orElse(@Nullable T val, T def) {
        return val != null ? val : def;
    }

    @NotNull
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

    public static <T> List<T> newList() {
        return new ArrayList<>();
    }

    public static <T> List<T> newList(int initialCapacity) {
        return new ArrayList<>(initialCapacity);
    }

    public static <T, R> Map<T, R> newMap() {
        return new HashMap<>();
    }

    public static <T, R> Map<T, R> newMap(int initialCapacity) {
        return new HashMap<>(initialCapacity);
    }

}
