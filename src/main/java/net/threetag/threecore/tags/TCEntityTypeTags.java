package net.threetag.threecore.tags;

import net.minecraft.entity.EntityType;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.Tags;
import net.threetag.threecore.ThreeCore;

public class TCEntityTypeTags {

    public static final Tags.IOptionalNamedTag<EntityType<?>> ARMOR_STANDS = make(ThreeCore.MODID, "armor_stands");

    public static Tags.IOptionalNamedTag<EntityType<?>> make(String name) {
        return make(ThreeCore.MODID, name);
    }

    public static Tags.IOptionalNamedTag<EntityType<?>> make(String domain, String path) {
        return EntityTypeTags.createOptional(new ResourceLocation(domain, path));
    }

}
