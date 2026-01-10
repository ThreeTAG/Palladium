package net.threetag.palladium.util;

import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.crafting.SizedIngredient;

public class RecipeUtil {

    public static boolean checkSizedIngredientInContainer(SizedIngredient sizedIngredient, Container container) {
        int found = 0;
        for (int i = 0; i < container.getContainerSize(); i++) {
            var stack = container.getItem(i);

            if (!stack.isEmpty() && sizedIngredient.ingredient().test(stack)) {
                found += stack.getCount();

                if (found >= sizedIngredient.count()) {
                    return true;
                }
            }
        }
        return false;
    }

    public static void consumeSizedIngredientFromContainer(SizedIngredient sizedIngredient, Container container) {
        int left = sizedIngredient.count();

        for (int i = 0; i < container.getContainerSize(); i++) {
            var stack = container.getItem(i);

            if (!stack.isEmpty() && sizedIngredient.ingredient().test(stack)) {
                int remove = Math.min(left, stack.getCount());
                stack.shrink(remove);
                left -= remove;

                if (stack.getCount() <= 0) {
                    container.setItem(i, ItemStack.EMPTY);
                } else {
                    container.setItem(i, stack);
                }

                if (left <= 0) {
                    return;
                }
            }
        }
    }

}
