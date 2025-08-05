package net.threetag.palladium.customization;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.Util;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.RegistryFixedCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.phys.Vec3;
import net.threetag.palladium.client.PoseStackTransformation;
import net.threetag.palladium.registry.PalladiumRegistryKeys;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public record CustomizationCategory(int sortIndex, CustomizationPreview preview,
                                    @Nullable EquipmentSlot hiddenByEquipment) {

    public static final CustomizationPreview DEFAULT_PREVIEW = new CustomizationPreview(1, Vec3.ZERO, new Vec3(15, 40, 0));

    public static final Codec<CustomizationCategory> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.INT.optionalFieldOf("sort_index", 100).forGetter(CustomizationCategory::sortIndex),
            CustomizationPreview.CODEC.optionalFieldOf("preview", DEFAULT_PREVIEW).forGetter(CustomizationCategory::preview),
            EquipmentSlot.CODEC.optionalFieldOf("hidden_by_equipment").forGetter(s -> Optional.ofNullable(s.hiddenByEquipment))
    ).apply(instance, (s, p, h) -> new CustomizationCategory(s, p, h.orElse(null))));

    public static final Codec<Holder<CustomizationCategory>> HOLDER_CODEC = RegistryFixedCodec.create(PalladiumRegistryKeys.CUSTOMIZATION_CATEGORY);

    public static String makeDescriptionId(CustomizationCategory slot, RegistryAccess registryAccess) {
        return Util.makeDescriptionId("customization_category", registryAccess.lookupOrThrow(PalladiumRegistryKeys.CUSTOMIZATION_CATEGORY).getKey(slot));
    }

    public static String makeDescriptionId(ResourceKey<CustomizationCategory> key) {
        return Util.makeDescriptionId("customization_category", key.location());
    }

}
