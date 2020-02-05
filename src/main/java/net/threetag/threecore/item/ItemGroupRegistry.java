package net.threetag.threecore.item;

import net.minecraft.item.*;
import net.minecraft.util.NonNullList;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class ItemGroupRegistry {

    private static Map<String, ItemGroup> ITEM_GROUPS = new HashMap<>();

    public static final String TECHNOLOGY = "technology";
    public static final String SUITS_AND_ARMOR = "suits_and_armor";

    static {
        getOrCreateItemGroup(TECHNOLOGY, () -> new ItemStack(TCItems.CIRCUIT.get()));
        addItemGroup(SUITS_AND_ARMOR, new SuitsAndArmorItemGroup(SUITS_AND_ARMOR));
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

        return ITEM_GROUPS.get(name.toLowerCase());
    }

    public static ItemGroup addItemGroup(String name, ItemStack stack) {
        return addItemGroup(name, () -> stack);
    }

    public static ItemGroup addItemGroup(String name, Supplier<ItemStack> stackSupplier) {
        return addItemGroup(name, new SimpleItemGroup(name, stackSupplier));
    }

    public static ItemGroup addItemGroup(String name, ItemGroup itemGroup) {
        ITEM_GROUPS.put(name, itemGroup);
        return itemGroup;
    }

    public static ItemGroup getOrCreateItemGroup(String name, ItemStack stack) {
        return getOrCreateItemGroup(name, () -> stack);
    }

    public static ItemGroup getOrCreateItemGroup(String name, Supplier<ItemStack> stackSupplier) {
        ItemGroup itemGroup = getItemGroup(name);

        if (itemGroup != null)
            return itemGroup;
        else {
            return addItemGroup(name, stackSupplier);
        }
    }

    public static ItemGroup getTechnologyGroup() {
        return getItemGroup(TECHNOLOGY);
    }

    public static ItemGroup getSuitsAndArmorGroup() {
        return getItemGroup(SUITS_AND_ARMOR);
    }

    public static class SuitsAndArmorItemGroup extends ItemGroup {

        public SuitsAndArmorItemGroup(String label) {
            super(label);
        }

        @Override
        public ItemStack createIcon() {
            return new ItemStack(Items.LEATHER_CHESTPLATE);
        }

        @OnlyIn(Dist.CLIENT)
        @Override
        public void fill(NonNullList<ItemStack> items) {
            for (Item item : ForgeRegistries.ITEMS) {
                item.fillItemGroup(this, items);

                if (item.getRegistryName().getNamespace().equalsIgnoreCase("minecraft") && item instanceof ArmorItem) {
                    items.add(new ItemStack(item));
                }
            }
        }
    }

}
