package com.threetag.threecore.base.recipe;

import com.google.gson.JsonObject;
import com.threetag.threecore.ThreeCore;
import com.threetag.threecore.base.ThreeCoreBase;
import com.threetag.threecore.util.recipe.RecipeUtil;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

public class PressingRecipe implements IRecipe<IInventory> {

    public static final IRecipeType<PressingRecipe> RECIPE_TYPE = Registry.register(Registry.RECIPE_TYPE, new ResourceLocation(ThreeCore.MODID, "pressing"), new IRecipeType<PressingRecipe>() {
        public String toString() {
            return "pressing";
        }
    });

    private final ResourceLocation id;
    private final String group;
    private final Ingredient input;
    private final Ingredient cast;
    private final ItemStack output;
    private final float experience;
    private final int energy;

    public PressingRecipe(ResourceLocation id, String group, Ingredient input, Ingredient cast, ItemStack output, float experience, int energy) {
        this.id = id;
        this.group = group;
        this.input = input;
        this.cast = cast;
        this.output = output;
        this.experience = experience;
        this.energy = energy;
    }

    @Override
    public ItemStack getIcon() {
        return new ItemStack(ThreeCoreBase.HYDRAULIC_PRESS);
    }

    @Override
    public boolean matches(IInventory inv, World worldIn) {
        ItemStack castStack = inv.getStackInSlot(0);
        boolean cast = this.cast == null ? castStack.isEmpty() : this.cast.test(castStack);
        return cast && this.input.test(inv.getStackInSlot(1));
    }

    @Override
    public ItemStack getCraftingResult(IInventory inv) {
        return this.output.copy();
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        NonNullList<Ingredient> nonnulllist = NonNullList.create();
        nonnulllist.add(this.cast == null ? Ingredient.EMPTY : this.cast);
        nonnulllist.add(this.input);
        return nonnulllist;
    }

    @Override
    public boolean canFit(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getRecipeOutput() {
        return this.output;
    }

    @Override
    public ResourceLocation getId() {
        return this.id;
    }

    @Override
    public String getGroup() {
        return this.group;
    }

    public int getEnergy() {
        return this.energy;
    }

    public float getExperience() {
        return this.experience;
    }

    public Ingredient getCast() {
        return cast;
    }

    @Override
    public IRecipeSerializer<PressingRecipe> getSerializer() {
        return ThreeCoreBase.PRESSING_RECIPE_SERIALIZER;
    }

    @Override
    public IRecipeType<PressingRecipe> getType() {
        return RECIPE_TYPE;
    }

    public static class Serializer extends net.minecraftforge.registries.ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<PressingRecipe> {

        public static final ResourceLocation NAME = new ResourceLocation(ThreeCore.MODID, "grinding");

        @Override
        public PressingRecipe read(ResourceLocation recipeId, JsonObject json) {
            String group = JSONUtils.getString(json, "group", "");

            Ingredient ingredient;
            if (JSONUtils.isJsonArray(json, "ingredient")) {
                ingredient = Ingredient.deserialize(JSONUtils.getJsonArray(json, "ingredient"));
            } else {
                ingredient = Ingredient.deserialize(JSONUtils.getJsonObject(json, "ingredient"));
            }

            Ingredient cast = null;
            if (JSONUtils.hasField(json, "cast")) {
                if (JSONUtils.isJsonArray(json, "cast")) {
                    cast = Ingredient.deserialize(JSONUtils.getJsonArray(json, "cast"));
                } else {
                    cast = Ingredient.deserialize(JSONUtils.getJsonObject(json, "cast"));
                }
            }

            float xp = JSONUtils.getFloat(json, "experience", 0.0F);
            int energy = JSONUtils.getInt(json, "energy");

            return new PressingRecipe(recipeId, group, ingredient, cast, RecipeUtil.parseItemStackExt(JSONUtils.getJsonObject(json, "result"), true), xp, energy);
        }

        @Override
        public PressingRecipe read(ResourceLocation recipeId, PacketBuffer buffer) {
            String s = buffer.readString(32767);
            Ingredient ingredient = Ingredient.read(buffer);
            Ingredient cast = Ingredient.read(buffer);
            ItemStack itemstack = buffer.readItemStack();
            float xp = buffer.readFloat();
            int energy = buffer.readVarInt();
            return new PressingRecipe(recipeId, s, ingredient, cast, itemstack, xp, energy);
        }

        @Override
        public void write(PacketBuffer buffer, PressingRecipe recipe) {
            buffer.writeString(recipe.group);
            recipe.input.write(buffer);
            recipe.cast.write(buffer);
            buffer.writeItemStack(recipe.output);
            buffer.writeFloat(recipe.experience);
            buffer.writeVarInt(recipe.energy);
        }

    }

}
