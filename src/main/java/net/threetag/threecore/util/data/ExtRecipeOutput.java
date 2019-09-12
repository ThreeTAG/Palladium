package net.threetag.threecore.util.data;

import com.google.gson.JsonObject;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tags.Tag;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

public class ExtRecipeOutput {

    private ItemStack stack;
    private Tag<Item> tag;
    private int amount;

    public ExtRecipeOutput(ItemStack stack) {
        this.stack = stack;
    }

    public ExtRecipeOutput(Tag<Item> tag, int amount) {
        this.tag = tag;
        this.amount = amount;
    }

    public ExtRecipeOutput(Tag<Item> tag) {
        this(tag, 1);
    }

    public ResourceLocation getId() {
        if (stack != null) {
            return ForgeRegistries.ITEMS.getKey(this.stack.getItem());
        } else {
            return this.tag.getAllElements().size() > 0 ? ForgeRegistries.ITEMS.getKey(this.tag.getAllElements().iterator().next()) : this.tag.getId();
        }
    }

    public String getGroup() {
        if (stack != null) {
            return this.stack.getItem().getGroup().getPath();
        } else if (this.tag.getAllElements().size() > 0) {
            return this.tag.getAllElements().iterator().next().getGroup().getPath();
        }

        return "";
    }

    public JsonObject serialize() {
        JsonObject jsonObject = new JsonObject();
        if (stack != null) {
            jsonObject.addProperty("item", this.stack.getItem().getRegistryName().toString());
            if (this.stack.getCount() > 1)
                jsonObject.addProperty("count", this.stack.getCount());
        } else {
            jsonObject.addProperty("tag", this.tag.getId().toString());
            if (this.amount > 1)
                jsonObject.addProperty("count", this.amount);
        }
        return jsonObject;
    }

}
