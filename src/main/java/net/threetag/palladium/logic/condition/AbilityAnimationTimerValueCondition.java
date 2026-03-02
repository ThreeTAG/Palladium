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

public record AbilityAnimationTimerValueCondition(AbilityReference ability, int min, int max) implements Condition {

    public static final MapCodec<AbilityAnimationTimerValueCondition> CODEC = RecordCodecBuilder.mapCodec(instance -> instance
            .group(
                    AbilityReference.CODEC.fieldOf("ability").forGetter(AbilityAnimationTimerValueCondition::ability),
                    PalladiumCodecs.TIME.optionalFieldOf("min", Integer.MIN_VALUE).forGetter(AbilityAnimationTimerValueCondition::min),
                    PalladiumCodecs.TIME.optionalFieldOf("max", Integer.MAX_VALUE).forGetter(AbilityAnimationTimerValueCondition::max)
            ).apply(instance, AbilityAnimationTimerValueCondition::new)
    );
    public static final StreamCodec<RegistryFriendlyByteBuf, AbilityAnimationTimerValueCondition> STREAM_CODEC = StreamCodec.composite(
            AbilityReference.STREAM_CODEC, AbilityAnimationTimerValueCondition::ability,
            ByteBufCodecs.VAR_INT, AbilityAnimationTimerValueCondition::min,
            ByteBufCodecs.VAR_INT, AbilityAnimationTimerValueCondition::max,
            AbilityAnimationTimerValueCondition::new
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
        }

        var timer = dependency.getAnimationTimerValue();
        return timer >= this.min && timer <= this.max;
    }

    @Override
    public ConditionSerializer<AbilityAnimationTimerValueCondition> getSerializer() {
        return ConditionSerializers.ABILITY_ANIMATION_TIMER_VALUE.get();
    }

    public static class Serializer extends ConditionSerializer<AbilityAnimationTimerValueCondition> {

        @Override
        public MapCodec<AbilityAnimationTimerValueCondition> codec() {
            return CODEC;
        }

        @Override
        public void addDocumentation(CodecDocumentationBuilder<Condition, AbilityAnimationTimerValueCondition> builder, HolderLookup.Provider provider) {
            builder.setName("Ability Animation Timer Value")
                    .setDescription("Checks if the given animation timer in an ability has a certain value.")
                    .add("ability", TYPE_ABILITY_REFERENCE, "The ability that is being checked on.")
                    .addOptional("min", TYPE_TIME, "The minimum required value of the current animation timer value of the ability")
                    .addOptional("max", TYPE_TIME, "The maximum required value of the current animation timer value of the ability")
                    .addExampleObject(new AbilityAnimationTimerValueCondition(new AbilityReference(
                            Identifier.fromNamespaceAndPath("example", "power"),
                            "ability_key"
                    ), 5, 10));
        }
    }
}
