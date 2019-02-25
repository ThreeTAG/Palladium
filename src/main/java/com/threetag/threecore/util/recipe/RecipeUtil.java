package com.threetag.threecore.util.recipe;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.threetag.threecore.ThreeCore;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.registries.ForgeRegistries;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class RecipeUtil {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static File RECIPE_DIR = null;

    public static void init() {
        CraftingHelper.register(new ResourceLocation(ThreeCore.MODID, "tag_exists"), new RecipeConditionTagExists());
    }

    public static void setupDir() {
        if (RECIPE_DIR == null) {
            RECIPE_DIR = new File("recipes");
        }

        if (!RECIPE_DIR.exists()) {
            RECIPE_DIR.mkdir();
        }
    }

    public static Map<String, Object> getCondition(Object o) {
        if (o instanceof String) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("type", ThreeCore.MODID + ":tag_exists");
            map.put("tag", o.toString());
            return map;
        } else if (o instanceof Item) {
            ResourceLocation key = ForgeRegistries.ITEMS.getKey((Item) o);
            if (key.getNamespace().equalsIgnoreCase("minecraft") || key.getNamespace().equalsIgnoreCase(ThreeCore.MODID))
                return null;
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("type", "forge:item_exists");
            map.put("item", key.toString());
            return map;
        } else if (o instanceof ItemStack) {
            return getCondition(((ItemStack) o).getItem());
        } else if (o instanceof Block) {
            return getCondition(Item.getItemFromBlock((Block) o));
        }

        return null;
    }

    public static List<Map<String, Object>> generateConditions(List<Object> objects) {
        List<Map<String, Object>> list = new LinkedList<>();
        for (Object o : objects) {
            Map<String, Object> condition = getCondition(o);
            if (condition != null)
                list.add(condition);
        }
        return list;
    }

    public static void addSmeltingRecipe(String name, ItemStack output, Object input, float xp, int cookingTime) {
        addSmeltingRecipe(name, null, output, input, xp, cookingTime);
    }

    public static void addSmeltingRecipe(String name, String group, ItemStack output, Object input, float xp, int cookingTime) {
        setupDir();
        Map<String, Object> json = new LinkedHashMap<>();

        List<Object> test = Arrays.asList(output, input);
        List<Map<String, Object>> conditions = generateConditions(test);
        if (conditions.size() > 0)
            json.put("conditions", conditions.toArray(new Map[conditions.size()]));

        json.put("type", "smelting");
        json.put("result", output.getItem().getRegistryName().toString());
        json.put("ingredient", serializeItem(input));
        json.put("experience", xp);
        json.put("cookingtime", cookingTime);

        if (group != null) {
            json.put("group", group);
        }

        File f = new File(RECIPE_DIR, name + ".json");

        try (FileWriter w = new FileWriter(f)) {
            GSON.toJson(json, w);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void addShapedRecipe(String name, ItemStack result, Object... components) {
        addShapedRecipe(name, null, result, components);
    }

    public static void addShapedRecipe(String name, String group, ItemStack result, Object... components) {
        setupDir();
        Map<String, Object> json = new LinkedHashMap<>();
        List<Object> test = new LinkedList<>();
        test.add(result);

        List<String> pattern = new ArrayList<>();
        int i = 0;
        while (i < components.length && components[i] instanceof String) {
            pattern.add((String) components[i]);
            i++;
        }
        json.put("pattern", pattern);

        Map<String, Map<String, Object>> key = new LinkedHashMap<>();
        Character curKey = null;
        for (; i < components.length; i++) {
            Object o = components[i];
            if (o instanceof Character) {
                if (curKey != null)
                    throw new IllegalArgumentException("Provided two char ac_keys in a row");
                curKey = (Character) o;
            } else {
                if (curKey == null)
                    throw new IllegalArgumentException("Providing object without a char key");
                key.put(Character.toString(curKey), serializeItem(o));

                if (!test.contains(o))
                    test.add(o);

                curKey = null;
            }
        }

        json.put("key", key);
        json.put("type", "minecraft:crafting_shaped");
        json.put("result", serializeItem(result));

        if (group != null) {
            json.put("group", group);
        }

        List<Map<String, Object>> conditions = generateConditions(test);
        if (conditions.size() > 0)
            json.put("conditions", conditions.toArray(new Map[conditions.size()]));

        File f = new File(RECIPE_DIR, name + ".json");

        try (FileWriter w = new FileWriter(f)) {
            GSON.toJson(json, w);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void addShapelessRecipe(String name, ItemStack result, Object... components) {
        addShapelessRecipe(name, null, result, components);
    }

    public static void addShapelessRecipe(String name, String group, ItemStack result, Object... components) {
        setupDir();
        Map<String, Object> json = new LinkedHashMap<>();

        List<Object> test = new LinkedList<>();
        test.add(result);
        for (Object o : components)
            if (!test.contains(o))
                test.add(o);
        List<Map<String, Object>> conditions = generateConditions(test);
        if (conditions.size() > 0)
            json.put("conditions", conditions.toArray(new Map[conditions.size()]));

        List<Map<String, Object>> ingredients = new ArrayList<>();
        for (Object o : components) {
            ingredients.add(serializeItem(o));
        }
        json.put("ingredients", ingredients);
        json.put("type", "minecraft:crafting_shapeless");
        json.put("result", serializeItem(result));

        if (group != null) {
            json.put("group", group);
        }

        File f = new File(RECIPE_DIR, name + ".json");

        try (FileWriter w = new FileWriter(f)) {
            GSON.toJson(json, w);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Map<String, Object> serializeItem(Object thing) {
        if (thing instanceof Item) {
            return serializeItem(new ItemStack((Item) thing));
        }
        if (thing instanceof Block) {
            return serializeItem(new ItemStack((Block) thing));
        }
        if (thing instanceof ItemStack) {
            ItemStack stack = (ItemStack) thing;
            Map<String, Object> ret = new LinkedHashMap<>();
            ret.put("item", stack.getItem().getRegistryName().toString());

            if (stack.getCount() > 1) {
                ret.put("count", stack.getCount());
            }

            if (stack.hasTag()) {
                ret.put("type", "minecraft:item_nbt");
                ret.put("nbt", stack.getTag().toString());
            }

            return ret;
        }
        if (thing instanceof String) {
            Map<String, Object> ret = new LinkedHashMap<>();
            ret.put("tag", (String) thing);
            return ret;
        }

        throw new IllegalArgumentException("Not a block, item, stack, or od name");
    }

}
