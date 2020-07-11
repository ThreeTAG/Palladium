package net.threetag.threecore.data;

import com.google.gson.JsonObject;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tags.ITag;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

public class ExtRecipeOutput {

    private ItemStack stack;
    private ITag.INamedTag<Item> tag;
    private int amount;

    public ExtRecipeOutput(ItemStack stack) {
        this.stack = stack;
    }

    public ExtRecipeOutput(ITag.INamedTag<Item> tag, int amount) {
        this.tag = tag;
        this.amount = amount;
    }

    public ExtRecipeOutput(ITag.INamedTag<Item> tag) {
        this(tag, 1);
    }

    public ResourceLocation getId() {
        if (stack != null) {
            return ForgeRegistries.ITEMS.getKey(this.stack.getItem());
        } else {
            return this.tag.func_230236_b_().size() > 0 ? ForgeRegistries.ITEMS.getKey(this.tag.func_230236_b_().iterator().next()) : this.tag.func_230234_a_();
        }
    }

    public String getGroup() {
        if (stack != null) {
            return this.stack.getItem().getGroup().getPath();
        } else if (this.tag.func_230236_b_().size() > 0) {
            return this.tag.func_230236_b_().iterator().next().getGroup().getPath();
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
            jsonObject.addProperty("tag", this.tag.func_230234_a_().toString());
            if (this.amount > 1)
                jsonObject.addProperty("count", this.amount);
        }
        return jsonObject;
    }

}
