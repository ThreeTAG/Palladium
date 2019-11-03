package net.threetag.threecore.util.item;

import net.minecraft.item.IItemTier;
import net.minecraft.item.ItemTier;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class ItemTierRegistry {

    private static Map<String, IItemTier> ITEM_TIERS = new HashMap<>();

    static {
        addItemTier("wood", ItemTier.WOOD);
        addItemTier("stone", ItemTier.STONE);
        addItemTier("iron", ItemTier.WOOD);
        addItemTier("gold", ItemTier.GOLD);
        addItemTier("diamond", ItemTier.DIAMOND);
    }

    public static IItemTier getItemTier(String name) {
        return ITEM_TIERS.get(name.toLowerCase());
    }

    public static IItemTier addItemTier(String name, IItemTier itemTier) {
        ITEM_TIERS.put(name.toLowerCase(), itemTier);
        return itemTier;
    }

    public static IItemTier getOrCreateItemTier(String name, Supplier<IItemTier> itemTierSupplier) {
        return getItemTier(name) != null ? getItemTier(name) : addItemTier(name, itemTierSupplier.get());
    }

}
