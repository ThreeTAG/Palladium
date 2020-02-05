package net.threetag.threecore.data;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.conditions.ICondition;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class AddConditionRecipeWrapper {

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private List<ICondition> conditions = new ArrayList<>();
        private IFinishedRecipe recipe;

        public Builder addCondition(ICondition condition) {
            conditions.add(condition);
            return this;
        }

        public Builder setRecipe(Consumer<Consumer<IFinishedRecipe>> callable) {
            callable.accept(this::setRecipe);
            return this;
        }

        public Builder setRecipe(IFinishedRecipe recipe) {
            this.recipe = recipe;
            return this;
        }

        public void build(Consumer<IFinishedRecipe> consumer) {
            build(consumer, this.recipe.getID());
        }

        public void build(Consumer<IFinishedRecipe> consumer, String namespace, String path) {
            build(consumer, new ResourceLocation(namespace, path));
        }

        public void build(Consumer<IFinishedRecipe> consumer, ResourceLocation id) {
            if (this.recipe == null)
                throw new IllegalStateException("Invalid ConditionalRecipe builder, No recipe");

            consumer.accept(new Finished(id, conditions, recipe));
        }
    }

    private static class Finished implements IFinishedRecipe {

        private ResourceLocation id;
        private List<ICondition> conditions = new ArrayList<>();
        private IFinishedRecipe recipe;

        public Finished(ResourceLocation id, List<ICondition> conditions, IFinishedRecipe recipe) {
            this.id = id;
            this.conditions = conditions;
            this.recipe = recipe;
        }

        @Override
        public void serialize(JsonObject jsonObject) {
            if (this.conditions != null && !this.conditions.isEmpty()) {
                JsonArray conds = new JsonArray();
                for (ICondition c : conditions)
                    conds.add(CraftingHelper.serialize(c));
                jsonObject.add("conditions", conds);
            }

            JsonObject recipeJson = this.recipe.getRecipeJson();
            for (Map.Entry<String, JsonElement> entry : recipeJson.entrySet()) {
                jsonObject.add(entry.getKey(), entry.getValue());
            }
        }

        @Override
        public ResourceLocation getID() {
            return this.id;
        }

        @Override
        public IRecipeSerializer<?> getSerializer() {
            return this.recipe.getSerializer();
        }

        @Nullable
        @Override
        public JsonObject getAdvancementJson() {
            return this.recipe.getAdvancementJson();
        }

        @Nullable
        @Override
        public ResourceLocation getAdvancementID() {
            return this.recipe.getAdvancementID();
        }
    }

}
