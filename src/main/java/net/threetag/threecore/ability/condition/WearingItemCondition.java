package net.threetag.threecore.ability.condition;

import com.google.common.collect.Maps;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.crafting.Ingredient;
import net.threetag.threecore.ability.Ability;
import net.threetag.threecore.util.threedata.EnumSync;
import net.threetag.threecore.util.threedata.IngredientThreeData;
import net.threetag.threecore.util.threedata.ThreeData;

import java.util.Map;

public class WearingItemCondition extends Condition {

    public static final Map<EquipmentSlotType, ThreeData<Ingredient>> SLOT_DATA = Maps.newHashMap();

    static {
        for (EquipmentSlotType slot : EquipmentSlotType.values()) {
            SLOT_DATA.put(slot, new IngredientThreeData(slot.getName().toLowerCase()).setSyncType(EnumSync.SELF).enableSetting("Specifies the ingredient for the item in the " + slot.getName() + " slot"));
        }
    }

    public WearingItemCondition(Ability ability) {
        super(ConditionType.WEARING_ITEM, ability);
    }

    @Override
    public void registerData() {
        super.registerData();
        for (EquipmentSlotType slot : EquipmentSlotType.values()) {
            this.dataManager.register(SLOT_DATA.get(slot), Ingredient.EMPTY);
        }
    }

    @Override
    public boolean test(LivingEntity entity) {
        for (EquipmentSlotType slot : EquipmentSlotType.values()) {
            Ingredient ingredient = this.dataManager.get(SLOT_DATA.get(slot));
            if (ingredient != Ingredient.EMPTY && !ingredient.test(entity.getItemStackFromSlot(slot))) {
                return false;
            }
        }
        return true;
    }
}
