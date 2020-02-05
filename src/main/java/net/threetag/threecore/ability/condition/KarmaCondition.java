package net.threetag.threecore.ability.condition;

import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Util;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.threetag.threecore.ability.Ability;
import net.threetag.threecore.capability.CapabilityKarma;
import net.threetag.threecore.util.threedata.EnumSync;
import net.threetag.threecore.util.threedata.IntegerThreeData;
import net.threetag.threecore.util.threedata.ThreeData;

import java.util.concurrent.atomic.AtomicBoolean;

public class KarmaCondition extends Condition {

    public static final ThreeData<Integer> MIN = new IntegerThreeData("min").setSyncType(EnumSync.SELF).enableSetting("min", "The minimum karma required for the condition to be true.");
    public static final ThreeData<Integer> MAX = new IntegerThreeData("max").setSyncType(EnumSync.SELF).enableSetting("max", "The maximum karma required for the condition to be true.");

    public KarmaCondition(Ability ability) {
        super(ConditionType.KARMA, ability);
    }

    @Override
    public boolean test(LivingEntity entity) {
        AtomicBoolean b = new AtomicBoolean(false);
        entity.getCapability(CapabilityKarma.KARMA).ifPresent(k -> b.set(k.getKarma() >= this.dataManager.get(MIN) && k.getKarma() <= this.dataManager.get(MAX)));
        return b.get();
    }

    @Override
    public void registerData() {
        super.registerData();
        this.dataManager.register(MIN, Integer.MIN_VALUE);
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

}
