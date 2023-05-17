package net.threetag.palladium.util.property;

import net.minecraft.core.Registry;
import net.minecraft.world.item.Item;

public class ItemProperty extends RegistryObjectProperty<Item> {

    public ItemProperty(String key) {
        super(key, Registry.ITEM);
    }

    @Override
    public String getPropertyType() {
        return "item";
    }
}
