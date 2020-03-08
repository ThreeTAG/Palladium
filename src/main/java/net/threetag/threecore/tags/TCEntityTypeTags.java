package net.threetag.threecore.tags;

import net.minecraft.entity.EntityType;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.tags.Tag;
import net.minecraft.util.ResourceLocation;
import net.threetag.threecore.ThreeCore;

public class TCEntityTypeTags {

    public static final Tag<EntityType<?>> ARMOR_STANDS = make(ThreeCore.MODID, "armor_stands");

    public static Tag<EntityType<?>> make(String name) {
        return new EntityTypeTags.Wrapper(new ResourceLocation(ThreeCore.MODID, name));
    }

    public static Tag<EntityType<?>> make(String domain, String path) {
        return new EntityTypeTags.Wrapper(new ResourceLocation(domain, path));
    }

}
