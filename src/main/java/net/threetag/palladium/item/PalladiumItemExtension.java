package net.threetag.palladium.item;

import net.minecraft.world.item.Item;

public interface PalladiumItemExtension {

    default Item.Properties palladium$getProperties() {
        return new Item.Properties();
    }

}
