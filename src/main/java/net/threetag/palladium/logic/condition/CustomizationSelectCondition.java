package net.threetag.palladium.logic.condition;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderSet;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.threetag.palladium.customization.Customization;
import net.threetag.palladium.customization.EntityCustomizationHandler;
import net.threetag.palladium.documentation.CodecDocumentationBuilder;
import net.threetag.palladium.logic.context.DataContext;
import net.threetag.palladium.registry.PalladiumRegistryKeys;
import net.threetag.palladium.util.MixedHolderSet;

import java.util.List;

public class CustomizationSelectCondition implements Condition {

    public static final MapCodec<CustomizationSelectCondition> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            MixedHolderSet.codec(PalladiumRegistryKeys.CUSTOMIZATION).fieldOf("customization").forGetter(c -> c.customization)
    ).apply(instance, CustomizationSelectCondition::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, CustomizationSelectCondition> STREAM_CODEC = StreamCodec.composite(
            MixedHolderSet.streamCodec(PalladiumRegistryKeys.CUSTOMIZATION), c -> c.customization,
            CustomizationSelectCondition::new
    );

    private final MixedHolderSet<Customization> customization;

    public CustomizationSelectCondition(MixedHolderSet<Customization> customization) {
        this.customization = customization;
    }

    @Override
    public boolean test(DataContext context) {
        var entity = context.getLivingEntity();

        if (entity != null) {
            var handler = EntityCustomizationHandler.get(entity);

            for (Holder<Customization> holder : this.customization) {
                if (handler.isSelected(holder)) {
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public ConditionSerializer<?> getSerializer() {
        return ConditionSerializers.CUSTOMIZATION_SELECTED.get();
    }

    public static class Serializer extends ConditionSerializer<CustomizationSelectCondition> {

        @Override
        public MapCodec<CustomizationSelectCondition> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, CustomizationSelectCondition> streamCodec() {
            return STREAM_CODEC;
        }

        @SuppressWarnings("deprecation")
        @Override
        public void addDocumentation(CodecDocumentationBuilder<Condition, CustomizationSelectCondition> builder, HolderLookup.Provider provider) {
            builder.setName("Customization Selected")
                    .setDescription("Checks if the given customization is currently selected by the entity.")
                    .add("customization", TYPE_CUSTOMIZATION_HOLDER_SET, "ID(s) or tag(s) of the required flight type.")
                    .addExampleObject(new CustomizationSelectCondition(new MixedHolderSet<>(
                            HolderSet.emptyNamed(provider.lookupOrThrow(PalladiumRegistryKeys.CUSTOMIZATION), TagKey.create(PalladiumRegistryKeys.CUSTOMIZATION, Identifier.fromNamespaceAndPath("example", "customization_tag")))
                    )))
                    .addExampleObject(new CustomizationSelectCondition(new MixedHolderSet<>(List.of(
                            HolderSet.emptyNamed(provider.lookupOrThrow(PalladiumRegistryKeys.CUSTOMIZATION), TagKey.create(PalladiumRegistryKeys.CUSTOMIZATION, Identifier.fromNamespaceAndPath("example", "customization_tag_1"))),
                            HolderSet.emptyNamed(provider.lookupOrThrow(PalladiumRegistryKeys.CUSTOMIZATION), TagKey.create(PalladiumRegistryKeys.CUSTOMIZATION, Identifier.fromNamespaceAndPath("example", "customization_tag_2")))
                    ))));
        }
    }
}
