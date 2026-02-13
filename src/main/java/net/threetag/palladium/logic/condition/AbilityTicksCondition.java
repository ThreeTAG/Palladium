package net.threetag.palladium.logic.condition;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import net.threetag.palladium.documentation.CodecDocumentationBuilder;
import net.threetag.palladium.logic.context.DataContext;
import net.threetag.palladium.power.ability.AbilityInstance;
import net.threetag.palladium.power.ability.AbilityReference;
import net.threetag.palladium.util.PalladiumCodecs;

public record AbilityTicksCondition(AbilityReference ability, int min,
                                    int max) implements Condition {

    public static final MapCodec<AbilityTicksCondition> CODEC = RecordCodecBuilder.mapCodec(instance -> instance
            .group(
                    AbilityReference.CODEC.fieldOf("ability").forGetter(AbilityTicksCondition::ability),
                    PalladiumCodecs.TIME.optionalFieldOf("min", Integer.MIN_VALUE).forGetter(AbilityTicksCondition::min),
                    PalladiumCodecs.TIME.optionalFieldOf("max", Integer.MAX_VALUE).forGetter(AbilityTicksCondition::max)
            ).apply(instance, AbilityTicksCondition::new)
    );
    public static final StreamCodec<RegistryFriendlyByteBuf, AbilityTicksCondition> STREAM_CODEC = StreamCodec.composite(
            AbilityReference.STREAM_CODEC, AbilityTicksCondition::ability,
            ByteBufCodecs.VAR_INT, AbilityTicksCondition::min,
            ByteBufCodecs.VAR_INT, AbilityTicksCondition::max,
            AbilityTicksCondition::new
    );

    @Override
    public boolean test(DataContext context) {
        var entity = context.getLivingEntity();
        var holder = context.getPowerInstance();

        if (entity == null) {
            return false;
        }

        AbilityInstance<?> dependency = this.ability.getInstance(entity, holder);

        if (dependency == null) {
            return false;
        } else {
            return this.min <= dependency.getEnabledTicks() && dependency.getEnabledTicks() <= this.max;
        }
    }

    @Override
    public ConditionSerializer<AbilityTicksCondition> getSerializer() {
        return ConditionSerializers.ABILITY_TICKS.get();
    }

    public static class Serializer extends ConditionSerializer<AbilityTicksCondition> {

        @Override
        public MapCodec<AbilityTicksCondition> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, AbilityTicksCondition> streamCodec() {
            return STREAM_CODEC;
        }

        @Override
        public void addDocumentation(CodecDocumentationBuilder<Condition, AbilityTicksCondition> builder, HolderLookup.Provider provider) {
            builder.setName("Ability Ticks")
                    .setDescription("Checks if the ability has been enabled for a certain amount of ticks.")
                    .add("ability", TYPE_ABILITY_REFERENCE, "The ability that is being checked on.")
                    .addOptional("min", TYPE_TIME, "The minimum required value of the current tick count of the ability.")
                    .addOptional("max", TYPE_TIME, "The maximum required value of the current tick count of the ability.")
                    .addExampleObject(new AbilityTicksCondition(new AbilityReference(
                            Identifier.fromNamespaceAndPath("example", "power"),
                            "ability_key"
                    ), 5, 10));
        }
    }
}
