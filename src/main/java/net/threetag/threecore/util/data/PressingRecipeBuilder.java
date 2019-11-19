package net.threetag.threecore.util.data;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.ICriterionInstance;
import net.minecraft.advancements.IRequirementsStrategy;
import net.minecraft.advancements.criterion.RecipeUnlockedTrigger;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tags.Tag;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.threetag.threecore.base.recipe.TCBaseRecipeSerializers;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class PressingRecipeBuilder {

    private String group;
    private Ingredient input;
    private Ingredient cast;
    private final ExtRecipeOutput output;
    private float experience = 0F;
    private int energy = 0;
    private List<ICondition> conditions = new ArrayList<>();
    private final Advancement.Builder advancementBuilder = Advancement.Builder.builder();

    public PressingRecipeBuilder(ItemStack output) {
        this.output = new ExtRecipeOutput(output);
    }

    public PressingRecipeBuilder(IItemProvider itemProvider) {
        this(itemProvider, 1);
    }

    public PressingRecipeBuilder(IItemProvider itemProvider, int amount) {
        this(new ItemStack(itemProvider, amount));
    }

    public PressingRecipeBuilder(Tag<Item> tag) {
        this(tag, 1);
    }

    public PressingRecipeBuilder(Tag<Item> tag, int amount) {
        this.output = new ExtRecipeOutput(tag, amount);
    }

    public PressingRecipeBuilder setIngredient(Ingredient ingredient) {
        this.input = ingredient;
        return this;
    }

    public PressingRecipeBuilder setIngredient(IItemProvider... itemProviders) {
        this.input = Ingredient.fromItems(itemProviders);
        return this;
    }

    public PressingRecipeBuilder setIngredient(ItemStack... stacks) {
        this.input = Ingredient.fromStacks(stacks);
        return this;
    }

    public PressingRecipeBuilder setIngredient(Tag<Item> tag) {
        this.input = Ingredient.fromTag(tag);
        return this;
    }

    public PressingRecipeBuilder setCast(Ingredient ingredient) {
        this.cast = ingredient;
        return this;
    }

    public PressingRecipeBuilder setCast(IItemProvider... itemProviders) {
        this.cast = Ingredient.fromItems(itemProviders);
        return this;
    }

    public PressingRecipeBuilder setCast(ItemStack... stacks) {
        this.cast = Ingredient.fromStacks(stacks);
        return this;
    }

    public PressingRecipeBuilder setCast(Tag<Item> tag) {
        this.cast = Ingredient.fromTag(tag);
        return this;
    }

    public PressingRecipeBuilder setEnergy(int energy) {
        this.energy = energy;
        return this;
    }

    public PressingRecipeBuilder setExperience(float experience) {
        this.experience = experience;
        return this;
    }

    public PressingRecipeBuilder setGroup(String group) {
        this.group = group;
        return this;
    }

    public PressingRecipeBuilder addCondition(ICondition condition) {
        conditions.add(condition);
        return this;
    }

    public PressingRecipeBuilder addCriterion(String key, ICriterionInstance criterionInstance) {
        this.advancementBuilder.withCriterion(key, criterionInstance);
        return this;
    }

    private void validate(ResourceLocation resourceLocation) {
        if (this.input == null) {
            throw new IllegalStateException("No input specified for recipe " + resourceLocation);
        } else if (this.advancementBuilder.getCriteria().isEmpty()) {
            throw new IllegalStateException("No way of obtaining recipe " + resourceLocation);
        } else if(this.energy <= 0) {
            throw new IllegalStateException("Energy for " + resourceLocation + " must be greater than 0!");
        }
    }

    public void build(Consumer<IFinishedRecipe> consumer) {
        this.build(consumer, this.output.getId());
    }

    public void build(Consumer<IFinishedRecipe> consumer, String name) {
        ResourceLocation resourceLocation = this.output.getId();
        if ((new ResourceLocation(name)).equals(resourceLocation)) {
            throw new IllegalStateException("Pressing Recipe " + name + " should remove its 'save' argument");
        } else {
            this.build(consumer, new ResourceLocation(name));
        }
    }

    public void build(Consumer<IFinishedRecipe> consumer, ResourceLocation name) {
        this.validate(name);
        this.advancementBuilder.withParentId(new ResourceLocation("recipes/root")).withCriterion("has_the_recipe", new RecipeUnlockedTrigger.Instance(name)).withRewards(net.minecraft.advancements.AdvancementRewards.Builder.recipe(name)).withRequirementsStrategy(IRequirementsStrategy.OR);
        consumer.accept(new Result(name, group == null ? "" : group, input, cast, output, experience, energy, conditions, advancementBuilder, new ResourceLocation(name.getNamespace(), "recipes/" + this.output.getGroup() + "/" + name.getPath())));
    }

    public class Result implements IFinishedRecipe {

        private final ResourceLocation id;
        private final String group;
        private final Ingredient input;
        private final Ingredient cast;
        private final ExtRecipeOutput output;
        private final float experience;
        private final int energy;
        private final List<ICondition> conditions;
        private final Advancement.Builder advancementBuilder;
        private final ResourceLocation advancementId;

        public Result(ResourceLocation id, String group, Ingredient input, Ingredient cast, ExtRecipeOutput output, float experience, int energy, List<ICondition> conditions, Advancement.Builder advancementBuilder, ResourceLocation advancementId) {
            this.id = id;
            this.group = group;
            this.input = input;
            this.cast = cast;
            this.output = output;
            this.experience = experience;
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

            jsonObject.add("result", this.output.serialize());

            jsonObject.add("ingredient", this.input.serialize());

            if (this.cast != null) {
                jsonObject.add("cast", this.cast.serialize());
            }

            jsonObject.addProperty("experience", this.experience);

            jsonObject.addProperty("energy", this.energy);
        }

        @Override
        public ResourceLocation getID() {
            return this.id;
        }

        @Override
        public IRecipeSerializer<?> getSerializer() {
            return TCBaseRecipeSerializers.PRESSING;
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
