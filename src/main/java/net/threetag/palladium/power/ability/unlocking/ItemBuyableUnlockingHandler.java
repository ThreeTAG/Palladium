package net.threetag.palladium.power.ability.unlocking;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.chat.Component;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.threetag.palladium.icon.IngredientIcon;
import net.threetag.palladium.logic.condition.Condition;
import net.threetag.palladium.logic.condition.TrueCondition;

public class ItemBuyableUnlockingHandler extends BuyableUnlockingHandler {

    public static final MapCodec<ItemBuyableUnlockingHandler> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Ingredient.CODEC.fieldOf("ingredient").forGetter(h -> h.ingredient),
            ExtraCodecs.POSITIVE_INT.optionalFieldOf("amount", 1).forGetter(h -> h.amount),
            Condition.CODEC.optionalFieldOf("conditions", TrueCondition.INSTANCE).forGetter(h -> h.condition)
    ).apply(instance, ItemBuyableUnlockingHandler::new));

    private final Ingredient ingredient;
    private final int amount;

    public ItemBuyableUnlockingHandler(Ingredient ingredient, int amount, Condition conditions) {
        super(conditions);
        this.ingredient = ingredient;
        this.amount = amount;
    }

    @Override
    public boolean hasEnoughCurrency(LivingEntity entity) {
        if (entity instanceof Player player) {
            int found = 0;
            for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
                var stack = player.getInventory().getItem(i);

                if (!stack.isEmpty() && this.ingredient.test(stack)) {
                    found += stack.getCount();
                }
            }

            return found >= this.amount;
        }

        return false;
    }

    @Override
    public void consumeCurrency(LivingEntity entity) {
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
    }

    @Override
    public Display getDisplay() {
        var stacks = this.ingredient.items().toList();
        var component = Component.empty();

        for (int i = 0; i < stacks.size(); i++) {
            component.append(stacks.get(i).value().getName(stacks.get(i).value().getDefaultInstance()));

            if (i < stacks.size() - 1) {
                if (i == stacks.size() - 2) {
                    component.append(" ").append(Component.translatable("gui.palladium.powers.buy_ability.or")).append(" ");
                } else {
                    component.append(", ");
                }
            }
        }

        return new Display(new IngredientIcon(this.ingredient), this.amount, component);
    }

    @Override
    public UnlockingHandlerSerializer<?> getSerializer() {
        return UnlockingHandlerSerializers.ITEM_BUYABLE.get();
    }

    public static class Serializer extends UnlockingHandlerSerializer<ItemBuyableUnlockingHandler> {

        @Override
        public MapCodec<ItemBuyableUnlockingHandler> codec() {
            return CODEC;
        }

    }
}
