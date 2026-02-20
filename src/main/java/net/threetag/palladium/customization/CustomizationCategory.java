package net.threetag.palladium.customization;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.RegistryFixedCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.Util;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.phys.Vec3;
import net.threetag.palladium.logic.condition.Condition;
import net.threetag.palladium.logic.context.DataContext;
import net.threetag.palladium.registry.PalladiumRegistryKeys;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public record CustomizationCategory(int sortIndex, CustomizationPreview preview, boolean requiresSelection,
                                    Optional<ResourceKey<Customization>> defaultValue,
                                    @Nullable EquipmentSlot hiddenByEquipment, @Nullable Condition visibility) {

    public static final CustomizationPreview DEFAULT_PREVIEW = new CustomizationPreview(1, Vec3.ZERO, new Vec3(15, 40, 0));

    private static final Codec<CustomizationCategory> UNVALIDATED_CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.INT.optionalFieldOf("sort_index", 100).forGetter(CustomizationCategory::sortIndex),
            CustomizationPreview.CODEC.optionalFieldOf("preview", DEFAULT_PREVIEW).forGetter(CustomizationCategory::preview),
            Codec.BOOL.optionalFieldOf("requires_selection", false).forGetter(CustomizationCategory::requiresSelection),
            ResourceKey.codec(PalladiumRegistryKeys.CUSTOMIZATION).optionalFieldOf("default").forGetter(CustomizationCategory::defaultValue),
            EquipmentSlot.CODEC.optionalFieldOf("hidden_by_equipment").forGetter(c -> Optional.ofNullable(c.hiddenByEquipment)),
            Condition.CODEC.optionalFieldOf("visibility").forGetter(c -> Optional.ofNullable(c.visibility))
    ).apply(instance, (s, p, rs, dv, h, v)
            -> new CustomizationCategory(s, p, rs, dv, h.orElse(null), v.orElse(null))));
    public static final Codec<CustomizationCategory> CODEC = UNVALIDATED_CODEC.validate(category -> {
        if (category.requiresSelection() && category.defaultValue().isEmpty()) {
            return DataResult.error(() -> "Category needs a default if requires_selection is set to true!");
        } else {
            return DataResult.success(category);
        }
    });

    public static final Codec<Holder<CustomizationCategory>> HOLDER_CODEC = RegistryFixedCodec.create(PalladiumRegistryKeys.CUSTOMIZATION_CATEGORY);

    public static String makeDescriptionId(CustomizationCategory slot, RegistryAccess registryAccess) {
        return Util.makeDescriptionId("customization_category", registryAccess.lookupOrThrow(PalladiumRegistryKeys.CUSTOMIZATION_CATEGORY).getKey(slot));
    }

    public static String makeDescriptionId(ResourceKey<CustomizationCategory> key) {
        return Util.makeDescriptionId("customization_category", key.identifier());
    }

    public boolean isVisible(DataContext context) {
        return this.visibility == null || this.visibility.test(context);
    }

    @Nullable
    public Holder<Customization> getDefaultValue(RegistryAccess registryAccess) {
        if (this.requiresSelection() && this.defaultValue().isPresent()) {
            return registryAccess.lookupOrThrow(PalladiumRegistryKeys.CUSTOMIZATION).getOrThrow(this.defaultValue().get());
        } else {
            return null;
        }
    }

}
