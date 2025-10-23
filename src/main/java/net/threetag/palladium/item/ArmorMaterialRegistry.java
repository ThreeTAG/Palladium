package net.threetag.palladium.item;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.equipment.ArmorMaterial;
import net.minecraft.world.item.equipment.ArmorMaterials;
import net.minecraft.world.item.equipment.ArmorType;
import net.minecraft.world.item.equipment.EquipmentAssets;
import org.jetbrains.annotations.Nullable;

public class ArmorMaterialRegistry {

    private static final BiMap<ResourceLocation, ArmorMaterial> REGISTRY = HashBiMap.create();

    public static final Codec<ArmorMaterial> REGISTRY_CODEC = ResourceLocation.CODEC.xmap(ArmorMaterialRegistry::get, armorMaterial -> REGISTRY.inverse().get(armorMaterial));
    public static final Codec<ArmorMaterial> DIRECT_CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ExtraCodecs.NON_NEGATIVE_INT.fieldOf("durability").forGetter(ArmorMaterial::durability),
            Codec.unboundedMap(ArmorType.CODEC, ExtraCodecs.NON_NEGATIVE_INT).fieldOf("defense").forGetter(ArmorMaterial::defense),
            ExtraCodecs.NON_NEGATIVE_INT.fieldOf("enchantment_value").forGetter(ArmorMaterial::enchantmentValue),
            BuiltInRegistries.SOUND_EVENT.holderByNameCodec().fieldOf("equip_sound").forGetter(ArmorMaterial::equipSound),
            ExtraCodecs.NON_NEGATIVE_FLOAT.fieldOf("toughness").forGetter(ArmorMaterial::toughness),
            ExtraCodecs.NON_NEGATIVE_FLOAT.fieldOf("knockback_resistance").forGetter(ArmorMaterial::knockbackResistance),
            TagKey.hashedCodec(Registries.ITEM).fieldOf("repair_items").forGetter(ArmorMaterial::repairIngredient),
            ResourceKey.codec(EquipmentAssets.ROOT_ID).fieldOf("asset_id").forGetter(ArmorMaterial::assetId)
    ).apply(instance, ArmorMaterial::new));
    public static final Codec<ArmorMaterial> CODEC = Codec.either(REGISTRY_CODEC, DIRECT_CODEC).xmap(
            either -> either.map(
                    armorMaterial -> armorMaterial,
                    armorMaterial -> armorMaterial
            ),
            armorMaterial -> REGISTRY.inverse().containsKey(armorMaterial) ? Either.left(armorMaterial) : Either.right(armorMaterial)
    );

    public static ArmorMaterial register(ResourceLocation id, ArmorMaterial armorMaterial) {
        if (REGISTRY.containsKey(id)) {
            throw new IllegalArgumentException("Duplicate armor material id: " + id);
        }

        REGISTRY.put(id, armorMaterial);
        return armorMaterial;
    }

    @Nullable
    public static ArmorMaterial get(ResourceLocation id) {
        return REGISTRY.get(id);
    }

    static {
        // Register vanilla armor materials
        register(ResourceLocation.withDefaultNamespace("leather"), ArmorMaterials.LEATHER);
        register(ResourceLocation.withDefaultNamespace("chainmail"), ArmorMaterials.CHAINMAIL);
        register(ResourceLocation.withDefaultNamespace("iron"), ArmorMaterials.IRON);
        register(ResourceLocation.withDefaultNamespace("gold"), ArmorMaterials.GOLD);
        register(ResourceLocation.withDefaultNamespace("diamond"), ArmorMaterials.DIAMOND);
        register(ResourceLocation.withDefaultNamespace("netherite"), ArmorMaterials.NETHERITE);
        register(ResourceLocation.withDefaultNamespace("turtle_scute"), ArmorMaterials.TURTLE_SCUTE);
        register(ResourceLocation.withDefaultNamespace("armadillo_scute"), ArmorMaterials.ARMADILLO_SCUTE);
    }

}
