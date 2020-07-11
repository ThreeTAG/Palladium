package net.threetag.threecore.tags;

import net.minecraft.entity.EntityType;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.tags.ITag;
import net.threetag.threecore.ThreeCore;

public class TCEntityTypeTags {

    public static final ITag.INamedTag<EntityType<?>> ARMOR_STANDS = make(ThreeCore.MODID, "armor_stands");

    public static ITag.INamedTag<EntityType<?>> make(String name) {
        return EntityTypeTags.func_232896_a_(ThreeCore.MODID + ":" + name);
    }

    public static ITag.INamedTag<EntityType<?>> make(String domain, String path) {
        return EntityTypeTags.func_232896_a_(domain + ":" + path);
    }

}
