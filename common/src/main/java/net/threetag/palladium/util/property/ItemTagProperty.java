package net.threetag.palladium.util.property;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.material.Fluid;

public class ItemTagProperty extends TagKeyProperty<Item> {

    public ItemTagProperty(String key) {
        super(key, Registries.ITEM);
    }

}
