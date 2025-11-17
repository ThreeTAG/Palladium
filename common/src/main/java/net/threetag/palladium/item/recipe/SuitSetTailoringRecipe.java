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
import net.threetag.palladium.item.SuitSet;
import net.threetag.palladium.util.json.GsonUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SuitSetTailoringRecipe extends TailoringRecipe {

    private final SuitSet suitSet;

    public SuitSetTailoringRecipe(ResourceLocation id, SuitSet suitSet, List<SizedIngredient> ingredients,
                                  Ingredient toolIngredient, ResourceLocation toolIcon, ResourceLocation categoryId,
                                  boolean requiresUnlocking) {
        super(id, buildResults(suitSet), ingredients, toolIngredient, toolIcon, categoryId, requiresUnlocking);
        this.suitSet = suitSet;
    }

    private static Map<EquipmentSlot, ItemStack> buildResults(SuitSet suitSet) {
        Map<EquipmentSlot, ItemStack> map = new HashMap<>();
        map.put(EquipmentSlot.HEAD, suitSet.getHelmet() != null ? suitSet.getHelmet().getDefaultInstance() : ItemStack.EMPTY);
        map.put(EquipmentSlot.CHEST, suitSet.getChestplate() != null ? suitSet.getChestplate().getDefaultInstance() : ItemStack.EMPTY);
        map.put(EquipmentSlot.LEGS, suitSet.getLeggings() != null ? suitSet.getLeggings().getDefaultInstance() : ItemStack.EMPTY);
        map.put(EquipmentSlot.FEET, suitSet.getBoots() != null ? suitSet.getBoots().getDefaultInstance() : ItemStack.EMPTY);
        return map;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return PalladiumRecipeSerializers.SUIT_SET_TAILORING.get();
    }

    @Override
    public Component getTitle() {
        return Component.translatable(this.suitSet.getDescriptionId());
    }

    public static class Serializer implements RecipeSerializer<SuitSetTailoringRecipe> {

        @Override
        public SuitSetTailoringRecipe fromJson(ResourceLocation recipeId, JsonObject serializedRecipe) {
            var suitSetId = GsonUtil.getAsResourceLocation(serializedRecipe, "suit_set");

            if (!SuitSet.REGISTRY.containsKey(suitSetId)) {
                throw new JsonParseException("Unknown suit set " + suitSetId);
            }

            var suitSet = SuitSet.REGISTRY.get(suitSetId);

            List<SizedIngredient> ingredients = itemsFromJson(GsonHelper.getAsJsonArray(serializedRecipe, "ingredients"));

            if (ingredients.isEmpty()) {
                throw new JsonParseException("No ingredients for shapeless recipe");
            }

            var toolIngredient = Ingredient.fromJson(serializedRecipe.get("tool"));

            if (toolIngredient.isEmpty()) {
                throw new JsonParseException("Valid tool ingredient required");
            }

            return new SuitSetTailoringRecipe(
                    recipeId,
                    suitSet,
                    ingredients,
                    toolIngredient,
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
        public SuitSetTailoringRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
            var suitSet = SuitSet.REGISTRY.get(buffer.readResourceLocation());
            List<SizedIngredient> ingredients = buffer.readList(SizedIngredient::fromNetwork);
            return new SuitSetTailoringRecipe(
                    recipeId,
                    suitSet,
                    ingredients,
                    Ingredient.fromNetwork(buffer),
                    buffer.readNullable(FriendlyByteBuf::readResourceLocation),
                    buffer.readNullable(FriendlyByteBuf::readResourceLocation),
                    buffer.readBoolean()
            );
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, SuitSetTailoringRecipe recipe) {
            buffer.writeResourceLocation(SuitSet.REGISTRY.getKey(recipe.suitSet));
            buffer.writeCollection(recipe.ingredients, (buf, ingredient) -> ingredient.toNetwork(buf));
            recipe.toolIngredient.toNetwork(buffer);
            buffer.writeNullable(recipe.toolIcon, FriendlyByteBuf::writeResourceLocation);
            buffer.writeNullable(recipe.categoryId, FriendlyByteBuf::writeResourceLocation);
            buffer.writeBoolean(recipe.requiresUnlocking);
        }
    }
}
