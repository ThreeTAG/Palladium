package net.threetag.palladium.logic.condition;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.threetag.palladium.documentation.CodecDocumentationBuilder;
import net.threetag.palladium.logic.context.DataContext;
import net.threetag.palladium.power.EntityPowerHandler;
import net.threetag.palladium.power.Power;
import net.threetag.palladium.power.PowerUtil;
import net.threetag.palladium.registry.PalladiumRegistryKeys;
import net.threetag.palladium.tag.PalladiumPowerTags;
import net.threetag.palladium.util.MixedHolderSet;

import java.util.List;

public record HasPowerCondition(MixedHolderSet<Power> powers) implements Condition {

    public static final MapCodec<HasPowerCondition> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                    MixedHolderSet.codec(PalladiumRegistryKeys.POWER).fieldOf("power").forGetter(HasPowerCondition::powers)
            ).apply(instance, HasPowerCondition::new)
    );
    public static final StreamCodec<RegistryFriendlyByteBuf, HasPowerCondition> STREAM_CODEC = StreamCodec.composite(
            MixedHolderSet.streamCodec(PalladiumRegistryKeys.POWER), HasPowerCondition::powers, HasPowerCondition::new
    );

    @Override
    public boolean test(DataContext context) {
        var entity = context.getLivingEntity();

        if (entity == null) {
            return false;
        }

        EntityPowerHandler handler = PowerUtil.getPowerHandler(entity);

        for (Holder<Power> powerHolder : this.powers) {
            if (handler.hasPower(powerHolder)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public ConditionSerializer<HasPowerCondition> getSerializer() {
        return ConditionSerializers.HAS_POWER.get();
    }

    public static class Serializer extends ConditionSerializer<HasPowerCondition> {

        @Override
        public MapCodec<HasPowerCondition> codec() {
            return CODEC;
        }

        @Override
        public void addDocumentation(CodecDocumentationBuilder<Condition, HasPowerCondition> builder, HolderLookup.Provider provider) {
            builder.setName("Has Power")
                    .setDescription("Checks if the entity has specified power.")
                    .add("power", TYPE_POWER_HOLDER_SET, "IDs or tags of power that need to be on the entity.")
                    .addExampleObject(new HasPowerCondition(new MixedHolderSet<>(provider.lookupOrThrow(PalladiumRegistryKeys.POWER).getOrThrow(PalladiumPowerTags.IS_MECHANICAL))))
                    .addExampleObject(new HasPowerCondition(new MixedHolderSet<>(List.of(
                            provider.lookupOrThrow(PalladiumRegistryKeys.POWER).getOrThrow(PalladiumPowerTags.IS_GENETIC),
                            provider.lookupOrThrow(PalladiumRegistryKeys.POWER).getOrThrow(PalladiumPowerTags.IS_MAGICAL)
                    ))));
        }
    }
}
