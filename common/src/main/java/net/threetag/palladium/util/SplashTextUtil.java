package net.threetag.palladium.util;

import com.mojang.datafixers.util.Pair;
import org.apache.commons.lang3.tuple.Triple;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class SplashTextUtil {

    private static final List<Triple<Integer, Integer, String>> DATED = new ArrayList<>();
    private static final List<Pair<Integer, String>> RANDOM = new ArrayList<>();

    static {
        addDated(20, 12, "Happy birthday, Lucraft!");
        addDated(16, 7, "Happy birthday, Sheriff!");
        addDated(27, 10, "Happy birthday, Connor!");
        addDated(8, 1, "TEST DU WICHT");

        addRandom(300, "Also try FiskHeroes!");
    }

    public static void addRandom(int rarity, String splash) {
        RANDOM.add(Pair.of(rarity, splash));
    }

    public static void addDated(int day, int month, String splash) {
        DATED.add(Triple.of(day, month, splash));
    }

    @Nullable
    public static String getPossibleOverrideSplash() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DATE);

        for (Triple<Integer, Integer, String> triple : DATED) {
            if (triple.getLeft() == day && triple.getMiddle() == month) {
                return triple.getRight();
            }
        }

        Random random = new Random();

        for (Pair<Integer, String> pair : RANDOM) {
            if (random.nextInt(pair.getFirst()) == 0) {
                return pair.getSecond();
            }
        }

        return null;
    }

}
