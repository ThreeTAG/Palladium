package net.threetag.palladium.logic.condition;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.logic.context.DataContext;
import net.threetag.palladium.power.ability.AbilityInstance;
import net.threetag.palladium.registry.PalladiumRegistries;
import net.threetag.palladium.registry.PalladiumRegistryKeys;
import net.threetag.palladium.util.PalladiumCodecs;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public interface Condition {

    Codec<Condition> DIRECT_CODEC = PalladiumRegistries.CONDITION_SERIALIZER.byNameCodec().dispatch(Condition::getSerializer, ConditionSerializer::codec);

    Codec<Condition> FALSE_TRUE_WRAPPED_CODEC = Codec.either(DIRECT_CODEC, Codec.BOOL).xmap(either -> either.map(
                    left -> left,
                    right -> right ? TrueCondition.INSTANCE : FalseCondition.INSTANCE),
            condition -> condition instanceof TrueCondition ? Either.right(true) : (condition instanceof FalseCondition ? Either.right(false) : Either.left(condition)));

    Codec<List<Condition>> LIST_CODEC = PalladiumCodecs.listOrPrimitive(FALSE_TRUE_WRAPPED_CODEC);
    StreamCodec<RegistryFriendlyByteBuf, Condition> STREAM_CODEC = ByteBufCodecs.registry(PalladiumRegistryKeys.CONDITION_SERIALIZER).dispatch(Condition::getSerializer, ConditionSerializer::streamCodec);

    boolean test(DataContext context);

    default void init(LivingEntity entity, AbilityInstance<?> abilityInstance) {

    }

    ConditionSerializer<?> getSerializer();

    default List<String> getDependentAbilities() {
        return Collections.emptyList();
    }

    static boolean checkConditions(Collection<Condition> conditions, DataContext context) {
        for (Condition condition : conditions) {
            if (!condition.test(context)) {
                return false;
            }
        }

        return true;
    }
}
