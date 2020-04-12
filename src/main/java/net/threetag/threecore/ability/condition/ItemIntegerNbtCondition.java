package net.threetag.threecore.ability.condition;

import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Util;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.threetag.threecore.ability.Ability;
import net.threetag.threecore.util.threedata.EquipmentSlotThreeData;
import net.threetag.threecore.util.threedata.IntegerThreeData;
import net.threetag.threecore.util.threedata.StringThreeData;
import net.threetag.threecore.util.threedata.ThreeData;

public class ItemIntegerNbtCondition extends Condition {

    public static ThreeData<EquipmentSlotType> EQUIPMENT_SLOT = new EquipmentSlotThreeData("slot").enableSetting("slot", "The slot which will be checked on");
    public static ThreeData<String> NBT_TAG = new StringThreeData("nbt_tag").enableSetting("The nbt tag which will be checked on");
    public static ThreeData<Integer> MIN = new IntegerThreeData("min").enableSetting("The minimum value the nbt tag has to be");
    public static ThreeData<Integer> MAX = new IntegerThreeData("max").enableSetting("The maximum value the nbt tag has to be");

    public ItemIntegerNbtCondition(Ability ability) {
        super(ConditionType.ITEM_INTEGER_NBT, ability);
    }

    @Override
    public void registerData() {
        super.registerData();
        this.register(EQUIPMENT_SLOT, EquipmentSlotType.HEAD);
        this.register(NBT_TAG, "Opening");
        this.register(MIN, 0);
        this.register(MAX, 0);
    }

    @Override
    public ITextComponent createTitle() {
        return new TranslationTextComponent(Util.makeTranslationKey("ability.condition", this.type.getRegistryName()), this.get(EQUIPMENT_SLOT), this.get(NBT_TAG));
    }

    @Override
    public boolean test(LivingEntity entity) {
        ItemStack stack = entity.getItemStackFromSlot(this.get(EQUIPMENT_SLOT));

        if (stack.isEmpty())
            return false;

        int i = stack.getOrCreateTag().getInt(this.get(NBT_TAG));
        return i >= this.get(MIN) && i <= this.get(MAX);
    }
}
