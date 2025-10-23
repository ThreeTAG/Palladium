package net.threetag.palladium.item;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.ToolMaterial;
import org.jetbrains.annotations.Nullable;

public class ToolMaterialRegistry {

    private static final BiMap<ResourceLocation, ToolMaterial> REGISTRY = HashBiMap.create();

    public static final Codec<ToolMaterial> REGISTRY_CODEC = ResourceLocation.CODEC.xmap(ToolMaterialRegistry::get, toolMaterial -> REGISTRY.inverse().get(toolMaterial));
    public static final Codec<ToolMaterial> DIRECT_CODEC = RecordCodecBuilder.create(instance -> instance.group(
            TagKey.hashedCodec(Registries.BLOCK).fieldOf("incorrect_blocks_for_drops").forGetter(ToolMaterial::incorrectBlocksForDrops),
            ExtraCodecs.NON_NEGATIVE_INT.fieldOf("durability").forGetter(ToolMaterial::durability),
            ExtraCodecs.NON_NEGATIVE_FLOAT.fieldOf("speed").forGetter(ToolMaterial::speed),
            ExtraCodecs.NON_NEGATIVE_FLOAT.fieldOf("attack_damage_bonus").forGetter(ToolMaterial::attackDamageBonus),
            ExtraCodecs.NON_NEGATIVE_INT.fieldOf("enchantment_value").forGetter(ToolMaterial::enchantmentValue),
            TagKey.hashedCodec(Registries.ITEM).fieldOf("repair_items").forGetter(ToolMaterial::repairItems)
    ).apply(instance, ToolMaterial::new));
    public static final Codec<ToolMaterial> CODEC = Codec.either(REGISTRY_CODEC, DIRECT_CODEC).xmap(
            either -> either.map(
                    toolMaterial -> toolMaterial,
                    toolMaterial -> toolMaterial
            ),
            toolMaterial -> REGISTRY.inverse().containsKey(toolMaterial) ? Either.left(toolMaterial) : Either.right(toolMaterial)
    );

    public static ToolMaterial register(ResourceLocation id, ToolMaterial toolMaterial) {
        if (REGISTRY.containsKey(id)) {
            throw new IllegalArgumentException("Duplicate tool material id: " + id);
        }

        REGISTRY.put(id, toolMaterial);
        return toolMaterial;
    }

    @Nullable
    public static ToolMaterial get(ResourceLocation id) {
        return REGISTRY.get(id);
    }

    static {
        // Register vanilla tool materials
        register(ResourceLocation.withDefaultNamespace("wood"), ToolMaterial.WOOD);
        register(ResourceLocation.withDefaultNamespace("stone"), ToolMaterial.STONE);
        register(ResourceLocation.withDefaultNamespace("iron"), ToolMaterial.IRON);
        register(ResourceLocation.withDefaultNamespace("diamond"), ToolMaterial.DIAMOND);
        register(ResourceLocation.withDefaultNamespace("gold"), ToolMaterial.GOLD);
        register(ResourceLocation.withDefaultNamespace("netherite"), ToolMaterial.NETHERITE);
    }

}
