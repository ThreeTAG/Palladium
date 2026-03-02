package net.threetag.palladium.logic.condition;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.logic.context.DataContext;
import net.threetag.palladium.power.ability.AbilityInstance;
import net.threetag.palladium.registry.PalladiumRegistries;
import net.threetag.palladium.util.PalladiumCodecs;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

public interface Condition {

    Codec<Condition> DIRECT_CODEC = PalladiumRegistries.CONDITION_SERIALIZER.byNameCodec().dispatch(Condition::getSerializer, ConditionSerializer::codec);

    Codec<Condition> FALSE_TRUE_WRAPPED_CODEC = Codec.either(DIRECT_CODEC, Codec.BOOL).xmap(either -> either.map(
                    Function.identity(),
                    right -> right ? TrueCondition.INSTANCE : FalseCondition.INSTANCE),
            condition -> condition instanceof TrueCondition ? Either.right(true) : (condition instanceof FalseCondition ? Either.right(false) : Either.left(condition)));

    Codec<Condition> MOLANG_WRAPPED_CODEC = Codec.either(FALSE_TRUE_WRAPPED_CODEC, Codec.STRING).xmap(either -> either.map(
                    Function.identity(),
                    MoLangCondition::new),
            condition -> condition instanceof MoLangCondition molang ? Either.right(molang.getMolang()) : Either.left(condition));

    Codec<Condition> CODEC = PalladiumCodecs.listOrPrimitive(MOLANG_WRAPPED_CODEC).xmap(AndCondition::new, condition -> condition instanceof AndCondition(
            List<Condition> conditions
    ) ? conditions : Collections.singletonList(condition));

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
