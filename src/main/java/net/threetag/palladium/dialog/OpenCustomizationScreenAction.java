package net.threetag.palladium.dialog;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.nbt.StringTag;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.dialog.action.Action;
import net.threetag.palladium.customization.CustomizationCategory;
import net.threetag.palladium.registry.PalladiumRegistryKeys;

import java.util.Map;
import java.util.Optional;

public record OpenCustomizationScreenAction(Optional<ResourceKey<CustomizationCategory>> category) implements Action {

    public static final MapCodec<OpenCustomizationScreenAction> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            ResourceKey.codec(PalladiumRegistryKeys.CUSTOMIZATION_CATEGORY).optionalFieldOf("category").forGetter(OpenCustomizationScreenAction::category)
    ).apply(instance, OpenCustomizationScreenAction::new));

    @Override
    public MapCodec<? extends Action> codec() {
        return CODEC;
    }

    @Override
    public Optional<ClickEvent> createAction(Map<String, ValueGetter> valueGetters) {
        return Optional.of(new ClickEvent.Custom(PalladiumDialogActions.OPEN_CUSTOMIZATION_SCREEN.getId(),
                Optional.of(StringTag.valueOf(this.category.map(key -> key.identifier().toString()).orElse("")))));
    }

}
