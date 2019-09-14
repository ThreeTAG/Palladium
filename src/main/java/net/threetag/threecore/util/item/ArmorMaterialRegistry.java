package net.threetag.threecore.util.item;

import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.IArmorMaterial;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class ArmorMaterialRegistry {

    private static Map<String, IArmorMaterial> ARMOR_MATERIALS = new HashMap<>();

    static {
        addArmorMaterial("leather", ArmorMaterial.LEATHER);
        addArmorMaterial("chain", ArmorMaterial.CHAIN);
        addArmorMaterial("iron", ArmorMaterial.IRON);
        addArmorMaterial("gold", ArmorMaterial.GOLD);
        addArmorMaterial("diamond", ArmorMaterial.DIAMOND);
        addArmorMaterial("turtle", ArmorMaterial.TURTLE);
    }

    public static IArmorMaterial getArmorMaterial(String name) {
        return ARMOR_MATERIALS.get(name.toLowerCase());
    }

    public static IArmorMaterial addArmorMaterial(String name, IArmorMaterial armorMaterial) {
        ARMOR_MATERIALS.put(name.toLowerCase(), armorMaterial);
        return armorMaterial;
    }

    public static IArmorMaterial getOrCreateArmorMaterial(String name, Supplier<IArmorMaterial> armorMaterialSupplier) {
        return getArmorMaterial(name) != null ? getArmorMaterial(name) : addArmorMaterial(name, armorMaterialSupplier.get());
    }

}
