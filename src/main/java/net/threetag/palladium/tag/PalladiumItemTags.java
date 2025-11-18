package net.threetag.palladium.tag;

import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.threetag.palladium.Palladium;

public class PalladiumItemTags {

    public static final TagKey<Item> VIBRATION_ABSORPTION_BOOTS = tag("vibration_absorption_boots");

    private static TagKey<Item> tag(String path) {
        return TagKey.create(Registries.ITEM, Palladium.id(path));
    }

}
