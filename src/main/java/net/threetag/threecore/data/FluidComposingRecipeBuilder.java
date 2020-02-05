package net.threetag.threecore.data;

import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.ICriterionInstance;
import net.minecraft.advancements.IRequirementsStrategy;
import net.minecraft.advancements.criterion.RecipeUnlockedTrigger;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tags.Tag;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.fluids.FluidStack;
import net.threetag.threecore.item.recipe.TCBaseRecipeSerializers;
import net.threetag.threecore.fluid.FluidIngredient;
import net.threetag.threecore.util.TCFluidUtil;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class FluidComposingRecipeBuilder {

    private String group;
    private final List<Ingredient> ingredients = Lists.newArrayList();
    private FluidIngredient inputFluid;
    private FluidStack result;
    private int energy = 0;
    private List<ICondition> conditions = new ArrayList<>();
    private final Advancement.Builder advancementBuilder = Advancement.Builder.builder();

    public FluidComposingRecipeBuilder(FluidStack result) {
        this.result = result;
    }

    public FluidComposingRecipeBuilder addIngredient(Tag<Item> tag) {
        return this.addIngredient(Ingredient.fromTag(tag));
    }

    public FluidComposingRecipeBuilder addIngredient(IItemProvider itemProvider) {
        return this.addIngredient(itemProvider, 1);
    }

    public FluidComposingRecipeBuilder addIngredient(IItemProvider itemProvider, int count) {
        for (int i = 0; i < count; ++i) {
            this.addIngredient(Ingredient.fromItems(itemProvider));
        }

        return this;
    }

    public FluidComposingRecipeBuilder addIngredient(Ingredient ingredient) {
        return this.addIngredient(ingredient, 1);
    }

    public FluidComposingRecipeBuilder addIngredient(Ingredient ingredient, int count) {
        for (int i = 0; i < count; ++i) {
            this.ingredients.add(ingredient);
        }

        return this;
    }

    public FluidComposingRecipeBuilder setInputFluid(FluidIngredient fluid) {
        this.inputFluid = fluid;
        return this;
    }

    public FluidComposingRecipeBuilder setEnergy(int energy) {
        this.energy = energy;
        return this;
    }

    public FluidComposingRecipeBuilder addCondition(ICondition condition) {
        conditions.add(condition);
        return this;
    }

    public FluidComposingRecipeBuilder addCriterion(String key, ICriterionInstance criterionInstance) {
        this.advancementBuilder.withCriterion(key, criterionInstance);
        return this;
    }

    public FluidComposingRecipeBuilder setGroup(String group) {
        this.group = group;
        return this;
    }

    private void validate(ResourceLocation resourceLocation) {
        if (this.inputFluid == null) {
            throw new IllegalStateException("No input specified for recipe " + resourceLocation);
        } else if (this.ingredients.size() <= 0) {
            throw new IllegalStateException("Not enough ingredients for recipe " + resourceLocation);
        } else if (this.advancementBuilder.getCriteria().isEmpty()) {
            throw new IllegalStateException("No way of obtaining recipe " + resourceLocation);
        } else if (this.energy <= 0) {
            throw new IllegalStateException("Energy for " + resourceLocation + " must be greater than 0!");
        }
    }

    public void build(Consumer<IFinishedRecipe> consumer) {
        this.build(consumer, this.result.getFluid().getRegistryName());
    }

    public void build(Consumer<IFinishedRecipe> consumer, String name) {
        ResourceLocation resourceLocation = this.result.getFluid().getRegistryName();
        if ((new ResourceLocation(name)).equals(resourceLocation)) {
            throw new IllegalStateException("Fluid Composing Recipe " + name + " should remove its 'save' argument");
        } else {
            this.build(consumer, new ResourceLocation(name));
        }
    }

    public void build(Consumer<IFinishedRecipe> consumer, ResourceLocation name) {
        this.validate(name);
        this.advancementBuilder.withParentId(new ResourceLocation("recipes/root")).withCriterion("has_the_recipe", new RecipeUnlockedTrigger.Instance(name)).withRewards(net.minecraft.advancements.AdvancementRewards.Builder.recipe(name)).withRequirementsStrategy(IRequirementsStrategy.OR);
        consumer.accept(new FluidComposingRecipeBuilder.Result(name, group == null ? "" : group, ingredients, inputFluid, result, energy, conditions, advancementBuilder, new ResourceLocation(name.getNamespace(), "recipes/fluids/" + name.getPath())));
    }

    public class Result implements IFinishedRecipe {

        private final ResourceLocation id;
        private final String group;
        private final List<Ingredient> ingredients;
        private final FluidIngredient inputFluid;
        private final FluidStack result;
        private final int energy;
        private final List<ICondition> conditions;
        private final Advancement.Builder advancementBuilder;
        private final ResourceLocation advancementId;

        public Result(ResourceLocation id, String group, List<Ingredient> ingredients, FluidIngredient inputFluid, FluidStack result, int energy, List<ICondition> conditions, Advancement.Builder advancementBuilder, ResourceLocation advancementId) {
            this.id = id;
            this.group = group;
            this.ingredients = ingredients;
            this.inputFluid = inputFluid;
            this.result = result;
            this.energy = energy;
            this.conditions = conditions;
            this.advancementBuilder = advancementBuilder;
            this.advancementId = advancementId;
        }

        @Override
        public void serialize(JsonObject jsonObject) {
            if (!this.group.isEmpty()) {
                jsonObject.addProperty("group", this.group);
            }

            if (this.conditions != null && !this.conditions.isEmpty()) {
                JsonArray conds = new JsonArray();
                for (ICondition c : conditions)
                    conds.add(CraftingHelper.serialize(c));
                jsonObject.add("conditions", conds);
            }

            jsonObject.add("output", TCFluidUtil.serializeFluidStack(this.result));

            JsonArray array = new JsonArray();
            for (Ingredient ingredient : this.ingredients)
                array.add(ingredient.serialize());
            jsonObject.add("ingredients", array);

            if (this.inputFluid.getTag() == null) {
                if (this.inputFluid.getFluids().length == 1) {
                    jsonObject.add("fluid_input", TCFluidUtil.serializeFluidStack(this.inputFluid.getFluids()[0]));
                } else {
                    JsonArray fluids = new JsonArray();
                    for (FluidStack fluidStack : this.inputFluid.getFluids())
                        fluids.add(TCFluidUtil.serializeFluidStack(fluidStack));
                    jsonObject.add("fluid_input", fluids);
                }
            } else {
                JsonObject fluidInput = new JsonObject();
                fluidInput.addProperty("tag", this.inputFluid.getTag().getId().toString());
                fluidInput.addProperty("amount", this.inputFluid.getFluids()[0].getAmount());
                jsonObject.add("fluid_input", fluidInput);
            }

            jsonObject.addProperty("energy", this.energy);
        }

        @Override
        public ResourceLocation getID() {
            return this.id;
        }

        @Override
        public IRecipeSerializer<?> getSerializer() {
            return TCBaseRecipeSerializers.FLUID_COMPOSING;
        }

        @Nullable
        @Override
        public JsonObject getAdvancementJson() {
            return this.advancementBuilder.serialize();
        }

        @Nullable
        @Override
        public ResourceLocation getAdvancementID() {
            return this.advancementId;
        }
    }
}
