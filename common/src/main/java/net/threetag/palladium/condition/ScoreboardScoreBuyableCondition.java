package net.threetag.palladium.condition;

import com.google.gson.JsonObject;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.threetag.palladium.power.ability.AbilityConfiguration;
import net.threetag.palladium.util.icon.IIcon;
import net.threetag.palladium.util.icon.ItemIcon;
import net.threetag.palladium.util.property.*;

public class ScoreboardScoreBuyableCondition extends BuyableCondition {

    private final String objective;
    private final int amount;
    private final IIcon icon;
    private final Component description;

    public ScoreboardScoreBuyableCondition(String objective, int amount, IIcon icon, Component description) {
        this.objective = objective;
        this.amount = amount;
        this.icon = icon;
        this.description = description;
    }

    @Override
    public AbilityConfiguration.UnlockData createData() {
        return new AbilityConfiguration.UnlockData(this.icon, this.amount, this.description);
    }

    @Override
    public boolean isAvailable(LivingEntity entity) {
        if (entity instanceof Player player) {
            var objective = player.getScoreboard().getObjective(this.objective);
            return objective != null && player.getScoreboard().getOrCreatePlayerScore(player.getScoreboardName(), objective).getScore() >= this.amount;
        }

        return false;
    }

    @Override
    public boolean takeFromEntity(LivingEntity entity) {
        if (entity instanceof Player player) {
            var objective = player.getScoreboard().getObjective(this.objective);

            if(objective != null) {
                var score = player.getScoreboard().getOrCreatePlayerScore(player.getScoreboardName(), objective);

                if(score.getScore() >= this.amount) {
                    score.setScore(score.getScore() - this.amount);
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public ConditionSerializer getSerializer() {
        return ConditionSerializers.SCOREBOARD_SCORE_BUYABLE.get();
    }

    public static class Serializer extends ConditionSerializer {

        public static final PalladiumProperty<String> OBJECTIVE = new StringProperty("objective").configurable("Name of the scoreboard objective");
        public static final PalladiumProperty<Integer> SCORE = new IntegerProperty("score").configurable("Required player score for the scoreboard objective");
        public static final PalladiumProperty<IIcon> ICON = new IconProperty("icon").configurable("Icon that will be displayed during buying");
        public static final PalladiumProperty<Component> DESCRIPTION = new ComponentProperty("description").configurable("Name of the score that will be displayed during buying");

        public Serializer() {
            this.withProperty(OBJECTIVE, "objective_name");
            this.withProperty(SCORE, 3);
            this.withProperty(ICON, new ItemIcon(Items.COMMAND_BLOCK));
            this.withProperty(DESCRIPTION, Component.literal("Scoreboard Score"));
        }

        @Override
        public ConditionContextType getContextType() {
            return ConditionContextType.ABILITIES;
        }

        @Override
        public Condition make(JsonObject json) {
            return new ScoreboardScoreBuyableCondition(getProperty(json, OBJECTIVE), getProperty(json, SCORE), getProperty(json, ICON), getProperty(json, DESCRIPTION));
        }

        @Override
        public String getDescription() {
            return "A buyable condition that requires a certain score for a scoreboard objective.";
        }
    }
}
