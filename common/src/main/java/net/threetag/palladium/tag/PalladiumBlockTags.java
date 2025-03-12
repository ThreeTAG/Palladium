package net.threetag.palladium.tag;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.threetag.palladium.Palladium;

public class PalladiumBlockTags {

    public static final TagKey<Block> PREVENTS_INTANGIBILITY = tag("prevents_intangibility");

    private static TagKey<Block> tag(String path) {
        return tag(Palladium.MOD_ID, path);
    }

    private static TagKey<Block> tag(String domain, String path) {
        return TagKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(domain, path));
    }

    private static TagKey<Block> commonTag(String path) {
        return TagKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath("c", path));
    }
}
