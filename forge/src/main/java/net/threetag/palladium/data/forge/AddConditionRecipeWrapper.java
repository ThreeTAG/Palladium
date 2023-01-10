package net.threetag.palladium.data.forge;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.conditions.ICondition;

import org.jetbrains.annotations.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class AddConditionRecipeWrapper {

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private final List<ICondition> conditions = new ArrayList<>();
        private FinishedRecipe recipe;

        public Builder addCondition(ICondition condition) {
            conditions.add(condition);
            return this;
        }

        public Builder setRecipe(Consumer<Consumer<FinishedRecipe>> callable) {
            callable.accept(this::setRecipe);
            return this;
        }

        public Builder setRecipe(FinishedRecipe recipe) {
            this.recipe = recipe;
            return this;
        }

        public void build(Consumer<FinishedRecipe> consumer) {
            build(consumer, this.recipe.getId());
        }

        public void build(Consumer<FinishedRecipe> consumer, String namespace, String path) {
            build(consumer, new ResourceLocation(namespace, path));
        }

        public void build(Consumer<FinishedRecipe> consumer, ResourceLocation id) {
            if (this.recipe == null)
                throw new IllegalStateException("Invalid ConditionalRecipe builder, No recipe");

            consumer.accept(new Finished(id, conditions, recipe));
        }
    }

    private static class Finished implements FinishedRecipe {

        private final ResourceLocation id;
        private List<ICondition> conditions = new ArrayList<>();
        private final FinishedRecipe recipe;

        public Finished(ResourceLocation id, List<ICondition> conditions, FinishedRecipe recipe) {
            this.id = id;
            this.conditions = conditions;
            this.recipe = recipe;
        }

        @Override
        public void serializeRecipeData(JsonObject jsonObject) {
            if (this.conditions != null && !this.conditions.isEmpty()) {
                JsonArray conds = new JsonArray();
                for (ICondition c : conditions)
                    conds.add(CraftingHelper.serialize(c));
                jsonObject.add("conditions", conds);
            }

            JsonObject recipeJson = this.recipe.serializeRecipe();
            for (Map.Entry<String, JsonElement> entry : recipeJson.entrySet()) {
                jsonObject.add(entry.getKey(), entry.getValue());
            }
        }

        @Override
        public ResourceLocation getId() {
            return this.id;
        }

        @Override
        public RecipeSerializer<?> getType() {
            return this.recipe.getType();
        }

        @Nullable
        @Override
        public JsonObject serializeAdvancement() {
            return this.recipe.serializeAdvancement();
        }

        @Nullable
        @Override
        public ResourceLocation getAdvancementId() {
            return this.recipe.getAdvancementId();
        }
    }

}
