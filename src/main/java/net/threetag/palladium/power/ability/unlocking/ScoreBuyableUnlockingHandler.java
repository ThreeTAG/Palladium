package net.threetag.palladium.power.ability.unlocking;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.threetag.palladium.icon.Icon;
import net.threetag.palladium.logic.condition.Condition;
import net.threetag.palladium.logic.condition.TrueCondition;
import net.threetag.palladium.util.ScoreboardUtil;

public class ScoreBuyableUnlockingHandler extends BuyableUnlockingHandler {

    public static final MapCodec<ScoreBuyableUnlockingHandler> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.STRING.fieldOf("objective").forGetter(h -> h.objective),
            ExtraCodecs.POSITIVE_INT.optionalFieldOf("amount", 1).forGetter(h -> h.amount),
            Icon.CODEC.fieldOf("icon").forGetter(h -> h.icon),
            ComponentSerialization.CODEC.fieldOf("description").forGetter(h -> h.description),
            Condition.CODEC.optionalFieldOf("conditions", TrueCondition.INSTANCE).forGetter(h -> h.condition)
    ).apply(instance, ScoreBuyableUnlockingHandler::new));

    private final String objective;
    private final int amount;
    private final Icon icon;
    private final Component description;

    public ScoreBuyableUnlockingHandler(String objective, int amount, Icon icon, Component description, Condition condition) {
        super(condition);
        this.objective = objective;
        this.amount = amount;
        this.icon = icon;
        this.description = description;
    }

    @Override
    public boolean hasEnoughCurrency(LivingEntity entity) {
        if (entity instanceof Player player) {
            int score = ScoreboardUtil.getScore(player, this.objective);
            return score >= this.amount;
        }

        return false;
    }

    @Override
    public void consumeCurrency(LivingEntity entity) {
        if (entity instanceof Player player) {
            int score = ScoreboardUtil.getScore(player, this.objective);

            if (score >= this.amount) {
                ScoreboardUtil.setScore(player, this.objective, score - this.amount);
            }
        }

    }

    @Override
    public Display getDisplay() {
        return new Display(this.icon, this.amount, this.description);
    }

    @Override
    public UnlockingHandlerSerializer<?> getSerializer() {
        return UnlockingHandlerSerializers.SCORE_BUYABLE.get();
    }

    public static class Serializer extends UnlockingHandlerSerializer<ScoreBuyableUnlockingHandler> {

        @Override
        public MapCodec<ScoreBuyableUnlockingHandler> codec() {
            return CODEC;
        }

    }
}
