package net.threetag.palladium.power.ability.unlocking;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.threetag.palladium.logic.condition.Condition;
import net.threetag.palladium.power.ability.AbilityInstance;
import net.threetag.palladium.power.ability.AbilityReference;
import net.threetag.palladium.registry.PalladiumRegistries;
import net.threetag.palladium.registry.PalladiumRegistryKeys;

import java.util.List;

public abstract class UnlockingHandler {

    public static final Codec<UnlockingHandler> DIRECT_CODEC = PalladiumRegistries.ABILITY_UNLOCKING_HANDLER_SERIALIZER.byNameCodec().dispatch(UnlockingHandler::getSerializer, UnlockingHandlerSerializer::codec);

    public static final Codec<UnlockingHandler> CODEC = Codec.either(
            DIRECT_CODEC,
            Condition.LIST_CODEC
    ).xmap(
            either -> either.map(
                    left -> left,
                    ConditionalUnlockingHandler::new
            ),
            handler -> handler instanceof ConditionalUnlockingHandler cond ? Either.right(cond.conditions) : Either.left(handler)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, UnlockingHandler> STREAM_CODEC = ByteBufCodecs.registry(PalladiumRegistryKeys.ABILITY_UNLOCKING_HANDLER_SERIALIZER).dispatch(UnlockingHandler::getSerializer, UnlockingHandlerSerializer::streamCodec);

    public abstract boolean check(LivingEntity entity, AbilityInstance<?> abilityInstance);

    public void registerDataComponents(DataComponentMap.Builder builder) {
    }

    public void onClicked(Player player, AbilityInstance<?> abilityInstance) {
    }

    public List<AbilityReference> getParentAbilities() {
        return List.of();
    }

    public abstract UnlockingHandlerSerializer<?> getSerializer();

}
