package net.threetag.palladium.util;

import net.minecraft.world.entity.Entity;

public class ScoreboardUtil {

    public static int getScore(Entity entity, String objective) {
        return getScore(entity, objective, 0);
    }

    public static int getScore(Entity entity, String objective, int fallback) {
        var scoreboard = entity.level().getScoreboard();
        var obj = scoreboard.getObjective(objective);

        if (obj != null) {
            var score = scoreboard.getPlayerScoreInfo(entity, obj);

            if (score != null) {
                return score.value();
            }
        }

        return fallback;
    }

    public static int setScore(Entity entity, String objective, int value) {
        var scoreboard = entity.level().getScoreboard();
        var obj = scoreboard.getObjective(objective);

        if (obj != null) {
            var score = scoreboard.getOrCreatePlayerScore(entity, obj);
            score.set(value);
            return score.get();
        }

        return 0;
    }

    public static int addScore(Entity entity, String objective, int amount) {
        var scoreboard = entity.level().getScoreboard();
        var obj = scoreboard.getObjective(objective);

        if (obj != null) {
            var score = scoreboard.getOrCreatePlayerScore(entity, obj);
            score.add(amount);
            return score.get();
        }

        return 0;
    }

    public static int subtractScore(Entity entity, String objective, int amount) {
        var scoreboard = entity.level().getScoreboard();
        var obj = scoreboard.getObjective(objective);

        if (obj != null) {
            var score = scoreboard.getOrCreatePlayerScore(entity, obj);
            score.add(-amount);
            return score.get();
        }

        return 0;
    }

}
