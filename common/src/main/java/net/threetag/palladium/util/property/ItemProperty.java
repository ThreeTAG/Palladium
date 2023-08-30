package net.threetag.palladium.util.property;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;

public class ItemProperty extends RegistryObjectProperty<Item> {

    public ItemProperty(String key) {
        super(key, BuiltInRegistries.ITEM);
    }

    @Override
    public String getPropertyType() {
        return "item";
    }
}
