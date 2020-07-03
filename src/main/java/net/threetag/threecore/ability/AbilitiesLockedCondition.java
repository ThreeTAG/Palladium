package net.threetag.threecore.ability;

import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Util;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.threetag.threecore.ability.condition.Condition;
import net.threetag.threecore.ability.condition.ConditionType;
import net.threetag.threecore.util.threedata.EnumSync;
import net.threetag.threecore.util.threedata.StringArrayThreeData;
import net.threetag.threecore.util.threedata.ThreeData;

public class AbilitiesLockedCondition extends Condition {

    public static final ThreeData<String[]> ABILITIES = new StringArrayThreeData("ability_ids").enableSetting("Contains the ids of abilities which MUST be LOCKED").setSyncType(EnumSync.NONE);

    public AbilitiesLockedCondition(Ability ability) {
        super(ConditionType.ABILITIES_LOCKED, ability);
    }

    @Override
    public void registerData() {
        super.registerData();
        this.register(ABILITIES, new String[]{"ability_1", "ability_2"});
    }

    @Override
    public ITextComponent createTitle() {
        return new TranslationTextComponent(Util.makeTranslationKey("ability.condition", this.type.getRegistryName()) + (this.dataManager.get(INVERT) ? ".not" : ""));
    }

    @Override
    public boolean test(LivingEntity entity) {
        for(String s : this.get(ABILITIES)) {
            Ability dependentAbility = AbilityHelper.getAbilityById(entity, s, ability.container);

            if(dependentAbility == null || dependentAbility == ability || dependentAbility.getConditionManager().isUnlocked()) {
                return false;
            }
        }
        return true;
    }
}
