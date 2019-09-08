package net.threetag.threecore.util.item;

import net.threetag.threecore.base.ThreeCoreBase;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class ItemGroupRegistry {

    private static Map<String, ItemGroup> TABS = new HashMap<>();

    public static final String TECHNOLOGY = "technology";

    static {
        getOrCreateCreativeTab(TECHNOLOGY, () -> new ItemStack(ThreeCoreBase.CIRCUIT));
    }

    public static ItemGroup getItemGroup(String name) {
        if (name.equalsIgnoreCase("blocks"))
            return ItemGroup.BUILDING_BLOCKS;
        if (name.equalsIgnoreCase("decoration"))
            return ItemGroup.DECORATIONS;
        if (name.equalsIgnoreCase("redstone"))
            return ItemGroup.REDSTONE;
        if (name.equalsIgnoreCase("transportation"))
            return ItemGroup.TRANSPORTATION;
        if (name.equalsIgnoreCase("misc"))
            return ItemGroup.MISC;
        if (name.equalsIgnoreCase("food"))
            return ItemGroup.FOOD;
        if (name.equalsIgnoreCase("tools"))
            return ItemGroup.TOOLS;
        if (name.equalsIgnoreCase("combat"))
            return ItemGroup.COMBAT;
        if (name.equalsIgnoreCase("brewing"))
            return ItemGroup.BREWING;

        return TABS.get(name.toLowerCase());
    }

    public static ItemGroup addItemGroup(String name, ItemStack stack) {
        return addItemGroup(name, () -> stack);
    }

    public static ItemGroup addItemGroup(String name, Supplier<ItemStack> stackSupplier) {
        return addItemGroup(name, new ItemGroup(name) {
            @Override
            public ItemStack createIcon() {
                return stackSupplier.get();
            }
        });
    }

    public static ItemGroup addItemGroup(String name, ItemGroup tab) {
        TABS.put(name, tab);
        return tab;
    }

    public static ItemGroup getOrCreateCreativeTab(String name, ItemStack stack) {
        return getOrCreateCreativeTab(name, () -> stack);
    }

    public static ItemGroup getOrCreateCreativeTab(String name, Supplier<ItemStack> stackSupplier) {
        ItemGroup tab = getItemGroup(name);

        if (tab != null)
            return tab;
        else {
            return addItemGroup(name, stackSupplier);
        }
    }

}
