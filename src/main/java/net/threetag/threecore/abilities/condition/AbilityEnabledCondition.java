package net.threetag.threecore.abilities.condition;

import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Util;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.threetag.threecore.abilities.Ability;
import net.threetag.threecore.abilities.AbilityHelper;
import net.threetag.threecore.util.threedata.EnumSync;
import net.threetag.threecore.util.threedata.StringThreeData;
import net.threetag.threecore.util.threedata.TextComponentThreeData;
import net.threetag.threecore.util.threedata.ThreeData;

public class AbilityEnabledCondition extends Condition {

    public static final ThreeData<String> ABILITY_ID = new StringThreeData("ability_id").setSyncType(EnumSync.SELF).enableSetting("ability_id", "The id for the ability that must be enabled for the condition to be true.");
    public static final ThreeData<ITextComponent> ABILITY_TITLE = new TextComponentThreeData("ability_title").setSyncType(EnumSync.SELF);

    public AbilityEnabledCondition(Ability ability) {
        super(ConditionType.ABILITY_ENABLED, ability);
    }

    @Override
    public void registerData() {
        super.registerData();
        this.dataManager.register(ABILITY_ID, "");
        this.dataManager.register(ABILITY_TITLE, new StringTextComponent("/"));
    }

    @Override
    public ITextComponent createTitle() {
        return new TranslationTextComponent(Util.makeTranslationKey("ability.condition", this.type.getRegistryName()) + (this.dataManager.get(INVERT) ? ".not" : ""), this.dataManager.get(ABILITY_TITLE));
    }

    @Override
    public boolean test(LivingEntity entity) {
        Ability dependentAbility = AbilityHelper.getAbilityById(entity, this.dataManager.get(ABILITY_ID), ability.container);
        return dependentAbility != null && dependentAbility != ability && dependentAbility.getConditionManager().isEnabled();
    }
}
