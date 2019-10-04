package net.threetag.threecore.abilities.condition;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Util;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.threetag.threecore.abilities.Ability;
import net.threetag.threecore.util.threedata.EnumSync;
import net.threetag.threecore.util.threedata.IntegerThreeData;
import net.threetag.threecore.util.threedata.ThreeData;

public class XPCondition extends Condition {

    public static final ThreeData<Integer> MIN = new IntegerThreeData("min").setSyncType(EnumSync.SELF).enableSetting("min", "The minimum xp level required for the condition to be true.");
    public static final ThreeData<Integer> MAX = new IntegerThreeData("max").setSyncType(EnumSync.SELF).enableSetting("max", "The maximum xp level required for the condition to be true.");

    public XPCondition(Ability ability) {
        super(ConditionType.XP, ability);
    }

    @Override
    public void registerData() {
        super.registerData();
        this.dataManager.register(MIN, 0);
        this.dataManager.register(MAX, Integer.MAX_VALUE);
    }

    @Override
    public ITextComponent createTitle() {
        int min = this.dataManager.get(MIN);
        int max = this.dataManager.get(MAX);
        if (min == max) {
            return new TranslationTextComponent(Util.makeTranslationKey("ability.condition", this.type.getRegistryName()) + "_at" + (this.dataManager.get(INVERT) ? ".not" : ""), min);
        } else if (max != Integer.MAX_VALUE) {
            return new TranslationTextComponent(Util.makeTranslationKey("ability.condition", this.type.getRegistryName()) + (this.dataManager.get(INVERT) ? ".not" : ""), min, max);
        } else
            return new TranslationTextComponent(Util.makeTranslationKey("ability.condition", this.type.getRegistryName()) + "_min" + (this.dataManager.get(INVERT) ? ".not" : ""), min);
    }

    @Override
    public boolean test(LivingEntity entity) {
        if (entity instanceof PlayerEntity) {
            int level = ((PlayerEntity) entity).experienceLevel;
            return level >= this.dataManager.get(MIN) && level <= this.dataManager.get(MAX);
        }
        return false;
    }
}
