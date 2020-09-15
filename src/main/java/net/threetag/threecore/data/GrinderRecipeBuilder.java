package net.threetag.threecore.data;

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
import net.minecraft.tags.ITag;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.registries.ForgeRegistries;
import net.threetag.threecore.item.recipe.TCRecipeSerializers;
import net.threetag.threecore.util.TCJsonUtil;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class GrinderRecipeBuilder {

    private String group;
    private Ingredient input;
    private final ItemStack output;
    private ItemStack byproduct;
    private float byproductChance = 1F;
    private float experience = 0F;
    private int energy = 0;
    private List<ICondition> conditions = new ArrayList<>();
    private final Advancement.Builder advancementBuilder = Advancement.Builder.builder();

    public GrinderRecipeBuilder(ItemStack output) {
        this.output = output;
    }

    public GrinderRecipeBuilder(IItemProvider itemProvider) {
        this(itemProvider, 1);
    }

    public GrinderRecipeBuilder(IItemProvider itemProvider, int amount) {
        this(new ItemStack(itemProvider, amount));
    }

    public GrinderRecipeBuilder setByproduct(ItemStack stack) {
        this.byproduct = stack;
        return this;
    }

    public GrinderRecipeBuilder setByproduct(IItemProvider itemProvider, int amount) {
        return this.setByproduct(new ItemStack(itemProvider, amount));
    }

    public GrinderRecipeBuilder setByproduct(IItemProvider itemProvider) {
        return this.setByproduct(itemProvider, 1);
    }

    public GrinderRecipeBuilder setByproductChance(float chance) {
        this.byproductChance = MathHelper.clamp(chance, 0F, 1F);
        return this;
    }

    public GrinderRecipeBuilder setIngredient(Ingredient ingredient) {
        this.input = ingredient;
        return this;
    }

    public GrinderRecipeBuilder setIngredient(IItemProvider... itemProviders) {
        this.input = Ingredient.fromItems(itemProviders);
        return this;
    }

    public GrinderRecipeBuilder setIngredient(ItemStack... stacks) {
        this.input = Ingredient.fromStacks(stacks);
        return this;
    }

    public GrinderRecipeBuilder setIngredient(ITag<Item> tag) {
        this.input = Ingredient.fromTag(tag);
        return this;
    }

    public GrinderRecipeBuilder setEnergy(int energy) {
        this.energy = energy;
        return this;
    }

    public GrinderRecipeBuilder setExperience(float experience) {
        this.experience = experience;
        return this;
    }

    public GrinderRecipeBuilder setGroup(String group) {
        this.group = group;
        return this;
    }

    public GrinderRecipeBuilder addCondition(ICondition condition) {
        conditions.add(condition);
        return this;
    }

    public GrinderRecipeBuilder addCriterion(String key, ICriterionInstance criterionInstance) {
        this.advancementBuilder.withCriterion(key, criterionInstance);
        return this;
    }

    private void validate(ResourceLocation resourceLocation) {
        if (this.input == null) {
            throw new IllegalStateException("No input specified for recipe " + resourceLocation);
        } else if (this.advancementBuilder.getCriteria().isEmpty()) {
            throw new IllegalStateException("No way of obtaining recipe " + resourceLocation);
        } else if (this.energy <= 0) {
            throw new IllegalStateException("Energy for " + resourceLocation + " must be greater than 0!");
        }
    }

    public void build(Consumer<IFinishedRecipe> consumer) {
        this.build(consumer, ForgeRegistries.ITEMS.getKey(this.output.getItem()));
    }

    public void build(Consumer<IFinishedRecipe> consumer, String name) {
        ResourceLocation resourceLocation = ForgeRegistries.ITEMS.getKey(this.output.getItem());
        if ((new ResourceLocation(name)).equals(resourceLocation)) {
            throw new IllegalStateException("Grinding Recipe " + name + " should remove its 'save' argument");
        } else {
            this.build(consumer, new ResourceLocation(name));
        }
    }

    public void build(Consumer<IFinishedRecipe> consumer, ResourceLocation name) {
        this.validate(name);
        this.advancementBuilder.withParentId(new ResourceLocation("recipes/root")).withCriterion("has_the_recipe", RecipeUnlockedTrigger.create(name)).withRewards(net.minecraft.advancements.AdvancementRewards.Builder.recipe(name)).withRequirementsStrategy(IRequirementsStrategy.OR);
        consumer.accept(new Result(name, group == null ? "" : group, input, output, byproduct, byproductChance, experience, energy, conditions, advancementBuilder, new ResourceLocation(name.getNamespace(), "recipes/" + this.output.getItem().getGroup().getPath() + "/" + name.getPath())));
    }

    public class Result implements IFinishedRecipe {

        private final ResourceLocation id;
        private final String group;
        private final Ingredient input;
        private final ItemStack output;
        private final ItemStack byproduct;
        private final float byproductChance;
        private final float experience;
        private final int energy;
        private final List<ICondition> conditions;
        private final Advancement.Builder advancementBuilder;
        private final ResourceLocation advancementId;

        public Result(ResourceLocation id, String group, Ingredient input, ItemStack output, ItemStack byproduct, float byproductChance, float experience, int energy, List<ICondition> conditions, Advancement.Builder advancementBuilder, ResourceLocation advancementId) {
            this.id = id;
            this.group = group;
            this.input = input;
            this.output = output;
            this.byproduct = byproduct;
            this.byproductChance = byproductChance;
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

            jsonObject.add("result", TCJsonUtil.serializeItemStack(this.output));

            if (this.byproduct != null) {
                JsonObject byproductJson = TCJsonUtil.serializeItemStack(this.byproduct);
                byproductJson.addProperty("chance", this.byproductChance);
            }

            jsonObject.add("ingredient", this.input.serialize());

            jsonObject.addProperty("experience", this.experience);

            jsonObject.addProperty("energy", this.energy);
        }

        @Override
        public ResourceLocation getID() {
            return this.id;
        }

        @Override
        public IRecipeSerializer<?> getSerializer() {
            return TCRecipeSerializers.GRINDING.get();
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
