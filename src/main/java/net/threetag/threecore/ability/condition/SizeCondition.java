package net.threetag.threecore.ability.condition;

import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Util;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.threetag.threecore.ability.Ability;
import net.threetag.threecore.capability.CapabilitySizeChanging;
import net.threetag.threecore.util.threedata.EnumSync;
import net.threetag.threecore.util.threedata.FloatThreeData;
import net.threetag.threecore.util.threedata.ThreeData;

import java.util.concurrent.atomic.AtomicBoolean;

public class SizeCondition extends Condition {

    public static final ThreeData<Float> MIN = new FloatThreeData("min").setSyncType(EnumSync.SELF).enableSetting("min", "The minimum size required for the condition to be true.");
    public static final ThreeData<Float> MAX = new FloatThreeData("max").setSyncType(EnumSync.SELF).enableSetting("max", "The maximum size required for the condition to be true.");

    public SizeCondition(Ability ability) {
        super(ConditionType.SIZE, ability);
    }

    @Override
    public boolean test(LivingEntity entity) {
        AtomicBoolean b = new AtomicBoolean(false);
        entity.getCapability(CapabilitySizeChanging.SIZE_CHANGING).ifPresent(s -> b.set(s.getScale() >= this.dataManager.get(MIN) && s.getScale() <= this.dataManager.get(MAX)));
        return b.get();
    }

    @Override
    public void registerData() {
        super.registerData();
        this.dataManager.register(MIN, 0F);
        this.dataManager.register(MAX, CapabilitySizeChanging.MAX_SIZE);
    }

    @Override
    public ITextComponent createTitle() {
        float min = this.dataManager.get(MIN);
        float max = this.dataManager.get(MAX);
        if (min == max) {
            return new TranslationTextComponent(Util.makeTranslationKey("ability.condition", this.type.getRegistryName()) + "_at" + (this.dataManager.get(INVERT) ? ".not" : ""), min);
        } else if (max != Integer.MAX_VALUE) {
            return new TranslationTextComponent(Util.makeTranslationKey("ability.condition", this.type.getRegistryName()) + (this.dataManager.get(INVERT) ? ".not" : ""), min, max);
        } else
            return new TranslationTextComponent(Util.makeTranslationKey("ability.condition", this.type.getRegistryName()) + "_min" + (this.dataManager.get(INVERT) ? ".not" : ""), min);
    }

}
