package net.threetag.threecore.base.recipe;

import com.google.gson.JsonObject;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistryEntry;
import net.threetag.threecore.base.block.TCBaseBlocks;
import net.threetag.threecore.util.recipe.IEnergyRecipe;
import net.threetag.threecore.util.recipe.RecipeUtil;

public class GrindingRecipe implements IEnergyRecipe<IInventory> {

    public static final IRecipeType<GrindingRecipe> RECIPE_TYPE = RecipeUtil.register("grinding");

    private final ResourceLocation id;
    private final String group;
    private final Ingredient input;
    private final ItemStack output;
    private final ItemStack byproduct;
    private final float byproductChance;
    private final float experience;
    private final int energy;

    public GrindingRecipe(ResourceLocation id, String group, Ingredient input, ItemStack output, ItemStack byproduct, float byproductChance, float experience, int energy) {
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
    public ItemStack getIcon() {
        return new ItemStack(TCBaseBlocks.GRINDER);
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

    @Override
    public int getRequiredEnergy() {
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
    public IRecipeSerializer<GrindingRecipe> getSerializer() {
        return TCBaseRecipeSerializers.GRINDING;
    }

    @Override
    public IRecipeType<GrindingRecipe> getType() {
        return RECIPE_TYPE;
    }

    public static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<GrindingRecipe> {

        @Override
        public GrindingRecipe read(ResourceLocation recipeId, JsonObject json) {
            String group = JSONUtils.getString(json, "group", "");
            Ingredient ingredient;
            if (JSONUtils.isJsonArray(json, "ingredient")) {
                ingredient = Ingredient.deserialize(JSONUtils.getJsonArray(json, "ingredient"));
            } else {
                ingredient = Ingredient.deserialize(JSONUtils.getJsonObject(json, "ingredient"));
            }

            float xp = JSONUtils.getFloat(json, "experience", 0.0F);
            int energy = JSONUtils.getInt(json, "energy");

            ItemStack byproduct = ItemStack.EMPTY;
            float byproductChance = 0F;

            if (JSONUtils.hasField(json, "byproduct")) {
                byproduct = RecipeUtil.parseItemStackExt(JSONUtils.getJsonObject(json, "byproduct"), true);
                byproductChance = JSONUtils.getFloat(JSONUtils.getJsonObject(json, "byproduct"), "chance", 1F);
            }

            return new GrindingRecipe(recipeId, group, ingredient, RecipeUtil.parseItemStackExt(JSONUtils.getJsonObject(json, "result"), true), byproduct, byproductChance, xp, energy);
        }

        @Override
        public GrindingRecipe read(ResourceLocation recipeId, PacketBuffer buffer) {
            String s = buffer.readString();
            Ingredient ingredient = Ingredient.read(buffer);
            ItemStack itemstack = buffer.readItemStack();
            ItemStack byproduct = buffer.readItemStack();
            float byproductChance = buffer.readFloat();
            float xp = buffer.readFloat();
            int energy = buffer.readVarInt();
            return new GrindingRecipe(recipeId, s, ingredient, itemstack, byproduct, byproductChance, xp, energy);
        }

        @Override
        public void write(PacketBuffer buffer, GrindingRecipe recipe) {
            buffer.writeString(recipe.group);
            recipe.input.write(buffer);
            buffer.writeItemStack(recipe.output);
            buffer.writeItemStack(recipe.byproduct);
            buffer.writeFloat(recipe.byproductChance);
            buffer.writeFloat(recipe.experience);
            buffer.writeVarInt(recipe.energy);
        }

    }

}
