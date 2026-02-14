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

public record AnimationTimerAbilityCondition(AbilityReference ability, int min, int max) implements Condition {

    public static final MapCodec<AnimationTimerAbilityCondition> CODEC = RecordCodecBuilder.mapCodec(instance -> instance
            .group(
                    AbilityReference.CODEC.fieldOf("ability").forGetter(AnimationTimerAbilityCondition::ability),
                    PalladiumCodecs.TIME.optionalFieldOf("min", Integer.MIN_VALUE).forGetter(AnimationTimerAbilityCondition::min),
                    PalladiumCodecs.TIME.optionalFieldOf("max", Integer.MAX_VALUE).forGetter(AnimationTimerAbilityCondition::max)
            ).apply(instance, AnimationTimerAbilityCondition::new)
    );
    public static final StreamCodec<RegistryFriendlyByteBuf, AnimationTimerAbilityCondition> STREAM_CODEC = StreamCodec.composite(
            AbilityReference.STREAM_CODEC, AnimationTimerAbilityCondition::ability,
            ByteBufCodecs.VAR_INT, AnimationTimerAbilityCondition::min,
            ByteBufCodecs.VAR_INT, AnimationTimerAbilityCondition::max,
            AnimationTimerAbilityCondition::new
    );

    @Override
    public boolean test(DataContext context) {
        var entity = context.getLivingEntity();
        var holder = context.getPowerInstance();

        if (entity == null) {
            return false;
        }

        AbilityInstance<?> dependency = this.ability.getInstance(entity, holder);

        if (dependency == null || !(dependency.getAnimationTimer() == null)) {
            return false;
        }

        var timer = dependency.getAnimationTimer().value();
        return timer >= this.min && timer <= this.max;
    }

    @Override
    public ConditionSerializer<AnimationTimerAbilityCondition> getSerializer() {
        return ConditionSerializers.ANIMATION_TIMER_ABILITY.get();
    }

    public static class Serializer extends ConditionSerializer<AnimationTimerAbilityCondition> {

        @Override
        public MapCodec<AnimationTimerAbilityCondition> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, AnimationTimerAbilityCondition> streamCodec() {
            return STREAM_CODEC;
        }

        @Override
        public void addDocumentation(CodecDocumentationBuilder<Condition, AnimationTimerAbilityCondition> builder, HolderLookup.Provider provider) {
            builder.setName("Ability Animation Timer")
                    .setDescription("Checks if the given animation timer in an ability has a certain value.")
                    .add("ability", TYPE_ABILITY_REFERENCE, "The ability that is being checked on.")
                    .addOptional("min", TYPE_TIME, "The minimum required value of the current animation timer value of the ability")
                    .addOptional("max", TYPE_TIME, "The maximum required value of the current animation timer value of the ability")
                    .addExampleObject(new AnimationTimerAbilityCondition(new AbilityReference(
                            Identifier.fromNamespaceAndPath("example", "power"),
                            "ability_key"
                    ), 5, 10));
        }
    }
}
