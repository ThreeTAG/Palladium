package net.threetag.palladium.tag;

import net.minecraft.tags.TagKey;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.power.Power;
import net.threetag.palladium.registry.PalladiumRegistryKeys;

public class PalladiumPowerTags {

    public static final TagKey<Power> IS_MECHANICAL = tag("is_mechanical");
    public static final TagKey<Power> IS_MAGICAL = tag("is_magical");
    public static final TagKey<Power> IS_GENETIC = tag("is_genetic");

    private static TagKey<Power> tag(String path) {
        return TagKey.create(PalladiumRegistryKeys.POWER, Palladium.id(path));
    }
}
