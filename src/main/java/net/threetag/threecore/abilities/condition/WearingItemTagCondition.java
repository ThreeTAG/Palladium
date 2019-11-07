package net.threetag.threecore.abilities.condition;

import com.google.common.collect.Maps;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tags.Tag;
import net.minecraft.util.Util;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.Tags;
import net.threetag.threecore.abilities.Ability;
import net.threetag.threecore.util.threedata.BooleanThreeData;
import net.threetag.threecore.util.threedata.EnumSync;
import net.threetag.threecore.util.threedata.ItemTagThreeData;
import net.threetag.threecore.util.threedata.ThreeData;

import java.util.Map;

public class WearingItemTagCondition extends Condition {

    public static final ThreeData<Tag<Item>> ITEM_TAG = new ItemTagThreeData("item_tag").setSyncType(EnumSync.SELF).enableSetting("Determines the item tag the items must have");
    public static final Map<EquipmentSlotType, ThreeData<Boolean>> SLOT_DATA = Maps.newHashMap();

    static {
        for (EquipmentSlotType slot : EquipmentSlotType.values()) {
            SLOT_DATA.put(slot, new BooleanThreeData(slot.getName().toLowerCase()).setSyncType(EnumSync.SELF).enableSetting("Determines if the item in the " + slot.getName() + " slot must have the specified tag"));
        }
    }

    public WearingItemTagCondition(Ability ability) {
        super(ConditionType.WEARING_ITEM_TAG, ability);
    }

    @Override
    public ITextComponent createTitle() {
        return new TranslationTextComponent(Util.makeTranslationKey("ability.condition", this.type.getRegistryName()) + (this.dataManager.get(INVERT) ? ".not" : ""), this.dataManager.get(ITEM_TAG).getId().toString());
    }

    @Override
    public void registerData() {
        super.registerData();
        this.dataManager.register(ITEM_TAG, Tags.Items.INGOTS_IRON);
        for (EquipmentSlotType slot : EquipmentSlotType.values()) {
            this.dataManager.register(SLOT_DATA.get(slot), slot.getSlotType() == EquipmentSlotType.Group.ARMOR);
        }
    }

    @Override
    public boolean test(LivingEntity entity) {
        Tag<Item> tag = this.dataManager.get(ITEM_TAG);
        for (EquipmentSlotType slot : EquipmentSlotType.values()) {
            ItemStack stack = entity.getItemStackFromSlot(slot);
            if (this.dataManager.get(SLOT_DATA.get(slot)) && (stack.isEmpty() || !stack.getItem().isIn(tag))) {
                return false;
            }
        }
        return true;
    }
}
