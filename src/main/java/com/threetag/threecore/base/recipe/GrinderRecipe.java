package com.threetag.threecore.base.recipe;

import com.google.gson.JsonObject;
import com.threetag.threecore.ThreeCore;
import com.threetag.threecore.util.recipe.RecipeUtil;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipe;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.crafting.RecipeType;

public class GrinderRecipe implements IRecipe {

    public static final RecipeType<GrinderRecipe> RECIPE_TYPE = RecipeType.get(new ResourceLocation("grinding"), GrinderRecipe.class);
    public static final IRecipeSerializer<GrinderRecipe> SERIALIZER = new Serializer();

    private final ResourceLocation id;
    private final String group;
    private final Ingredient input;
    private final ItemStack output;
    private final ItemStack byproduct;
    private final float byproductChance;
    private final float experience;
    private final int energy;

    public GrinderRecipe(ResourceLocation id, String group, Ingredient input, ItemStack output, ItemStack byproduct, float byproductChance, float experience, int energy) {
        this.id = id;
        this.group = group;
        this.input = input;
        this.output = output;
        this.byproduct = byproduct;
        this.byproductChance = byproductChance;
        this.experience = experience;
        this.energy = energy;
    }

    @Override
    public boolean matches(IInventory inv, World worldIn) {
        return this.input.test(inv.getStackInSlot(1));
    }

    @Override
    public ItemStack getCraftingResult(IInventory inv) {
        return this.output.copy();
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        NonNullList<Ingredient> nonnulllist = NonNullList.create();
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

    public ItemStack getByproduct() {
        return byproduct;
    }

    public float getByproductChance() {
        return byproductChance;
    }

    @Override
    public RecipeType<? extends IRecipe> getType() {
        return RECIPE_TYPE;
    }

    @Override
    public IRecipeSerializer<GrinderRecipe> getSerializer() {
        return SERIALIZER;
    }

    public static class Serializer implements IRecipeSerializer<GrinderRecipe> {

        public static final ResourceLocation NAME = new ResourceLocation(ThreeCore.MODID, "grinding");

        @Override
        public GrinderRecipe read(ResourceLocation recipeId, JsonObject json) {
            String group = JsonUtils.getString(json, "group", "");
            Ingredient ingredient;
            if (JsonUtils.isJsonArray(json, "ingredient")) {
                ingredient = Ingredient.deserialize(JsonUtils.getJsonArray(json, "ingredient"));
            } else {
                ingredient = Ingredient.deserialize(JsonUtils.getJsonObject(json, "ingredient"));
            }

            float xp = JsonUtils.getFloat(json, "experience", 0.0F);
            int energy = JsonUtils.getInt(json, "energy");

            ItemStack byproduct = ItemStack.EMPTY;
            float byproductChance = 0F;

            if (JsonUtils.hasField(json, "byproduct")) {
                byproduct = RecipeUtil.parseItemStackExt(JsonUtils.getJsonObject(json, "byproduct"), true);
                byproductChance = JsonUtils.getFloat(JsonUtils.getJsonObject(json, "byproduct"), "chance", 1F);
            }

            return new GrinderRecipe(recipeId, group, ingredient, RecipeUtil.parseItemStackExt(JsonUtils.getJsonObject(json, "result"), true), byproduct, byproductChance, xp, energy);
        }

        @Override
        public GrinderRecipe read(ResourceLocation recipeId, PacketBuffer buffer) {
            String s = buffer.readString(32767);
            Ingredient ingredient = Ingredient.read(buffer);
            ItemStack itemstack = buffer.readItemStack();
            ItemStack byproduct = buffer.readItemStack();
            float byproductChance = buffer.readFloat();
            float xp = buffer.readFloat();
            int energy = buffer.readVarInt();
            return new GrinderRecipe(recipeId, s, ingredient, itemstack, byproduct, byproductChance, xp, energy);
        }

        @Override
        public void write(PacketBuffer buffer, GrinderRecipe recipe) {
            buffer.writeString(recipe.group);
            recipe.input.write(buffer);
            buffer.writeItemStack(recipe.output);
            buffer.writeItemStack(recipe.byproduct);
            buffer.writeFloat(recipe.byproductChance);
            buffer.writeFloat(recipe.experience);
            buffer.writeVarInt(recipe.energy);
        }

        @Override
        public ResourceLocation getName() {
            return NAME;
        }
    }

}
