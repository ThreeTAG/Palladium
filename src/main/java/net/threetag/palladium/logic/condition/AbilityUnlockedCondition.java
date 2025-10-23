package net.threetag.palladium.logic.condition;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.threetag.palladium.power.ability.AbilityInstance;
import net.threetag.palladium.power.ability.AbilityReference;
import net.threetag.palladium.logic.context.DataContext;

import java.util.List;

public record AbilityUnlockedCondition(AbilityReference ability) implements Condition {

    public static final MapCodec<AbilityUnlockedCondition> CODEC = RecordCodecBuilder.mapCodec(instance -> instance
            .group(AbilityReference.CODEC.fieldOf("ability").forGetter(AbilityUnlockedCondition::ability)
            ).apply(instance, AbilityUnlockedCondition::new)
    );
    public static final StreamCodec<RegistryFriendlyByteBuf, AbilityUnlockedCondition> STREAM_CODEC = StreamCodec.composite(
            AbilityReference.STREAM_CODEC, AbilityUnlockedCondition::ability, AbilityUnlockedCondition::new
    );

    @Override
    public boolean test(DataContext context) {
        var entity = context.getLivingEntity();
        var holder = context.getPowerHolder();

        if (entity == null) {
            return false;
        }

        AbilityInstance<?> dependency = this.ability.getInstance(entity, holder);
        return dependency != null && dependency.isUnlocked();
    }

    @Override
    public List<String> getDependentAbilities() {
        return List.of(this.ability.abilityKey());
    }

    @Override
    public ConditionSerializer<AbilityUnlockedCondition> getSerializer() {
        return ConditionSerializers.ABILITY_UNLOCKED.get();
    }

    public static class Serializer extends ConditionSerializer<AbilityUnlockedCondition> {

        @Override
        public MapCodec<AbilityUnlockedCondition> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, AbilityUnlockedCondition> streamCodec() {
            return STREAM_CODEC;
        }

        @Override
        public String getDocumentationDescription() {
            return "Checks if the ability is unlocked.";
        }
    }
}
