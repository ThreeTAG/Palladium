package net.threetag.palladium.logic.condition;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderSet;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.threetag.palladium.documentation.CodecDocumentationBuilder;
import net.threetag.palladium.logic.context.DataContext;
import net.threetag.palladium.power.ability.AbilitySerializer;
import net.threetag.palladium.power.ability.AbilitySerializers;
import net.threetag.palladium.power.ability.AbilityUtil;
import net.threetag.palladium.registry.PalladiumRegistryKeys;
import net.threetag.palladium.util.MixedHolderSet;

public record AbilityTypeEnabledCondition(MixedHolderSet<AbilitySerializer<?>> abilityTypes) implements Condition {

    public static final MapCodec<AbilityTypeEnabledCondition> CODEC = RecordCodecBuilder.mapCodec(instance -> instance
            .group(MixedHolderSet.codec(PalladiumRegistryKeys.ABILITY_SERIALIZER).fieldOf("ability_type").forGetter(AbilityTypeEnabledCondition::abilityTypes)
            ).apply(instance, AbilityTypeEnabledCondition::new)
    );
    public static final StreamCodec<RegistryFriendlyByteBuf, AbilityTypeEnabledCondition> STREAM_CODEC = StreamCodec.composite(
            MixedHolderSet.streamCodec(PalladiumRegistryKeys.ABILITY_SERIALIZER), AbilityTypeEnabledCondition::abilityTypes, AbilityTypeEnabledCondition::new
    );

    @Override
    public boolean test(DataContext context) {
        var entity = context.getLivingEntity();

        if (entity == null) {
            return false;
        }

        for (Holder<AbilitySerializer<?>> abilityType : this.abilityTypes) {
            if (AbilityUtil.isTypeEnabled(entity, abilityType.value())) {
                return true;
            }
        }

        return false;
    }

    @Override
    public ConditionSerializer<AbilityTypeEnabledCondition> getSerializer() {
        return ConditionSerializers.ABILITY_TYPE_ENABLED.get();
    }

    public static class Serializer extends ConditionSerializer<AbilityTypeEnabledCondition> {

        @Override
        public MapCodec<AbilityTypeEnabledCondition> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, AbilityTypeEnabledCondition> streamCodec() {
            return STREAM_CODEC;
        }

        @Override
        public void addDocumentation(CodecDocumentationBuilder<Condition, AbilityTypeEnabledCondition> builder, HolderLookup.Provider provider) {
            builder.setName("Ability Type enabled")
                    .setDescription("Checks if an ability of a certain type is enabled.")
                    .add("ability_type", TYPE_ABILITY_TYPE_HOLDER_SET, "The ID(s) or tag(s) of the ability type that needs to be enabled.")
                    .addExampleObject(new AbilityTypeEnabledCondition(new MixedHolderSet<>(HolderSet.direct(AbilitySerializers.DUMMY))))
                    .addExampleObject(new AbilityTypeEnabledCondition(new MixedHolderSet<>(HolderSet.direct(AbilitySerializers.DUMMY), HolderSet.direct(AbilitySerializers.COMMAND))));
        }
    }
}