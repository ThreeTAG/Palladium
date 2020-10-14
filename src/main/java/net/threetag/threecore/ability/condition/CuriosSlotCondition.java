package net.threetag.threecore.ability.condition;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.threetag.threecore.ability.Ability;
import net.threetag.threecore.capability.ItemAbilityContainer;
import net.threetag.threecore.compat.curios.DefaultCuriosHandler;
import net.threetag.threecore.util.threedata.StringArrayThreeData;
import net.threetag.threecore.util.threedata.ThreeData;

public class CuriosSlotCondition extends Condition {

    public static final ThreeData<String[]> SLOTS = new StringArrayThreeData("slots").enableSetting("Requires the ability to be in an item, and needs that item to be in one of those specified slots. Leave empty for all kind of slots -> https://github.com/TheIllusiveC4/Curios/wiki/Frequently-Used-Slots");

    public CuriosSlotCondition(Ability ability) {
        super(ConditionType.CURIOS_SLOT, ability);
    }

    @Override
    public void registerData() {
        super.registerData();
        this.register(SLOTS, new String[]{"back"});
    }

    @Override
    public boolean test(LivingEntity entity) {
        if (!(this.ability.container instanceof ItemAbilityContainer))
            return false;

        for (String identifier : this.get(SLOTS)) {
            for (ItemStack stack : DefaultCuriosHandler.INSTANCE.getItemsInSlot(entity, identifier)) {
                if (stack == ((ItemAbilityContainer) this.ability.container).stack) {
                    return true;
                }
            }
        }

        return false;
    }
}
