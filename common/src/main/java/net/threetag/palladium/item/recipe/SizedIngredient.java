package net.threetag.palladium.item.recipe;

import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.threetag.palladium.util.json.GsonUtil;

public record SizedIngredient(Ingredient ingredient, int count) {

    public static SizedIngredient of(ItemLike item, int count) {
        return new SizedIngredient(Ingredient.of(item), count);
    }

    public SizedIngredient {
        if (count <= 0) {
            throw new IllegalArgumentException("Size must be positive");
        }
    }

    public boolean test(ItemStack stack) {
        return ingredient.test(stack) && stack.getCount() >= count;
    }

    public boolean test(Container container) {
        int found = 0;
        for (int i = 0; i < container.getContainerSize(); i++) {
            var stack = container.getItem(i);

            if (!stack.isEmpty() && this.ingredient.test(stack)) {
                found += stack.getCount();

                if (found >= this.count) {
                    return true;
                }
            }
        }
        return false;
    }

    public ItemStack take(Container container) {
        ItemStack resultStack = null;
        int left = this.count;

        for (int i = 0; i < container.getContainerSize(); i++) {
            var stack = container.getItem(i);

            if (!stack.isEmpty() && this.ingredient.test(stack)) {
                if (resultStack == null) {
                    resultStack = stack.copy();
                }

                int remove = Math.min(left, stack.getCount());
                stack.shrink(remove);
                left -= remove;

                if (stack.getCount() <= 0) {
                    container.setItem(i, ItemStack.EMPTY);
                } else {
                    container.setItem(i, stack);
                }

                if (left <= 0) {
                    resultStack.setCount(this.count);
                    return resultStack;
                }
            }
        }

        resultStack.setCount(this.count);
        return resultStack;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SizedIngredient other)) return false;
        return count == other.count && ingredient.equals(other.ingredient);
    }

    @Override
    public String toString() {
        return count + "x " + ingredient;
    }

    public ItemStack getDisplayItem(int timeInTicks) {
        var items = this.ingredient.getItems();
        return items[(timeInTicks / 20) % items.length];
    }

    public static SizedIngredient fromJson(JsonObject json, boolean canBeEmpty) {
        return new SizedIngredient(Ingredient.fromJson(json.get("ingredient"), canBeEmpty), GsonUtil.getAsIntMin(json, "count", 1, 1));
    }

    public JsonObject toJson() {
        var json = new JsonObject();
        json.add("ingredient", this.ingredient.toJson());
        json.addProperty("count", this.count);
        return json;
    }

    public static SizedIngredient fromNetwork(FriendlyByteBuf buf) {
        return new SizedIngredient(Ingredient.fromNetwork(buf), buf.readInt());
    }

    public void toNetwork(FriendlyByteBuf buf) {
        this.ingredient.toNetwork(buf);
        buf.writeInt(this.count);
    }

}
