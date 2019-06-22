package com.threetag.threecore.util.recipe;

import com.google.gson.JsonObject;
import net.minecraft.item.Item;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.Tag;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.crafting.IConditionSerializer;

import java.util.function.BooleanSupplier;

public class RecipeConditionTagExists implements IConditionSerializer {

    @Override
    public BooleanSupplier parse(JsonObject json) {
        ResourceLocation key = new ResourceLocation(JSONUtils.getString(json, "tag"));
        Tag<Item> tag = ItemTags.getCollection().get(key);
        return () -> tag != null && tag.getAllElements().size() > 0;
    }

}
