package net.threetag.threecore.item.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipe;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.registries.ForgeRegistryEntry;
import net.threetag.threecore.container.ConstructionTableInventory;

import javax.annotation.Nullable;
import java.util.Map;

public abstract class AbstractConstructionTableRecipe implements IRecipe<ConstructionTableInventory> {

    final ResourceLocation id;
    final String group;
    final NonNullList<Ingredient> recipeItems;
    final Ingredient toolIngredient;
    final ItemStack recipeOutput;

    public AbstractConstructionTableRecipe(ResourceLocation id, String group, NonNullList<Ingredient> recipeItems, Ingredient toolIngredient, ItemStack recipeOutput) {
        this.id = id;
        this.group = group;
        this.recipeItems = recipeItems;
        this.toolIngredient = toolIngredient == null ? Ingredient.EMPTY : toolIngredient;
        this.recipeOutput = recipeOutput;
    }

    @Override
    public ResourceLocation getId() {
        return this.id;
    }

    @Override
    public String getGroup() {
        return this.group;
    }

    @Override
    public boolean matches(ConstructionTableInventory inv, World worldIn) {
        if (inv.getSizeInventory() != this.recipeItems.size() + 1)
            return false;

        for (int i = 0; i < this.recipeItems.size(); i++) {
            ItemStack stack = inv.getStackInSlot(i);
            Ingredient ingredient = this.recipeItems.get(i);
            if (!ingredient.test(stack)) {
                return false;
            }
        }

        return this.toolIngredient == Ingredient.EMPTY || this.toolIngredient.test(inv.getToolItem());
    }

    @Override
    public ItemStack getCraftingResult(ConstructionTableInventory inv) {
        return this.getRecipeOutput().copy();
    }

    @Override
    public boolean canFit(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getRecipeOutput() {
        return this.recipeOutput;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        NonNullList<Ingredient> ingredients = NonNullList.create();
        ingredients.addAll(this.recipeItems);
        ingredients.add(this.toolIngredient);
        return ingredients;
    }

    @Override
    public NonNullList<ItemStack> getRemainingItems(ConstructionTableInventory inventory) {
        NonNullList<ItemStack> remaining = NonNullList.withSize(inventory.getSizeInventory(), ItemStack.EMPTY);

        for (int i = 0; i < remaining.size(); ++i) {
            ItemStack item = inventory.getStackInSlot(i);

            if (i == remaining.size() - 1 && item.isDamageable()) {
                ItemStack copy = item.copy();
                boolean[] broken = new boolean[]{false};
                PlayerEntity playerEntity = ForgeHooks.getCraftingPlayer();
                copy.attemptDamageItem(1, playerEntity.getRNG(), playerEntity instanceof ServerPlayerEntity ? (ServerPlayerEntity) playerEntity : null);
                inventory.markDirty();
                remaining.set(i, broken[0] ? ItemStack.EMPTY : copy);
            } else if (item.hasContainerItem()) {
                remaining.set(i, item.getContainerItem());
            }
        }

        return remaining;
    }

    public static abstract class Serializer<T extends AbstractConstructionTableRecipe> extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<T> {

        private final String[] shape;
        public final int size;

        public Serializer(String[] shape) {
            this.shape = shape;
            int i = 0;
            for (String s : shape) {
                i += s.length();
            }
            this.size = i;
        }

        public abstract T create(ResourceLocation id, String group, NonNullList<Ingredient> recipeItems, Ingredient toolIngredient, ItemStack result);

        @Override
        public T read(ResourceLocation recipeId, JsonObject json) {
            String group = JSONUtils.getString(json, "group", "");
            Map<String, Ingredient> map = ShapedRecipe.deserializeKey(JSONUtils.getJsonObject(json, "key"));
            JsonArray jsonArray = JSONUtils.getJsonArray(json, "pattern");
            NonNullList<Ingredient> recipeItems = NonNullList.withSize(this.size, Ingredient.EMPTY);

            if (jsonArray.size() != this.shape.length)
                throw new JsonSyntaxException("Invalid pattern: recipe needs " + this.shape.length + " rows");

            int k = 0;
            for (int i = 0; i < jsonArray.size(); i++) {
                String s = jsonArray.get(i).getAsString();

                if (s.length() != this.shape[i].length())
                    throw new JsonSyntaxException("Invalid pattern: recipe needs " + this.shape[i].length() + " columns in row #" + i);

                for (int j = 0; j < s.length(); j++) {
                    char c = s.charAt(j);
                    if (c != ' ') {
                        Ingredient ingredient = map.get(Character.valueOf(c).toString());
                        if (ingredient == null)
                            throw new JsonSyntaxException("Pattern references symbol '" + c + "' but it's not defined in the key");
                        recipeItems.set(k++, ingredient);
                    }
                }
            }

            Ingredient toolIngredient = null;
            if (JSONUtils.hasField(json, "tool")) {
                toolIngredient = Ingredient.deserialize(json.get("tool"));
            }

            ItemStack itemstack = ShapedRecipe.deserializeItem(JSONUtils.getJsonObject(json, "result"));
            return this.create(recipeId, group, recipeItems, toolIngredient, itemstack);
        }

        @Nullable
        @Override
        public T read(ResourceLocation recipeId, PacketBuffer buffer) {
            String group = buffer.readString(32767);
            NonNullList<Ingredient> recipeItems = NonNullList.withSize(this.size, Ingredient.EMPTY);

            for (int k = 0; k < recipeItems.size(); ++k) {
                recipeItems.set(k, Ingredient.read(buffer));
            }

            Ingredient ingredient = Ingredient.read(buffer);
            ItemStack result = buffer.readItemStack();
            return this.create(recipeId, group, recipeItems, ingredient, result);
        }

        @Override
        public void write(PacketBuffer buffer, T recipe) {
            buffer.writeString(recipe.group);

            for (Ingredient ingredient : recipe.recipeItems) {
                ingredient.write(buffer);
            }

            recipe.toolIngredient.write(buffer);
            buffer.writeItemStack(recipe.recipeOutput);
        }
    }

}
