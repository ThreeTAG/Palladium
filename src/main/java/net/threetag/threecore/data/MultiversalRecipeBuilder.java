package net.threetag.threecore.data;

import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;
import net.threetag.threecore.item.recipe.MultiversalRecipe;
import net.threetag.threecore.item.recipe.TCRecipeSerializers;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Consumer;

public class MultiversalRecipeBuilder {

    private String universe;
    private String identifier;
    private List<Item> items = Lists.newArrayList();

    public MultiversalRecipeBuilder(String universe, String identifier, IItemProvider... items) {
        this.universe = universe;
        this.identifier = identifier;
        for (IItemProvider item : items) {
            this.items.add(item.asItem());
        }
    }

    private void validate(ResourceLocation resourceLocation) {
        if (this.items.isEmpty()) {
            throw new IllegalStateException("No items specified for recipe " + resourceLocation);
        } else if (this.universe == null || this.universe.isEmpty()) {
            throw new IllegalStateException("No universe specified for recipe " + resourceLocation);
        } else if (this.identifier == null || this.identifier.isEmpty()) {
            throw new IllegalStateException("No identifier specified for recipe " + resourceLocation);
        }
    }

    public void build(Consumer<IFinishedRecipe> consumer, ResourceLocation name) {
        this.validate(name);
        consumer.accept(new Result(name, this.universe, this.identifier, this.items));
    }

    public class Result implements IFinishedRecipe {

        private final ResourceLocation id;
        private final String universe;
        private final String identifier;
        private final List<Item> items;

        public Result(ResourceLocation id, String universe, String identifier, List<Item> items) {
            this.id = id;
            this.universe = universe;
            this.identifier = identifier;
            this.items = items;
        }

        @Override
        public void serialize(JsonObject json) {
            json.addProperty("universe", this.universe);
            json.addProperty("identifier", this.identifier);

            if (this.items.size() == 1) {
                json.addProperty("items", ForgeRegistries.ITEMS.getKey(this.items.get(0)).toString());
            } else {
                JsonArray items = new JsonArray();
                for (Item item : this.items) {
                    JsonObject itemJson = new JsonObject();
                    itemJson.addProperty("item", ForgeRegistries.ITEMS.getKey(item.getItem()).toString());
                    items.add(itemJson);
                }
                json.add("items", items);
            }
        }

        @Override
        public ResourceLocation getID() {
            return this.id;
        }

        @Override
        public IRecipeSerializer<MultiversalRecipe> getSerializer() {
            return TCRecipeSerializers.MULTIVERSAL.get();
        }

        @Nullable
        @Override
        public JsonObject getAdvancementJson() {
            return null;
        }

        @Nullable
        @Override
        public ResourceLocation getAdvancementID() {
            return null;
        }
    }
}
