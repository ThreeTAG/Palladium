package net.threetag.palladium.item.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.threetag.palladium.util.json.GsonUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemTailoringRecipe extends TailoringRecipe {

    private final Component title;

    public ItemTailoringRecipe(ResourceLocation id, Map<EquipmentSlot, ItemStack> results,
                               List<SizedIngredient> ingredients, Ingredient toolIngredient, boolean consumeTool, Component title,
                               ResourceLocation toolIcon, ResourceLocation categoryId, boolean requiresUnlocking) {
        super(id, results, ingredients, toolIngredient, consumeTool, toolIcon, categoryId, requiresUnlocking);
        this.title = title;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return PalladiumRecipeSerializers.ITEM_TAILORING.get();
    }

    @Override
    public Component getTitle() {
        return this.title;
    }

    public static class Serializer implements RecipeSerializer<ItemTailoringRecipe> {

        @Override
        public ItemTailoringRecipe fromJson(ResourceLocation recipeId, JsonObject serializedRecipe) {
            var resultsJson = GsonHelper.getAsJsonObject(serializedRecipe, "results");
            Map<EquipmentSlot, ItemStack> results = new HashMap<>();

            for (EquipmentSlot slot : EquipmentSlot.values()) {
                if (slot.isArmor()) {
                    var stack = GsonUtil.getAsItemStack(resultsJson, slot.getName(), ItemStack.EMPTY);

                    if (!stack.isEmpty()) {
                        results.put(slot, stack);
                    }
                }
            }

            if (results.isEmpty()) {
                throw new JsonParseException("Tailoring result needs at least one item");
            }

            List<SizedIngredient> ingredients = itemsFromJson(GsonHelper.getAsJsonArray(serializedRecipe, "ingredients"));

            if (ingredients.isEmpty()) {
                throw new JsonParseException("No ingredients for shapeless recipe");
            }

            var toolIngredient = Ingredient.fromJson(serializedRecipe.get("tool"));

            if (toolIngredient.isEmpty()) {
                throw new JsonParseException("Valid tool ingredient required");
            }

            if (!serializedRecipe.has("title")) {
                throw new JsonParseException("Missing 'title'");
            }

            var title = Component.Serializer.fromJson(serializedRecipe.get("title"));

            return new ItemTailoringRecipe(
                    recipeId,
                    results,
                    ingredients,
                    toolIngredient,
                    GsonHelper.getAsBoolean(serializedRecipe, "consume_tool", false),
                    title,
                    GsonUtil.getAsResourceLocation(serializedRecipe, "tool_icon", null),
                    GsonUtil.getAsResourceLocation(serializedRecipe, "category", null),
                    GsonHelper.getAsBoolean(serializedRecipe, "requires_unlocking", true)
            );
        }

        private static List<SizedIngredient> itemsFromJson(JsonArray ingredientArray) {
            List<SizedIngredient> list = new ArrayList<>();

            for (int i = 0; i < ingredientArray.size(); ++i) {
                SizedIngredient ingredient = SizedIngredient.fromJson(GsonHelper.convertToJsonObject(ingredientArray.get(i), "ingredients[].$"), false);
                if (!ingredient.ingredient().isEmpty()) {
                    list.add(ingredient);
                }
            }

            return list;
        }

        @Override
        public ItemTailoringRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
            Map<EquipmentSlot, ItemStack> results = buffer.readMap(buf -> EquipmentSlot.byName(buf.readUtf()), FriendlyByteBuf::readItem);
            List<SizedIngredient> ingredients = buffer.readList(SizedIngredient::fromNetwork);
            return new ItemTailoringRecipe(
                    recipeId,
                    results,
                    ingredients,
                    Ingredient.fromNetwork(buffer),
                    buffer.readBoolean(),
                    buffer.readComponent(),
                    buffer.readNullable(FriendlyByteBuf::readResourceLocation),
                    buffer.readNullable(FriendlyByteBuf::readResourceLocation),
                    buffer.readBoolean()
            );
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, ItemTailoringRecipe recipe) {
            buffer.writeMap(recipe.results, (buf, slot) -> buf.writeUtf(slot.getName()), FriendlyByteBuf::writeItem);
            buffer.writeCollection(recipe.ingredients, (buf, ingredient) -> ingredient.toNetwork(buf));
            recipe.toolIngredient.toNetwork(buffer);
            buffer.writeBoolean(recipe.consumeTool);
            buffer.writeComponent(recipe.title);
            buffer.writeNullable(recipe.toolIcon, FriendlyByteBuf::writeResourceLocation);
            buffer.writeNullable(recipe.categoryId, FriendlyByteBuf::writeResourceLocation);
            buffer.writeBoolean(recipe.requiresUnlocking);
        }
    }
}
