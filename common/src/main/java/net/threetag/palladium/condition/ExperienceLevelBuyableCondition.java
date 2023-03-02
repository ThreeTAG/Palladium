package net.threetag.palladium.condition;

import com.google.gson.JsonObject;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.threetag.palladium.power.ability.AbilityConfiguration;
import net.threetag.palladium.util.icon.ExperienceIcon;
import net.threetag.palladium.util.property.IntegerProperty;
import net.threetag.palladium.util.property.PalladiumProperty;

public class ExperienceLevelBuyableCondition extends BuyableCondition {

    private final int xpLevel;

    public ExperienceLevelBuyableCondition(int xpLevel) {
        this.xpLevel = xpLevel;
    }

    @Override
    public AbilityConfiguration.UnlockData createData() {
        return new AbilityConfiguration.UnlockData(new ExperienceIcon(1, true), this.xpLevel, Component.translatable("gui.palladium.powers.buy_ability.experience_level" + (this.xpLevel > 1 ? "_plural" : "")));
    }

    @Override
    public boolean isAvailable(LivingEntity entity) {
        if (entity instanceof Player player) {
            return player.experienceLevel >= this.xpLevel;
        }

        return false;
    }

    @Override
    public boolean takeFromEntity(LivingEntity entity) {
        if (entity instanceof Player player) {
            if (player.experienceLevel >= this.xpLevel) {
                player.giveExperienceLevels(-this.xpLevel);
                return true;
            }
        }

        return false;
    }

    @Override
    public ConditionSerializer getSerializer() {
        return ConditionSerializers.EXPERIENCE_LEVEL_BUYABLE.get();
    }

    public static class Serializer extends ConditionSerializer {

        public static final PalladiumProperty<Integer> XP_LEVEL = new IntegerProperty("xp_level").configurable("Amount xp level that the player needs to spend");

        public Serializer() {
            this.withProperty(XP_LEVEL, 3);
        }

        @Override
        public ConditionContextType getContextType() {
            return ConditionContextType.ABILITIES;
        }

        @Override
        public Condition make(JsonObject json) {
            return new ExperienceLevelBuyableCondition(getProperty(json, XP_LEVEL));
        }

        @Override
        public String getDocumentationDescription() {
            return "A condition that makes the ability buyable for a certain amount of xp levels.";
        }
    }
}
