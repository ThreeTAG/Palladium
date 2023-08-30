package net.threetag.palladium.util.property;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.enchantment.Enchantment;

public class EnchantmentProperty extends RegistryObjectProperty<Enchantment> {

    public EnchantmentProperty(String key) {
        super(key, BuiltInRegistries.ENCHANTMENT);
    }

    @Override
    public String getPropertyType() {
        return "enchantment";
    }
}
