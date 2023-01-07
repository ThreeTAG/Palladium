package net.threetag.palladium.condition;

import com.google.gson.JsonObject;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.threetag.palladium.power.ability.AbilityConfiguration;
import net.threetag.palladium.util.icon.ItemIcon;
import net.threetag.palladium.util.property.IngredientProperty;
import net.threetag.palladium.util.property.IntegerProperty;
import net.threetag.palladium.util.property.PalladiumProperty;

public class ItemBuyableCondition extends BuyableCondition {

    private final Ingredient ingredient;
    private final int amount;

    public ItemBuyableCondition(Ingredient ingredient, int amount) {
        this.ingredient = ingredient;
        this.amount = amount;
    }

    @Override
    public AbilityConfiguration.UnlockData createData() {
        // TODO new ingredient icon
        var stack = this.ingredient.getItems()[0];
        return new AbilityConfiguration.UnlockData(new ItemIcon(stack), this.amount, stack.getItem().getName(stack));
    }

    @Override
    public boolean isAvailable(LivingEntity entity) {
        int found = 0;

        if (entity instanceof Player player) {
            for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
                ItemStack stack = player.getInventory().getItem(i);

                if (this.ingredient.test(stack)) {
                    found += stack.getCount();
                }
            }
        }

        return found >= this.amount;
    }

    @Override
    public boolean takeFromEntity(LivingEntity entity) {
        int remove = this.amount;

        if (entity instanceof Player player) {
            for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
                ItemStack stack = player.getInventory().getItem(i);

                if (this.ingredient.test(stack)) {
                    int willRemove = Math.min(remove, stack.getCount());
                    player.getInventory().removeItem(i, willRemove);
                    remove -= willRemove;

                    if (remove <= 0) {
                        break;
                    }
                }
            }
        }

        return remove <= 0;
    }

    @Override
    public ConditionSerializer getSerializer() {
        return ConditionSerializers.ITEM_BUYABLE.get();
    }

    public static class Serializer extends ConditionSerializer {

        public static final PalladiumProperty<Ingredient> INGREDIENT = new IngredientProperty("ingredient").configurable("Ingredient predicate for the item");
        public static final PalladiumProperty<Integer> AMOUNT = new IntegerProperty("amount").configurable("Amount of items that the player needs to spend");

        public Serializer() {
            this.withProperty(INGREDIENT, Ingredient.of(Items.IRON_INGOT));
            this.withProperty(AMOUNT, 3);
        }

        @Override
        public ConditionContextType getContextType() {
            return ConditionContextType.ABILITIES;
        }

        @Override
        public Condition make(JsonObject json) {
            return new ItemBuyableCondition(getProperty(json, INGREDIENT), getProperty(json, AMOUNT));
        }

    }
}
