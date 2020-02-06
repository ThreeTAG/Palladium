package net.threetag.threecore.item.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.RecipeItemHelper;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.util.RecipeMatcher;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistryEntry;
import net.threetag.threecore.fluid.FluidIngredient;
import net.threetag.threecore.item.FluidInventory;
import net.threetag.threecore.util.TCFluidUtil;
import net.threetag.threecore.util.RecipeUtil;

import javax.annotation.Nullable;

public class FluidComposingRecipe implements IEnergyRecipe<IInventory> {

    public static final int MAX_WIDTH = 9;
    public static final int MAX_HEIGHT = 9;

    public static final IRecipeType<FluidComposingRecipe> RECIPE_TYPE = RecipeUtil.register("fluid_composing");

    private final ResourceLocation id;
    private final String group;
    private final NonNullList<Ingredient> ingredients;
    private final FluidIngredient inputFluid;
    private final FluidStack output;
    private final int energy;
    private final boolean isSimple;

    public FluidComposingRecipe(ResourceLocation id, String group, NonNullList<Ingredient> ingredients, FluidIngredient inputFluid, FluidStack output, int energy) {
        this.id = id;
        this.group = group;
        this.ingredients = ingredients;
        this.inputFluid = inputFluid;
        this.output = output;
        this.energy = energy;
        this.isSimple = ingredients.stream().allMatch(Ingredient::isSimple);
    }

    @Override
    public boolean matches(IInventory inv, World worldIn) {
        if (!(inv instanceof FluidInventory) || !this.inputFluid.test(((FluidInventory) inv).getFluidTank().getFluid()))
            return false;

        RecipeItemHelper recipeitemhelper = new RecipeItemHelper();
        java.util.List<ItemStack> inputs = new java.util.ArrayList<>();
        int i = 0;

        for (int j = 0; j < inv.getSizeInventory(); ++j) {
            ItemStack itemstack = inv.getStackInSlot(j);
            if (!itemstack.isEmpty()) {
                ++i;
                if (isSimple)
                    recipeitemhelper.func_221264_a(itemstack, 1);
                else inputs.add(itemstack);
            }
        }

        return i == this.ingredients.size() && (isSimple ? recipeitemhelper.canCraft(this, null) : RecipeMatcher.findMatches(inputs, this.ingredients) != null);
    }

    @Override
    public ItemStack getCraftingResult(IInventory inv) {
        return ItemStack.EMPTY;
    }

    public FluidStack getResult(@Nullable IInventory inv) {
        return this.output;
    }

    @Override
    public int getRequiredEnergy() {
        return energy;
    }

    public FluidIngredient getInputFluid() {
        return inputFluid;
    }

    @Override
    public boolean canFit(int width, int height) {
        return width * height >= this.ingredients.size();
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return this.ingredients;
    }

    @Override
    public ItemStack getRecipeOutput() {
        return ItemStack.EMPTY;
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
    public IRecipeSerializer<FluidComposingRecipe> getSerializer() {
        return TCRecipeSerializers.FLUID_COMPOSING.get();
    }

    @Override
    public IRecipeType<FluidComposingRecipe> getType() {
        return RECIPE_TYPE;
    }

    public static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<FluidComposingRecipe> {

        @Override
        public FluidComposingRecipe read(ResourceLocation recipeId, JsonObject json) {
            String group = JSONUtils.getString(json, "group", "");
            NonNullList<Ingredient> ingredients = readIngredients(JSONUtils.getJsonArray(json, "ingredients"));
            int energy = JSONUtils.getInt(json, "energy");
            FluidIngredient fluidIngredient = FluidIngredient.deserialize(json.get("fluid_input"));
            FluidStack output = TCFluidUtil.deserializeFluidStack(JSONUtils.getJsonObject(json, "output"));

            if (ingredients.isEmpty()) {
                throw new JsonParseException("No ingredients for fluid composing recipe");
            } else if (ingredients.size() > MAX_WIDTH * MAX_HEIGHT) {
                throw new JsonParseException("Too many ingredients for fluid composing recipe the max is " + (MAX_WIDTH * MAX_HEIGHT));
            }

            return new FluidComposingRecipe(recipeId, group, ingredients, fluidIngredient, output, energy);
        }

        private static NonNullList<Ingredient> readIngredients(JsonArray jsonArray) {
            NonNullList<Ingredient> list = NonNullList.create();

            for (int i = 0; i < jsonArray.size(); ++i) {
                Ingredient ingredient = Ingredient.deserialize(jsonArray.get(i));
                if (!ingredient.hasNoMatchingItems()) {
                    list.add(ingredient);
                }
            }

            return list;
        }

        @Nullable
        @Override
        public FluidComposingRecipe read(ResourceLocation recipeId, PacketBuffer buffer) {
            String group = buffer.readString();
            int i = buffer.readVarInt();
            NonNullList<Ingredient> ingredients = NonNullList.withSize(i, Ingredient.EMPTY);

            for (int j = 0; j < ingredients.size(); ++j) {
                ingredients.set(j, Ingredient.read(buffer));
            }

            FluidIngredient fluidIngredient = FluidIngredient.read(buffer);
            FluidStack output = FluidStack.readFromPacket(buffer);
            int energy = buffer.readInt();

            return new FluidComposingRecipe(recipeId, group, ingredients, fluidIngredient, output, energy);
        }

        @Override
        public void write(PacketBuffer buffer, FluidComposingRecipe recipe) {
            buffer.writeString(recipe.group);
            buffer.writeVarInt(recipe.ingredients.size());

            for (Ingredient ingredient : recipe.ingredients) {
                ingredient.write(buffer);
            }

            recipe.inputFluid.write(buffer);
            recipe.output.writeToPacket(buffer);
            buffer.writeInt(recipe.energy);
        }
    }
}
