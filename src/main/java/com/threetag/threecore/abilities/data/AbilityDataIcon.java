package com.threetag.threecore.abilities.data;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.registries.ForgeRegistries;

public class AbilityDataIcon extends AbilityData<AbilityDataIcon.Icon> {

    public AbilityDataIcon(String key) {
        super(key);
    }

    @Override
    public Icon parseValue(JsonObject jsonObject, Icon defaultValue) {
        if (!JsonUtils.hasField(jsonObject, this.jsonKey))
            return defaultValue;
        JsonObject object = JsonUtils.getJsonObject(jsonObject, this.jsonKey);
        String type = JsonUtils.getString(object, "type");
        if (type.equalsIgnoreCase("item")) {
            ItemStack stack = CraftingHelper.getItemStack(object, true);
            return new Icon(stack);
        } else if (type.equalsIgnoreCase("texture")) {
            ResourceLocation resourceLocation = new ResourceLocation(JsonUtils.getString(object, "texture"));
            return new Icon(resourceLocation);
        } else if (type.equalsIgnoreCase("internal")) {
            return new Icon(true);
        } else {
            throw new JsonSyntaxException("Icon type must be either 'icon' or 'texture'!");
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt, Icon value) {
        NBTTagCompound tag = new NBTTagCompound();
        tag.putString("Type", value.texture != null ? "Texture" : (!value.stack.isEmpty() ? "Item" : "Internal"));
        if (value.texture != null)
            tag.putString("Texture", value.texture.toString());
        else if (!value.stack.isEmpty())
            tag.put("Item", value.stack.serializeNBT());
        nbt.put(this.key, tag);
    }

    @Override
    public Icon readFromNBT(NBTTagCompound nbt, Icon defaultValue) {
        NBTTagCompound tag = nbt.getCompound(this.key);
        if (tag.getString("Type").equalsIgnoreCase("Texture"))
            return new Icon(new ResourceLocation(tag.getString("Texture")));
        else if (tag.getString("Type").equalsIgnoreCase("Item"))
            return new Icon(ItemStack.read(tag.getCompound("Item")));
        else if (tag.getString("Type").equalsIgnoreCase("Internal"))
            return new Icon(true);
        return defaultValue;
    }

    @Override
    public String getDisplay(Icon value) {
        if (value.texture != null)
            return "{ \"type\": \"texture\", \"texture\": \"" + value.texture.toString() + "\" }";
        else if (!value.stack.isEmpty())
            return "{ \"type\": \"item\", \"item\": { \"item\": " + ForgeRegistries.ITEMS.getKey(value.stack.getItem()).toString() + "\" } }";
        else if (value.internal)
            return "{ \"type\": \"internal\" }";
        return super.getDisplay(value);
    }

    public static class Icon {

        public ItemStack stack = ItemStack.EMPTY;
        public ResourceLocation texture;
        public boolean internal;

        public Icon(ItemStack stack) {
            this.stack = stack;
        }

        public Icon(ResourceLocation texture) {
            this.texture = texture;
        }

        public Icon(boolean internal) {
            this.internal = internal;
        }
    }

}
