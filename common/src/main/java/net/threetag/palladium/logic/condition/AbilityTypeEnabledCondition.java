package net.threetag.palladium.logic.condition;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.threetag.palladium.power.ability.AbilitySerializer;
import net.threetag.palladium.power.ability.AbilityUtil;
import net.threetag.palladium.registry.PalladiumRegistries;
import net.threetag.palladium.registry.PalladiumRegistryKeys;
import net.threetag.palladium.logic.context.DataContext;

public record AbilityTypeEnabledCondition(AbilitySerializer<?> ability) implements Condition {

    public static final MapCodec<AbilityTypeEnabledCondition> CODEC = RecordCodecBuilder.mapCodec(instance -> instance
            .group(PalladiumRegistries.ABILITY_SERIALIZER.byNameCodec().fieldOf("ability_type").forGetter(AbilityTypeEnabledCondition::ability)
            ).apply(instance, AbilityTypeEnabledCondition::new)
    );
    public static final StreamCodec<RegistryFriendlyByteBuf, AbilityTypeEnabledCondition> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.registry(PalladiumRegistryKeys.ABILITY_SERIALIZER), AbilityTypeEnabledCondition::ability, AbilityTypeEnabledCondition::new
    );

    @Override
    public boolean test(DataContext context) {
        var entity = context.getLivingEntity();

        if (entity == null) {
            return false;
        }

        return AbilityUtil.isTypeEnabled(entity, this.ability);
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
        public String getDocumentationDescription() {
            return "Checks if an ability of a certain type is enabled.";
        }

    }
}