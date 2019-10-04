package net.threetag.threecore.abilities.condition;

import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.util.Util;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.threetag.threecore.abilities.Ability;
import net.threetag.threecore.abilities.capability.ItemAbilityContainer;
import net.threetag.threecore.util.threedata.EquipmentSlotThreeData;
import net.threetag.threecore.util.threedata.ThreeData;

public class EquipmentSlotCondition extends Condition {

    public static ThreeData<EquipmentSlotType> EQUIPMENT_SLOT = new EquipmentSlotThreeData("slot").enableSetting("slot", "Requires the ability to be in an item, and needs that item in the specified slot");

    public EquipmentSlotCondition(Ability ability) {
        super(ConditionType.EQUIPMENT_SLOT, ability);
    }

    @Override
    public void registerData() {
        super.registerData();
        this.dataManager.register(EQUIPMENT_SLOT, EquipmentSlotType.MAINHAND);
    }

    @Override
    public ITextComponent createTitle() {
        ITextComponent slotName = new TranslationTextComponent(Util.makeTranslationKey("ability.condition", this.type.getRegistryName()) + "." + this.dataManager.get(EQUIPMENT_SLOT).getName());
        return new TranslationTextComponent(Util.makeTranslationKey("ability.condition", this.type.getRegistryName()) + (this.dataManager.get(INVERT) ? ".not" : ""), slotName);
    }

    @Override
    public boolean test(LivingEntity entity) {
        if (!(this.ability.container instanceof ItemAbilityContainer))
            return false;
        return entity.getItemStackFromSlot(this.dataManager.get(EQUIPMENT_SLOT)) == ((ItemAbilityContainer) this.ability.container).stack;
    }

}
