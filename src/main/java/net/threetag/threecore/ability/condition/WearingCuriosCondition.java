package net.threetag.threecore.ability.condition;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.threetag.threecore.ability.Ability;
import net.threetag.threecore.compat.curios.DefaultCuriosHandler;
import net.threetag.threecore.util.threedata.EnumSync;
import net.threetag.threecore.util.threedata.IngredientThreeData;
import net.threetag.threecore.util.threedata.StringArrayThreeData;
import net.threetag.threecore.util.threedata.ThreeData;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;

public class WearingCuriosCondition extends Condition {

    public static final ThreeData<Ingredient> ITEM = new IngredientThreeData("item").enableSetting("Ingredient for testing the item in the curios slot").setSyncType(EnumSync.NONE);
    public static final ThreeData<String[]> SLOTS = new StringArrayThreeData("slots").enableSetting("Slots the item can be in. Leave empty for all kind of slots -> https://github.com/TheIllusiveC4/Curios/wiki/Frequently-Used-Slots");

    public WearingCuriosCondition(Ability ability) {
        super(ConditionType.WEARING_CURIOS, ability);
    }

    @Override
    public void registerData() {
        super.registerData();
        this.register(ITEM, Ingredient.fromItems(Items.APPLE));
        this.register(SLOTS, new String[]{"back", "belt"});
    }

    @Override
    public boolean test(LivingEntity entity) {
        AtomicBoolean result = new AtomicBoolean(false);

        DefaultCuriosHandler.INSTANCE.getCurioEquipped(this.get(ITEM), entity).ifPresent(triple -> {
            if (this.get(SLOTS).length == 0 || (this.get(SLOTS).length == 1 && this.get(SLOTS)[0].isEmpty())) {
                result.set(true);
            } else if (Arrays.asList(this.get(SLOTS)).contains(triple.getLeft())) {
                result.set(true);
            }
        });

        return result.get();
    }
}
