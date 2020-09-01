package net.threetag.threecore.data;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.advancements.Advancement.Builder;
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
import net.minecraftforge.registries.ForgeRegistries;
import net.threetag.threecore.item.recipe.AbstractConstructionTableRecipe;
import net.threetag.threecore.util.TCJsonUtil;

import javax.annotation.Nullable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Consumer;

public class ConstructionTableRecipeBuilder {

    private final IRecipeSerializer<? extends AbstractConstructionTableRecipe> recipeSerializer;
    private final ItemStack result;
    private final List<String> pattern = Lists.newArrayList();
    private final Map<Character, Ingredient> key = Maps.newLinkedHashMap();
    private Ingredient toolIngredient;
    private final Builder advancementBuilder = Builder.builder();
    private String group;
    private boolean consumesTool;

    public ConstructionTableRecipeBuilder(IRecipeSerializer<? extends AbstractConstructionTableRecipe> recipeSerializer, IItemProvider itemProvider, int amount) {
        this.recipeSerializer = recipeSerializer;
        this.result = new ItemStack(itemProvider, amount);
    }

    public static ConstructionTableRecipeBuilder recipe(IRecipeSerializer<? extends AbstractConstructionTableRecipe> recipeSerializer, IItemProvider itemProvider) {
        return recipe(recipeSerializer, itemProvider, 1);
    }

    public static ConstructionTableRecipeBuilder recipe(IRecipeSerializer<? extends AbstractConstructionTableRecipe> recipeSerializer, IItemProvider itemProvider, int amount) {
        return new ConstructionTableRecipeBuilder(recipeSerializer, itemProvider, amount);
    }

    public ConstructionTableRecipeBuilder key(Character character, Tag<Item> tag) {
        return this.key(character, Ingredient.fromTag(tag));
    }

    public ConstructionTableRecipeBuilder key(Character character, IItemProvider itemProvider) {
        return this.key(character, Ingredient.fromItems(itemProvider));
    }

    public ConstructionTableRecipeBuilder key(Character character, Ingredient ingredient) {
        if (this.key.containsKey(character)) {
            throw new IllegalArgumentException("Symbol '" + character + "' is already defined!");
        } else if (character == ' ') {
            throw new IllegalArgumentException("Symbol ' ' (whitespace) is reserved and cannot be defined");
        } else {
            this.key.put(character, ingredient);
            return this;
        }
    }

    public ConstructionTableRecipeBuilder enableToolConsuming() {
        this.consumesTool = true;
        return this;
    }

    public ConstructionTableRecipeBuilder patternLine(String patternLine) {
        this.pattern.add(patternLine);
        return this;
    }

    public ConstructionTableRecipeBuilder addCriterion(String name, ICriterionInstance criterionInstance) {
        this.advancementBuilder.withCriterion(name, criterionInstance);
        return this;
    }

    public ConstructionTableRecipeBuilder setToolIngredient(Ingredient ingredient) {
        this.toolIngredient = ingredient;
        return this;
    }

    public ConstructionTableRecipeBuilder setGroup(String group) {
        this.group = group;
        return this;
    }

    public void build(Consumer<IFinishedRecipe> consumer) {
        this.build(consumer, ForgeRegistries.ITEMS.getKey(this.result.getItem()).toString());
    }

    public void build(Consumer<IFinishedRecipe> consumer, String name) {
        ResourceLocation id = ForgeRegistries.ITEMS.getKey(this.result.getItem());
        if ((new ResourceLocation(name)).equals(id)) {
            throw new IllegalStateException("Construction Table Recipe " + name + " should remove its 'save' argument");
        } else {
            this.build(consumer, new ResourceLocation(name));
        }
    }

    public void build(Consumer<IFinishedRecipe> consumer, ResourceLocation location) {
        this.validate(location);
        this.advancementBuilder.withParentId(new ResourceLocation("recipes/root")).withCriterion("has_the_recipe", RecipeUnlockedTrigger.create(location)).withRewards(net.minecraft.advancements.AdvancementRewards.Builder.recipe(location)).withRequirementsStrategy(IRequirementsStrategy.OR);
        consumer.accept(new Result(this.recipeSerializer, location, this.result, this.group == null ? "" : this.group, this.pattern, this.key, this.toolIngredient, this.advancementBuilder, new ResourceLocation(location.getNamespace(), "recipes/" + this.result.getItem().getGroup().getPath() + "/" + location.getPath()), this.consumesTool));
    }

    private void validate(ResourceLocation location) {
        // TODO do this validation thing
        if (this.pattern.isEmpty()) {
            throw new IllegalStateException("No pattern is defined for construction table recipe " + location + "!");
        } else if (this.advancementBuilder.getCriteria().isEmpty()) {
            throw new IllegalStateException("No way of obtaining recipe " + location);
        }
    }

    public class Result implements IFinishedRecipe {

        private final IRecipeSerializer<? extends AbstractConstructionTableRecipe> recipeSerializer;
        private final ResourceLocation id;
        private final ItemStack result;
        private final String group;
        private final List<String> pattern;
        private final Map<Character, Ingredient> key;
        private final Ingredient toolIngredient;
        private final Builder advancementBuilder;
        private final ResourceLocation advancementId;
        private final boolean consumesTool;

        public Result(IRecipeSerializer<? extends AbstractConstructionTableRecipe> recipeSerializer, ResourceLocation id, ItemStack result, String group, List<String> pattern, Map<Character, Ingredient> key, Ingredient toolIngredient, Builder advancementBuilder, ResourceLocation advancementId, boolean consumesTool) {
            this.recipeSerializer = recipeSerializer;
            this.id = id;
            this.result = result;
            this.group = group;
            this.pattern = pattern;
            this.key = key;
            this.toolIngredient = toolIngredient;
            this.advancementBuilder = advancementBuilder;
            this.advancementId = advancementId;
            this.consumesTool = consumesTool;
        }

        public void serialize(JsonObject json) {
            if (!this.group.isEmpty()) {
                json.addProperty("group", this.group);
            }

            JsonArray patternArray = new JsonArray();
            Iterator var3 = this.pattern.iterator();

            while (var3.hasNext()) {
                String line = (String) var3.next();
                patternArray.add(line);
            }

            json.add("pattern", patternArray);
            JsonObject keyJson = new JsonObject();
            Iterator var7 = this.key.entrySet().iterator();

            while (var7.hasNext()) {
                Entry<Character, Ingredient> entry = (Entry) var7.next();
                keyJson.add(String.valueOf(entry.getKey()), (entry.getValue()).serialize());
            }

            json.add("key", keyJson);

            if (this.toolIngredient != null) {
                json.add("tool", this.toolIngredient.serialize());
            }

            json.addProperty("consumes_tool", this.consumesTool);

            json.add("result", TCJsonUtil.serializeItemStack(this.result));
        }

        public IRecipeSerializer<?> getSerializer() {
            return this.recipeSerializer;
        }

        public ResourceLocation getID() {
            return this.id;
        }

        @Nullable
        public JsonObject getAdvancementJson() {
            return this.advancementBuilder.serialize();
        }

        @Nullable
        public ResourceLocation getAdvancementID() {
            return this.advancementId;
        }
    }
}
