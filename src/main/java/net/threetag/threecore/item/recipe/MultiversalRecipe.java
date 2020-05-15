package net.threetag.threecore.item.recipe;

import com.google.common.collect.Lists;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ForgeRegistryEntry;
import net.threetag.threecore.item.MultiversalExtrapolatorItem;
import net.threetag.threecore.util.RecipeUtil;

import javax.annotation.Nullable;
import java.util.List;

public class MultiversalRecipe implements IRecipe<IInventory> {

    public static final IRecipeType<MultiversalRecipe> RECIPE_TYPE = RecipeUtil.register("multiversal");

    private final ResourceLocation id;
    private final String universe;
    private final String identifier;
    private final List<ItemStack> items;

    public MultiversalRecipe(ResourceLocation id, String universe, String identifier, List<ItemStack> items) {
        this.id = id;
        this.universe = universe;
        this.identifier = identifier;
        this.items = items;
    }

    public String getUniverse() {
        return universe;
    }

    public String getIdentifier() {
        return identifier;
    }

    public List<ItemStack> getItems() {
        return items;
    }

    @Override
    public boolean matches(IInventory inv, World worldIn) {
        return false;
    }

    @Override
    public ItemStack getCraftingResult(IInventory inv) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canFit(int width, int height) {
        return false;
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
    public IRecipeSerializer<MultiversalRecipe> getSerializer() {
        return TCRecipeSerializers.MULTIVERSAL.get();
    }

    @Override
    public IRecipeType<MultiversalRecipe> getType() {
        return RECIPE_TYPE;
    }

    public static List<String> getIdentifiersFromItem(ItemStack stack, World world) {
        List<String> list = Lists.newArrayList();
        world.getRecipeManager().getRecipes(RECIPE_TYPE).forEach((id, recipe) -> {
            if (recipe instanceof MultiversalRecipe) {
                for (ItemStack stack1 : ((MultiversalRecipe) recipe).getItems()) {
                    if (stack.isItemEqual(stack1) && !list.contains(((MultiversalRecipe) recipe).getIdentifier())) {
                        list.add(((MultiversalRecipe) recipe).getIdentifier());
                    }
                }
            }
        });
        return list;
    }

    public static List<ItemStack> getVariations(ItemStack stack, String universe, List<String> identifiers, World world) {
        List<ItemStack> items = Lists.newArrayList();
        world.getRecipeManager().getRecipes(RECIPE_TYPE).forEach((id, recipe) -> {
            if (recipe instanceof MultiversalRecipe) {
                if (((MultiversalRecipe) recipe).getUniverse().equals(universe) && identifiers.contains(((MultiversalRecipe) recipe).getIdentifier()) && ((MultiversalRecipe) recipe).getUniverse().equals(universe)) {
                    for (ItemStack stack1 : ((MultiversalRecipe) recipe).getItems()) {
                        if (!stack.isItemEqual(stack1)) {
                            items.add(stack1);
                        }
                    }
                }
            }
        });
        return items;
    }

    public static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<MultiversalRecipe> {

        @Override
        public MultiversalRecipe read(ResourceLocation recipeId, JsonObject json) {
            String universe = MultiversalExtrapolatorItem.registerUniverse(JSONUtils.getString(json, "universe"));
            String identifier = JSONUtils.getString(json, "identifier");
            List<ItemStack> items = Lists.newArrayList();
            JsonElement itemsJson = json.get("items");

            if (itemsJson.isJsonPrimitive()) {
                Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(itemsJson.getAsString()));

                if (item == null)
                    throw new JsonParseException("Item " + itemsJson.getAsString() + " in recipe " + recipeId + " does not exist!");

                items.add(new ItemStack(item));
            } else if (itemsJson.isJsonArray()) {
                for (int i = 0; i < itemsJson.getAsJsonArray().size(); i++) {
                    Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(itemsJson.getAsJsonArray().get(i).getAsString()));

                    if (item == null)
                        throw new JsonParseException("Item " + itemsJson.getAsString() + " in recipe " + recipeId + " does not exist!");

                    items.add(new ItemStack(item));
                }
            }

            return new MultiversalRecipe(recipeId, universe, identifier, items);
        }

        @Nullable
        @Override
        public MultiversalRecipe read(ResourceLocation recipeId, PacketBuffer buffer) {
            String universe = MultiversalExtrapolatorItem.registerUniverse(buffer.readString());
            String identifier = buffer.readString();
            List<ItemStack> items = Lists.newArrayList();
            int amount = buffer.readInt();
            for (int i = 0; i < amount; i++) {
                items.add(buffer.readItemStack());
            }
            return new MultiversalRecipe(recipeId, universe, identifier, items);
        }

        @Override
        public void write(PacketBuffer buffer, MultiversalRecipe recipe) {
            buffer.writeString(recipe.universe);
            buffer.writeString(recipe.identifier);
            buffer.writeInt(recipe.items.size());
            for (ItemStack stack : recipe.items) {
                buffer.writeItemStack(stack);
            }
        }
    }
}
