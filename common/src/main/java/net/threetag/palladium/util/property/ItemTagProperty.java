package net.threetag.palladium.util.property;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;

public class ItemTagProperty extends TagKeyProperty<Item> {

    public ItemTagProperty(String key) {
        super(key, Registries.ITEM);
    }

}
