package net.threetag.threecore.util;

import com.google.common.collect.Lists;
import com.google.gson.*;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.registries.ForgeRegistries;
import net.threetag.threecore.ThreeCore;

public class RecipeUtil {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public static <T extends IRecipe<?>> IRecipeType<T> register(final String id) {
        return register(new ResourceLocation(ThreeCore.MODID, id));
    }

    public static <T extends IRecipe<?>> IRecipeType<T> register(final ResourceLocation name) {
        return Registry.register(Registry.RECIPE_TYPE, name, new IRecipeType<T>() {
            public String toString() {
                return name.toString();
            }
        });
    }

    public static ItemStack parseItemStackExt(JsonObject json, boolean readNBT) {
        if (json.has("tag")) {
            ITag<Item> tag = ItemTags.getCollection().get(new ResourceLocation(JSONUtils.getString(json, "tag")));

            if (tag == null || tag.func_230236_b_().size() <= 0)
                throw new JsonSyntaxException("Unknown tag '" + JSONUtils.getString(json, "tag") + "'");

            Item item = Lists.newArrayList(tag.func_230236_b_()).get(0);

            if (readNBT && json.has("nbt")) {
                // Lets hope this works? Needs test
                try {
                    JsonElement element = json.get("nbt");
                    CompoundNBT nbt;
                    if (element.isJsonObject())
                        nbt = JsonToNBT.getTagFromJson(GSON.toJson(element));
                    else
                        nbt = JsonToNBT.getTagFromJson(element.getAsString());

                    CompoundNBT tmp = new CompoundNBT();
                    if (nbt.contains("ForgeCaps")) {
                        tmp.put("ForgeCaps", nbt.get("ForgeCaps"));
                        nbt.remove("ForgeCaps");
                    }

                    tmp.put("tag", nbt);
                    tmp.putString("id", ForgeRegistries.ITEMS.getKey(item).toString());
                    tmp.putInt("Count", JSONUtils.getInt(json, "count", 1));

                    return ItemStack.read(tmp);
                } catch (CommandSyntaxException e) {
                    throw new JsonSyntaxException("Invalid NBT Entry: " + e.toString());
                }
            }

            return new ItemStack(item, JSONUtils.getInt(json, "count", 1));
        } else {
            return CraftingHelper.getItemStack(json, readNBT);
        }
    }

}
